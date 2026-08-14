package dev.cworldstar.anosf.impl.radiation;

import lombok.Getter;

public class ExtendedRadiationInfo {
	
	public static enum RadiationSeverity {
		ABSENT(0),
		NEGLIGIBLE(1),
		VERY_SMALL(2),
		SMALL(3),
		NOTICABLE(4),
		LESS_SEVERE(5),
		SEVERE(6),
		DEADLY(7),
		CERTAIN_DEATH(8);

		@Getter
		private int radiationLevel = 1;
		
		RadiationSeverity(int i) {
			this.radiationLevel = i;
		}
	}
	
	public static enum RadiationType {
		NO_RADIATION,
		ALPHA_RADIATION, // blocked by thin sheets of paper
		BETA_RADIATION, // blocked by metal plates
		GAMMA_RADIATION, // blocked by lead
		XRAY, // blocked by lead, tin, bismuth
		NEUTRON_RADIATION, // blocked by water, plastic, boron
		COSMIC_RADIATION // blocked by extreme magnetic fields
	}
	
	@Getter
	private RadiationType type;
	@Getter
	private RadiationSeverity severity;
	
	public ExtendedRadiationInfo(
		RadiationType type, // The type of radiation	
		RadiationSeverity severity
	) {
		this.type = type;
		this.severity = severity;
	}

	private static ExtendedRadiationInfo placeholder = new ExtendedRadiationInfo(RadiationType.NO_RADIATION, RadiationSeverity.ABSENT);
	
	public static ExtendedRadiationInfo empty() {
		return placeholder;
	}
	
}
