package dev.cworldstar.anosf.items.materials;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;

public class HBParticle extends Particle {
	public HBParticle() {
		super("HBPARTICLE", Material.GLASS_BOTTLE, ItemTier.HIGH, "<gradient:red:gold:white>Higgs-Boson Particle", "Material", new String[] {
				"",
				"<gradient:red:gold:white><italic>\"Who the hell is this \"Higgs Boson\" guy anyway?\"</gradient> ",
				"<gray> -changelater, 20XX",
			}, Items.ACCELERATOR_RECIPE_TYPE, new ItemStack[] {
				ItemRegistry.getRegistryItem("ANOIUM_ALLOY_INGOT").getItem()
		});
	}

}
