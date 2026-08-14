package dev.cworldstar.anosf.items.machines.generators;

import java.util.Arrays;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.DecayGenerator;
import dev.cworldstar.anosf.items.recipes.GeneratorRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;

public class TieredDecayGenerators {
	/*
	 * 			ItemGroup category, 
			Material itemMaterial,
			ItemTier tier,
			String id,
			String itemType,
			String itemName,
			String[] lore,
			RecipeType recipeType, 
			int processingSpeed,
			ItemStack[] recipe,
			int perTickGeneration,
			BiFunction<Location, Config, Boolean> canGenerate,
			List<GeneratorRecipe> recipes
	 */
	public static void register() {
		for(ItemTier tier : Items.ItemTier.iterator()) {
			if(tier.getTier() < ItemTier.HIGH.getTier()) {
				continue;
			}
			ItemRegistry.registerItem(new DecayGenerator(
					ItemRegistry.getItemGroup("MACHINE_CATEGORY"),
					Material.COPPER_BLOCK,
					tier,
					"DECAY_GENERATOR_" + tier.toString(),
					"Generator",
					"<gradient:yellow:gray>Decay Generator</gradient>",
					new String[] {
							
					},
					RecipeType.NULL,
					((tier.getTier() * 4) - 3 + (tier.getTier() * 10)),
					new ItemStack[0],
					25600,
					(Location loc, Config cfg) -> {
						return true;
					},
					Arrays.asList(new GeneratorRecipe[] {
							new GeneratorRecipe("DECAY_HB_PARTICLE", ItemRegistry.getRegistryItem("HBPARTICLE").getItem(), 1080, 1080000),
							new GeneratorRecipe("DECAY_STRANGE_SPARK", ItemRegistry.getRegistryItem("FALLEN_SPARK_PARTICLE").getItem(), 74520, 74520000),
							new GeneratorRecipe("DECAY_WHITE_PARTICLE", ItemRegistry.getRegistryItem("WHITE_PARTICLE").getItem(), 12420, 12420000),
							new GeneratorRecipe("DECAY_COSMIC_PARTICLE", ItemRegistry.getRegistryItem("COSMIC_PARTICLE").getItem(), 1242000, 1242000000)
					})	
			));
		}
	}
}
