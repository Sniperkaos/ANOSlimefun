package dev.cworldstar.anosf.impl.radiation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;
import lombok.Getter;
import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.events.SFTickEvent;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet;
import dev.cworldstar.libs.cwlib.impl.armor.ArmorSetPiece;
import dev.cworldstar.libs.cwlib.listeners.AbstractListener;

public class ExtendedRadiation extends AbstractListener {
	
	public ExtendedRadiation(ANOSF addon) {
		super(addon);
	}

	@EventHandler
	public void onTickEvent(SFTickEvent tickEvent) {
		// radiation ticks
		if(!tickEvent.subtick(20)) return;
		
		for(Entry<Location, RadiationEmitter> entry : radiationEmitters.entrySet()) {
			Location emitterLocation = entry.getKey();
			RadiationEmitter emitter = entry.getValue();
			int strength = emitter.getStrength();
			
			ExtendedRadiationInfo info = emitter.getRadiationInfo();
			
			Collection<Entity> nearbyEntities = emitterLocation.getNearbyEntities(strength * 2, strength * 2, strength * 2);
			for(Entity e : nearbyEntities) {
				if(e instanceof LivingEntity living) {
					double distance = emitterLocation.subtract(e.getLocation()).toVector().length();
					if(distance <= strength) {
						applyRadiation(living, info, 1 * distance);
					} else {
						applyRadiation(living, info, 0.5 * distance);
					}
				}
			}
		}
	}
	
	private void applyRadiation(LivingEntity living, ExtendedRadiationInfo info, double severity) {
		if (entityIsProtected(living, info)) {
			return;
		}
	}

	@Getter
	private HashMap<Location, RadiationEmitter> radiationEmitters = new HashMap<Location, RadiationEmitter>();
	@Getter
	private HashMap<Location, ExtendedRadiationInfo> radiationInfoCache = new HashMap<Location, ExtendedRadiationInfo>();

	public boolean entityIsProtected(LivingEntity entity, ExtendedRadiationInfo info) {
		EntityEquipment playerInventory = entity.getEquipment();
		ItemStack[] armorContents = playerInventory.getArmorContents();
		int protection = 0;
		for(ItemStack item : armorContents) {
			if(item == null) continue;
			SlimefunItem potentialItem = SlimefunItem.getByItem(item);
			
			if(potentialItem instanceof ArmorSetPiece piece) {
				AbstractArmorSet set = piece.getArmorSet();
				if(set instanceof RadiationProtector protector) {
					boolean isProtected = protector.protectsFrom(info.getType(), info.getSeverity());
					if(isProtected) {
						protection = protector.getProtectionValue(); 
						break;
					}
				}
			}
			
			if(potentialItem == null) {
				continue; // whole body has to be protected
			}
			if(!(potentialItem instanceof RadiationProtector)) {
				continue; // whole body must be radiation protecting suit
			}
			
			RadiationProtector suit = (RadiationProtector) potentialItem;
			if(!suit.protectsFrom(info.getType(), info.getSeverity())) { // radiation suit must protect from the type and severity
				continue;
			}
			protection += suit.getProtectionValue();
		}
		return protection >= 4;
	}
	
	public ExtendedRadiationInfo getRadiationInfo(Location l) {
		Optional<ExtendedRadiationInfo> info = Optional.ofNullable(radiationInfoCache.get(l));
		if(!info.isPresent()) {
			return ExtendedRadiationInfo.empty();
		}
		return info.get();
	}

	public void registerEmitter(RadiationEmitter emitter, Location emitterLocation, int spread) {
		radiationEmitters.put(emitterLocation, emitter);
	}
	
	public void removeEmitter(Location emitterLocation) {
		RadiationEmitter emitter = radiationEmitters.get(emitterLocation);
		Validate.notNull(emitter, "The emitter at " + emitterLocation.toString() + " does not exist.");
		radiationEmitters.remove(emitterLocation);
		emitter.onEmitterRemove(emitterLocation);
	}
	
}
