package dev.cworldstar.anosf.items.machines.accelerator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.Casing;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

public class AcceleratorCasingGlass extends Casing implements WitherProof {
	public AcceleratorCasingGlass() {
		super(
				ItemRegistry.getItemGroup("MACHINE_CATEGORY"), 
				"ACCELERATOR_CASING_GLASS", 
				Material.BLACK_STAINED_GLASS, 
				ItemTier.HIGH, 
				"<#cfcabc>Accelerator Glass Casing</#cfcabc>", 
				"Multiblock Component",
				RecipeType.ENHANCED_CRAFTING_TABLE,
				new ItemStack[] {
						ItemRegistry.getRegistryItem("ACCELERATOR_CASING").getItem(),ItemRegistry.getRegistryItem("ACCELERATOR_CASING").getItem(),ItemRegistry.getRegistryItem("ACCELERATOR_CASING").getItem(),
						ItemRegistry.getRegistryItem("ACCELERATOR_CASING").getItem(),SlimefunItems.HARDENED_GLASS.asOne(), ItemRegistry.getRegistryItem("ACCELERATOR_CASING").getItem(),
						ItemRegistry.getRegistryItem("ACCELERATOR_CASING").getItem(),ItemRegistry.getRegistryItem("ACCELERATOR_CASING").getItem(),ItemRegistry.getRegistryItem("ACCELERATOR_CASING").getItem()
				},
				8
		);
	}

	@Override
	public void onAttack(Block block, Wither wither) {
		
	}
}
