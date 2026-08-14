package dev.cworldstar.anosf.items.machines.molecularcrafter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.Casing;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class MolecularWorkbenchCasing extends Casing {
	public MolecularWorkbenchCasing() {
		super(
				ItemRegistry.getItemGroup("MACHINE_CATEGORY"), 
				"MOLECULAR_WORKBENCH_CASING", 
				Material.IRON_BLOCK, 
				ItemTier.ELITE, 
				"<gradient:gray:gold:gold:gray>Molecular Workbench Casing", 
				"Multiblock Component",
				RecipeType.ENHANCED_CRAFTING_TABLE,
				new ItemStack[] {
						ItemRegistry.getRegistryItem("BLACKENED_CHROME_ALLOY_ROD").getItem(), ItemRegistry.getRegistryItem("BLACKENED_CHROME_ALLOY_PLATE").getItem(), ItemRegistry.getRegistryItem("BLACKENED_CHROME_ALLOY_ROD").getItem(),
						ItemRegistry.getRegistryItem("BLACKENED_CHROME_ALLOY_PLATE").getItem(), ItemRegistry.getRegistryItem("MACHINE_CORE_HIGH").getItem(), ItemRegistry.getRegistryItem("BLACKENED_CHROME_ALLOY_PLATE").getItem(),
						ItemRegistry.getRegistryItem("BLACKENED_CHROME_ALLOY_ROD").getItem(), ItemRegistry.getRegistryItem("BLACKENED_CHROME_ALLOY_PLATE").getItem(), ItemRegistry.getRegistryItem("BLACKENED_CHROME_ALLOY_ROD").getItem()
				},
				8
		);
	}
}
