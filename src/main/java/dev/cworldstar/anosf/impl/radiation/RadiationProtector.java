package dev.cworldstar.anosf.impl.radiation;

import java.util.Arrays;

import dev.cworldstar.anosf.impl.radiation.ExtendedRadiationInfo.RadiationSeverity;
import dev.cworldstar.anosf.impl.radiation.ExtendedRadiationInfo.RadiationType;

public interface RadiationProtector {
	abstract RadiationType[] getProtections();
	abstract RadiationSeverity getMaxSeverity();
	default boolean protectsFrom(RadiationType type, RadiationSeverity severity) {
		return Arrays.asList(getProtections()).contains(type) && getMaxSeverity().getRadiationLevel() >= severity.getRadiationLevel();
	}
	default boolean protectsFrom(RadiationType type) {
		return Arrays.asList(getProtections()).contains(type);
	}
	abstract int getProtectionValue();
}
