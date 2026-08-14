package dev.cworldstar.anosf.impl.radiation;

import org.bukkit.Location;
import org.bukkit.block.Block;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.impl.radiation.ExtendedRadiationInfo.RadiationSeverity;
import dev.cworldstar.anosf.impl.radiation.ExtendedRadiationInfo.RadiationType;

public interface RadiationEmitter {
	
	abstract ExtendedRadiationInfo getRadiationInfo();
	
	default void registerEmitter(RadiationType type, RadiationSeverity severity, Block emitter, int spread) {
		ExtendedRadiation radiation = ANOSF.radiation();
		radiation.registerEmitter(this, emitter.getLocation(), spread);
	}

	abstract int getStrength();
	
	default void onEmitterRemove(Location location) {
		ANOSF.radiation().removeEmitter(location);
	};
}
