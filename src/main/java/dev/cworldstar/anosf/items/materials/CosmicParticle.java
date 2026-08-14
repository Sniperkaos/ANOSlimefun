package dev.cworldstar.anosf.items.materials;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;

public class CosmicParticle extends Particle {

	public CosmicParticle() {
		super("COSMIC_PARTICLE", Material.GLASS_BOTTLE, ItemTier.COSMIC,"<gradient:white:gold:light_purple>Cosmic Particle</gradient>", "Material/Particle", new String[] {
			"",
			"<gradient:white:gold>\"What the hell are we even doing...",
			"<gray> -Unknown, 21XX"
		}, Items.ACCELERATOR_RECIPE_TYPE, new ItemStack[] {
				ItemRegistry.getRegistryItemAsItemStack("FALLEN_SPARK_PARTICLE"),
				ItemRegistry.getRegistryItemAsItemStack("WHITE_PARTICLE"),
		});
	}

}
