package dev.cworldstar.anosf.items.materials;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items.ItemTier;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;

public class ParticleMaterial {
	
	@Getter
	private Particle particle;
	@Getter
	private Particle decayed;
	
	
	public ParticleMaterial(
			String id,
			ItemTier tier,
			Material particleMaterial,
			Material decayedMaterial,
			String materialName, 
			RecipeType recipeType,
			ItemStack[] recipe
	) {
		particle = new Particle(id, particleMaterial, tier, materialName, "Particle Material", new String[0], recipeType, recipe);
		decayed = new Particle(id + "_DECAYED", decayedMaterial, tier, "Decayed " + materialName, "Particle Material", new String[0], recipeType, recipe);
	}
}
