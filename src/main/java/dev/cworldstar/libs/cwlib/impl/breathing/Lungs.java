package dev.cworldstar.libs.cwlib.impl.breathing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.libs.cwlib.events.LivingEntityBreatheEvent;
import dev.cworldstar.libs.cwlib.impl.SlimefunGas;
import dev.cworldstar.libs.cwlib.impl.gas.GasStorage;
import dev.cworldstar.libs.cwlib.impl.gas.GasStorage.StoredGas;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Lungs implements Serializable {

	public Lungs() {
		tryAddGas(SlimefunGas.AIR, SlimefunGas.AIR.volume(101F));
	}
	
	public static enum LungAction {
		EXHALING,
		INHALING
	}
	
	private static final long serialVersionUID = -8779763175991639148L;
	
	private static final LungsDataType LUNGS_DATA_TYPE = new LungsDataType();
	
	@AllArgsConstructor
	public static class LungGasPressure implements Serializable {
		private static final long serialVersionUID = -8143939999601796302L;
		private @Getter String gas;
		private @Getter float volume;
		
		public SlimefunGas getAsSlimefunGas() {
			return SlimefunGas.getRegistry().stream().filter(gas -> gas.getGasId().contentEquals(this.gas)).findFirst().orElse(SlimefunGas.AIR);
		}

		public boolean gasMatches(SlimefunGas sfgas) {
			return sfgas.getGasId().contentEquals(gas);
		}

		public float getPressure() {
			return getAsSlimefunGas().pressure(volume);
		}
		
		public float getLungPercentage() {
			return (getPressure() / MAX_PRESSURE) * 100F;
		}
		
		public void increaseVolume(float volume) {
			this.volume += volume;
		}
		
		public void decreaseVolume(float volume) {
			this.volume = Math.max(this.volume - volume, 0);
		}

		public LungGasPressure(SlimefunGas gas, float volume) {
			this(gas.getGasId(), volume);
		}
	}
	
	public static final float MAX_PRESSURE = 314.43F;
	
	private ArrayList<LungGasPressure> gases = new ArrayList<>();
	private @Getter LungAction currentAction = LungAction.INHALING;
	
	public float calculateCurrentPressure() {
		float currentPressure = 0.0F;
		for(LungGasPressure lungPressureValue : this.gases) {
			float volume = lungPressureValue.getPressure();
			currentPressure += volume;
		}
		return currentPressure;
	}
	
	public boolean containsGas(SlimefunGas gas) {
		return gases.stream().anyMatch(gp -> gp.gasMatches(gas));
	}
		
	/**
	 * Returns a boolean based on if the current Lungs instance
	 * contains at least %percentage% amount of the SlimefunGas.
	 * Ignores pressure, you could have 20kPa of gas in your lungs
	 * (~0.5 moles)
	 * and it would still return true.
	 * @param percentage
	 * @param gas
	 * @return
	 */
	public boolean containsAtLeast(int percentage, SlimefunGas gas) {
		boolean hasGas = containsGas(gas);
		if(!hasGas) {
			return false;
		}
		LungGasPressure pressure = getGas(p ->  p.gasMatches(gas));
		return pressure.getLungPercentage() >= percentage;
	}
	
	public @Nullable LungGasPressure getGas(Predicate<LungGasPressure> filter) {
		return gases.stream().filter(filter).findFirst().orElse(null);
	}
	
	public void breathe(LivingEntity owner) {
		LivingEntityBreatheEvent event = new LivingEntityBreatheEvent(
			owner,
			this,
			Breathing.getAtmosphere(owner),
			currentAction
		);
		
		Bukkit.getPluginManager().callEvent(event);
		
		if(event.isCancelled()) {
			return;
		}
		
		switch(currentAction) {
			case INHALING:
				inhale(owner, event.getEnvironment());
				currentAction = LungAction.EXHALING;
				break;
			case EXHALING:
				exhale(owner, event.getEnvironment());
				currentAction = LungAction.INHALING;
				break;
		}
	}
	
	public void triggerReactions(LivingEntity owner, GasStorage environment) {
		for(LungGasPressure gas : gases) {
			SlimefunGas sfGas = gas.getAsSlimefunGas();
			
			BreathingAction action = new BreathingAction(
				this,
				this.getCurrentAction(),
				owner
			);
			
			action.whenComplete((consumer, err) -> {
				if(consumer == null) return;
				consumer.accept(owner);
			});
			
			sfGas.getProperties().triggerBreathingAction(
				owner, 
				action, 
				environment
			);
		}
	}
	
	protected void inhale(LivingEntity owner, GasStorage atmosphere) {
		float inhalePressure = 101F;
		HashMap<StoredGas, Float> atmos = new HashMap<>();
		float atmospherePressure = atmosphere.getAtmospherePressure();
		for(StoredGas gas : atmosphere.gases()) {
			float atmospherePercentage = gas.getPressure() / atmospherePressure;
			float inhaleVolume = inhalePressure * atmospherePercentage;
			atmos.put(gas, inhaleVolume);
		}
		
		for(Entry<StoredGas, Float> atmosEntry : atmos.entrySet()) {
			tryAddGas(atmosEntry.getKey().toSlimefunGas(), atmosEntry.getKey().toSlimefunGas().volume(atmosEntry.getValue()));
		}
		triggerReactions(owner, atmosphere);
	}
	
	protected void exhale(LivingEntity owner, GasStorage atmosphere) {
		float removal = 101F;
		for(LungGasPressure pressure : gases) {
			// remove ~101kPa from the lungs
			SlimefunGas gas = pressure.getAsSlimefunGas();
			float gasPressure = Math.min(pressure.getPressure(), removal);
			if(gasPressure < removal) {
				removal = removal - gasPressure;
			}
			float volume = gas.volume(gasPressure);
			pressure.decreaseVolume(volume);
			if(removal <= 0) {
				break;
			}
		}
		gases.removeIf(gas -> gas.getVolume() <= 0);
	}
	
	private void addGas(SlimefunGas gas, float volume) {
		for(LungGasPressure pressure : gases) {
			if(pressure.gasMatches(gas)) {
				pressure.increaseVolume(volume);
				break;
			}
		}
	}
	
	private void newGas(SlimefunGas gas, float volume) {
		gases.add(new LungGasPressure(gas, volume));
	}
	
	public void tryAddGas(SlimefunGas gas, float volume) {
		float pressure = gas.pressure(volume);
		float currentPressure = calculateCurrentPressure();
		float newPressure = (currentPressure + pressure);
		
		if(newPressure > MAX_PRESSURE) {
			// calculate how much we can fit before the pressure overflows
			float allowedPressure = MAX_PRESSURE - currentPressure;
			if(allowedPressure <= 0) return; // we can't fit any gas;
			
			// now we convert the pressure into volume
			float newVolume = gas.volume(allowedPressure);
			
			if(containsGas(gas)) {
				addGas(gas, newVolume);
			} else {
				newGas(gas, newVolume);
			}
		} else {
			if(containsGas(gas)) {
				addGas(gas, volume);
			} else {
				newGas(gas, volume);
			}
		}
	}

	public static Lungs fromBytes(byte[] bytes) {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
		ObjectInputStream stream;
		try {
			stream = new ObjectInputStream(byteStream);
			Lungs lungs = (Lungs) stream.readObject();
			stream.close();
			return lungs;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] toBytes() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream stream;
		try {
			stream = new ObjectOutputStream(byteStream);
			stream.writeObject(this);
			stream.close();
			return byteStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	public static @NotNull PersistentDataType<byte[], Lungs> dataType() {
		return LUNGS_DATA_TYPE;
	}
}
