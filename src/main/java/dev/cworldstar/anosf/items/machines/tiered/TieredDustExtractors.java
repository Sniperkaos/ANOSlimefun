package dev.cworldstar.anosf.items.machines.tiered;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.DustExtractor;
import dev.cworldstar.anosf.items.recipes.DustExtractorRecipe;
import dev.cworldstar.anosf.items.tools.Hammer;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class TieredDustExtractors {
	public static void register() {
		for(ItemTier tier : Items.ItemTier.iterator()) {
			if(tier.getTier() < ItemTier.ELITE.getTier()) {
				continue;
			}
			ItemRegistry.registerItem(new DustExtractor(
					ItemRegistry.getItemGroup("MACHINE_CATEGORY"),
					Material.COPPER_BLOCK,
					tier,
					"DUST_EXTRACTOR_" + tier.toString(),
					"Machine",
					"<gradient:yellow:gray>Dust Extractor</gradient>",
					new String[] {
							
					},
					RecipeType.ENHANCED_CRAFTING_TABLE,
					(((tier.getTier() - 2) * 4) - 3 + ((tier.getTier() - 2) * 10)),
					new ItemStack[0],
					Arrays.asList(
						new DustExtractorRecipe[] {
							new DustExtractorRecipe("COBBLESTONE_TO_DUST", new ItemStack(Material.COBBLESTONE), Hammer.getDusts().stream().toArray(ItemStack[]::new), 50, 1200),
							new DustExtractorRecipe("GRAVEL_TO_DUST", new ItemStack(Material.GRAVEL), Hammer.getDusts().stream().toArray(ItemStack[]::new), 30, 1200),
							new DustExtractorRecipe("SAND_TO_DUST", new ItemStack(Material.SAND), Hammer.getDusts().stream().toArray(ItemStack[]::new), 35, 1200)
						}
					)					
			));
		}
	}
}
