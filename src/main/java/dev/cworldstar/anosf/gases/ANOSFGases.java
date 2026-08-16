package dev.cworldstar.anosf.gases;

import dev.cworldstar.libs.cwlib.impl.SlimefunGas;
import dev.cworldstar.libs.cwlib.impl.SlimefunGas.GasProperties;
import dev.cworldstar.libs.cwlib.impl.breathing.Breathing;
import dev.cworldstar.libs.cwlib.impl.breathing.Lungs;

public class ANOSFGases {
	public static final SlimefunGas STEAM = new SlimefunGas("anosf.steam", new GasProperties()
			.temperature(212F)
			.pressure(215F)
			.entityAtmosphereBreathe((entity, action, environment) -> {
				Lungs lungs = action.getLungs();
				float filtration = Breathing.isFiltered(entity, ANOSFGases.STEAM);
				if(lungs.containsAtLeast(15, ANOSFGases.STEAM) && !(filtration >= 1.0F)) {
					entity.setFireTicks(Math.round(60 * (1-filtration)));
				}
			})
	);
}
