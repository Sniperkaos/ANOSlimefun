package dev.cworldstar.anosf.items.machines.drugworkbench;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.Casing;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

public class DrugWorkbenchCasing extends Casing {
	public DrugWorkbenchCasing() {
		super(
				ItemRegistry.getItemGroup("MACHINE_CATEGORY"), 
				"FUME_SEALANT_BLOCK", 
				Material.DEAD_BRAIN_CORAL_BLOCK, 
				ItemTier.SIMPLE, 
				"<gradient:white:gray:aqua:gray>Fume Sealant Block", 
				"Multiblock Component",
				RecipeType.ENHANCED_CRAFTING_TABLE,
				new ItemStack[] {
						SlimefunItems.PLASTIC_SHEET.asOne(), SlimefunItems.PLASTIC_SHEET.asOne(), SlimefunItems.PLASTIC_SHEET.asOne(),
						SlimefunItems.COMPRESSED_CARBON.asOne(), SlimefunItems.COMPRESSED_CARBON.asOne(), SlimefunItems.COMPRESSED_CARBON.asOne(),
						SlimefunItems.STEEL_INGOT.asOne(), SlimefunItems.STEEL_INGOT.asOne(), SlimefunItems.STEEL_INGOT.asOne()
				}
		);
	}
}
