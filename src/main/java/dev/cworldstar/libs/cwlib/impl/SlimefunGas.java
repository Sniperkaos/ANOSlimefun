package dev.cworldstar.libs.cwlib.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Chunk;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.impl.breathing.Breathing;
import dev.cworldstar.libs.cwlib.impl.breathing.BreathingAction;
import dev.cworldstar.libs.cwlib.impl.gas.ChunkGasStorage;
import dev.cworldstar.libs.cwlib.impl.gas.GasStorage;
import dev.cworldstar.libs.cwlib.impl.gas.GasStorage.StoredGas;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class SlimefunGas {
	
	private static ArrayList<SlimefunGas> gasRegistry = new ArrayList<SlimefunGas>();
	
	public static List<SlimefunGas> getRegistry() {
		return Collections.unmodifiableList(gasRegistry);
	}
		
	public static SlimefunGas AIR;
	public static SlimefunGas NETHER_AIR;
	public static SlimefunGas END_AIR;
	
	static {
		AIR = new SlimefunGas("sfgas.air");
		NETHER_AIR = new SlimefunGas("sfgas.netherair", new GasProperties()
				.density(0.12F)
				.pressure(121.5F)
				.temperature(300F)
				.entityAtmosphereBreathe((entity, action, gases) -> {
					// check how much gas exists in our current chunk
					for(StoredGas gas : gases.gases()) {
						if(gas.isOfType(SlimefunGas.NETHER_AIR)) {
							float volume = SlimefunGas.getChunkGasVolume(gas);
							SlimefunGasSeverity severity = SlimefunGasSeverity.getSeverity(volume);
							// if we're minor polluted, we want to set the entity on fire
							if(
								// check if the entity lungs contains nether air AND that
								// the entity is in an environment with nether air.
								severity.canTrigger(SlimefunGasSeverity.MINOR_POLLUTION) && 
								Breathing.getLungs(entity).containsAtLeast(20, NETHER_AIR)
							) {
								action.complete((breather) -> {
									// check if the player is filtered
									float effectiveness = Breathing.isFiltered(breather, NETHER_AIR);
									
									if(effectiveness >= 1.0) return;
									breather.setFireTicks(Math.round((severity.getMaxPollution() / 10) * (20 * effectiveness)));
								});
							}
						}
					}
				})
		);
		END_AIR = new SlimefunGas("sfgas.endair");
	}
	
	public static class GasProperties {
		
		public @Getter float density = 0.076F;
		public @Getter float temperature = 68F;
		
		public float asKelvins() {
			return (temperature - 32F) * (5F / 9F) + 273.15F;
		}
		
		public @Getter float pressure = 101.325F;
		
		public @Getter boolean hazardous = false;
		public @Getter float damage = 0.0F;
		public @Getter DamageType damageType = DamageType.OUT_OF_WORLD;
		
		protected TriConsumer<LivingEntity, BreathingAction, GasStorage> entityAtmosphereBreathe;
		protected BiConsumer<LivingEntity, GasStorage> entityAtmosphereTick;
		
		public void triggerBreathingAction(LivingEntity who, BreathingAction action, GasStorage atmosphere) {
			if(entityAtmosphereBreathe == null) {
				action.complete(null);
				return;
			}
			entityAtmosphereBreathe.accept(who, action, atmosphere);
		}
		
		public void triggerAtmosphere(LivingEntity who, GasStorage atmosphere) {
			if(entityAtmosphereTick == null) {
				return;
			}
			entityAtmosphereTick.accept(who, atmosphere);
		}
		
		public GasProperties() {
			
		}
		
		/**
		 * This method is called whenever a {@link LivingEntity} ticks trying to breathe.
		 * If the atmosphere contains above a 10% volume of this gas, {@link #entityAtmosphereBreathe} is triggered.
		 * This differs from a {@link SlimefunGasEffect}.
		 * On average, a {@link LivingEntity} will breathe once every two seconds.
		 * 
		 * 
		 * @param onEntityAtmosphereBreathe
		 * @return
		 */
		public GasProperties entityAtmosphereBreathe(TriConsumer<LivingEntity, BreathingAction, GasStorage> onEntityAtmosphereBreathe) {
			this.entityAtmosphereBreathe = onEntityAtmosphereBreathe;
			return this;
		}
		
		/**
		 * This method is called whenever a {@link LivingEntity} ticks in an atmosphere.
		 * If the atmosphere contains above a 10% volume of this gas, {@link #entityAtmosphereTick} is triggered.
		 * Used internally for atmospheric damaging outside of breathing.
		 * This differs from a {@link SlimefunGasEffect}.
		 * This ticks in the same function that triggers {@link #entityAtmosphereBreathe}.
		 * 
		 */
		public GasProperties entityAtmosphereTick(BiConsumer<LivingEntity, GasStorage> onEntityAtmosphereTick) {
			this.entityAtmosphereTick = onEntityAtmosphereTick;
			return this;
		}
		
		public GasProperties damage(float damage) {
			this.damage = damage;
			return this;
		}
		
		public GasProperties damageType(DamageType type) {
			damageType = type;
			return this;
		}
		
		public GasProperties density(float density) {
			this.density = density;
			return this;
		}
		
		public GasProperties hazardous(boolean isHazardous) {
			hazardous = isHazardous;
			return this;
		}
		
		public GasProperties temperature(float temperature) {
			this.temperature = temperature;
			return this;
		}
		
		public GasProperties pressure(float pressure) {
			this.pressure = pressure;
			return this;
		}
	}
	
	private final @Getter String gasId;
	private final @Getter GasProperties properties;
	/*
	
	TODO: complete this, it should intercept chunk packets and change the biome hue based on pollution.
	NEED NMS, too lazy to finish
	
	static {
		if(PluginIntegrations.isProtocolLibInstalled()) {
			ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(AbstractSFAddon.get(), PacketType.Play.Server.MAP_CHUNK) {
			    @Override
			    public void onPacketSending(PacketEvent event) {
			        PacketContainer packet = event.getPacket();
			        int chunkX = packet.getIntegers().read(0);
			        int chunkZ = packet.getIntegers().read(1);
			        
			       // List<ChunkGas> pollutionValue = SlimefunGas.getPollutionValues();
			    }
			});
		}
	}*/
	
	/**
	 * This method calculates how much gas is in the atmosphere,
	 * where a volume of 100 is equal to {@link #CHUNK_BLOCK_SIZE}.
	 * For example, {@link #NETHER_AIR} will fill an atmosphere at a volume of
	 * ~21,000 moles, while {@link #AIR} might take ~50,000 moles.
	 * 
	 * @author cworldstar
	 * @param {@link StoredGas} gas
	 * @return the atmospheric volume of this {@link StoredGas}
	 */
	public static float getChunkGasVolume(StoredGas gas) {
		SlimefunGas sfgas = gas.toSlimefunGas();
		GasProperties properties = sfgas.getProperties();
		return 100 * (
			1-(float)Math.exp(
				-gas.getVolume() * (properties.getPressure() * properties.getDensity()) / ChunkGasStorage.CHUNK_BLOCK_SIZE
			)
		);
	}
	
	public @NotNull static ChunkGasStorage getChunkGasStorage(Chunk chunk) {
		PersistentDataContainer chunkDataContainer = chunk.getPersistentDataContainer();
		ChunkGasStorage storage = chunkDataContainer.get(ChunkGasStorage.key(), ChunkGasStorage.dataType());
		if(storage == null) {
			// doesn't exist, make it
			chunkDataContainer.set(ChunkGasStorage.key(), ChunkGasStorage.dataType(), new ChunkGasStorage());
			storage = chunkDataContainer.get(ChunkGasStorage.key(), ChunkGasStorage.dataType());
		}
		return storage;
	}
	
	public float pressure(float volume) {
		return volume * getProperties().getPressure();
	}
	
	public static final List<StoredGas> getChunkGasValues(Chunk chunk) {
		return getChunkGasStorage(chunk).gases();
	}
	
	public static final float getPollutionValue(SlimefunGas gas, Chunk chunk) {
		ChunkGasStorage storage = getChunkGasStorage(chunk);
		Optional<StoredGas> chunkGas = storage.getGas(gas);
		if(chunkGas.isEmpty()) {
			return 0.0F;
		}
		return getChunkGasVolume(chunkGas.get());
	}
	
	public void addPollutionValue(Chunk chunk, float value) {
		ChunkGasStorage storage = getChunkGasStorage(chunk);
		Optional<StoredGas> chunkGas = storage.getGas(this);
		if(!chunkGas.isPresent()) {
			storage.addGas(new StoredGas(this, value));
		} else {
			chunkGas.get().addVolume(value);
		}
		chunk.getPersistentDataContainer().set(ChunkGasStorage.key(), ChunkGasStorage.dataType(), storage);
	}
	
	public float getVolumeFromPercentage(GasStorage environment, float percent) {
		GasProperties properties = getProperties();
		float containerSize = environment.getSize();
		
		return (-containerSize) * (float) Math.log(1F - percent / 100F) / (properties.getPressure() * properties.getDensity());
	}
	
	@AllArgsConstructor
	public static class SlimefunGasEffect {
		private @Getter PotionEffect effect;
		private @Getter SlimefunGas gas;
		private @Getter SlimefunGasSeverity severity;
	}
	
	public static enum SlimefunGasSeverity {
		NON_POLLUTED(0, 10),
		MINOR_POLLUTION(11, 25),
		POLLUTED(26, 75),
		SEVERE_POLLUTION(76, 101);

		private @Getter int minPollution;
		private @Getter int maxPollution;
		
		public boolean test(float pollution) {
			return (pollution >= minPollution && pollution < maxPollution);
		}
	
		public boolean canTrigger(SlimefunGasSeverity severity) {
			return this.getMinPollution() > severity.getMaxPollution() || this.equals(severity);
		}
		
		SlimefunGasSeverity(int minPollution, int maxPollution) {
			
			minPollution = Math.clamp(minPollution, 0, 99);
			maxPollution = Math.clamp(maxPollution, 1, 100);
			
			if(minPollution > maxPollution) {
				maxPollution = minPollution + 1;
			}
			
			this.minPollution = minPollution;
			this.maxPollution = maxPollution;
		}
		
		public static @NotNull SlimefunGasSeverity getSeverity(float pollution) {
			for(SlimefunGasSeverity severity : SlimefunGasSeverity.values()) {
				if(severity.test(pollution)) {
					return severity;
				}
			}
			return SlimefunGasSeverity.NON_POLLUTED;
		}
	}
	
	private List<SlimefunGasEffect> pollutionEffects = new ArrayList<>();
	
	public List<SlimefunGasEffect> getPollutionEffects() {
		return pollutionEffects.stream().toList();
	}
	
	public void addPollutionEffect(PotionEffect effect, SlimefunGasSeverity atSeverity) {
		pollutionEffects.add(new SlimefunGasEffect(effect, this, atSeverity));
	}
	
	public SlimefunGas(String gasId) {
		this(gasId, new GasProperties());
	}
	
	public SlimefunGas(String gasId, GasProperties properties) {
		this.gasId = gasId;
		this.properties = properties;
		
		gasRegistry.add(this);
	}

	public float volume(float pressure) {
		return pressure / this.getProperties().getPressure();
	}
}
