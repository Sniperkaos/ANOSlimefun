package dev.cworldstar.anosf.items.explosives;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.impl.ExplosionManager;
import dev.cworldstar.libs.cwlib.impl.RadiationZone.RadiationZoneLevel;
import dev.cworldstar.libs.cwlib.impl.explosions.Explosive;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class TestExplosive extends Explosive {

	public static final SlimefunItemStack TEST_EXPLOSIVE = new SlimefunItemStack("TEST_EXPLOSIVE", 
		new ItemStackBuilder(
				SlimefunUtils.getCustomHead(
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdmNTI4YTAwMDVmZDY2ZTY4NjA5MzVkOWVjNzQzYmNlY2E4NDU5ODI1NjQwMmQ3OTc5ZTVhZDM1NGI4NDFmYSJ9fX0="
				)
		)
		.setName("<gradient:#118F1A:#D4D22A>Test Explosive [Medium]</gradient>")
		.setLore(
			new String[] {
				"",
				ExplosionManager.radius(96),
				ExplosionManager.power(100),
				ExplosionManager.falloutRadius(64),
				ExplosionManager.radioactive(RadiationZoneLevel.SMALL)
			}
		).get()
	);
	
	public TestExplosive() {
		super(Items.EXPLOSIVES_CATEGORY, TEST_EXPLOSIVE, Items.ACCELERATOR_RECIPE_TYPE, null);
	}

	public double getFalloutRadius() {
		return 64;
	};
	
	public boolean isRadioactive() {
		return true;
	};
	
	@Override
	public int getStrength() {
		return 100;
	}

	@Override
	public double getExplosionRadius() {
		return 96;
	}

}
