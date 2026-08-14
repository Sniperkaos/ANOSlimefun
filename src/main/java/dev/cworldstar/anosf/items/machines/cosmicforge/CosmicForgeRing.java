package dev.cworldstar.anosf.items.machines.cosmicforge;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.Casing;
import dev.cworldstar.anosf.items.recipes.MolecularWorkbenchRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class CosmicForgeRing extends Casing {

	public CosmicForgeRing() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), "COSMIC_FORGE_RING", Material.BLACK_GLAZED_TERRACOTTA, ItemTier.COSMIC, ItemTier.COSMIC.makeName("Forge Ring"), "Multiblock Component", RecipeType.NULL, null);
		ItemStack SAP = ItemRegistry.getRegistryItemAsItemStack("STRANGE_ALLOY_PLATE");
		ItemStack SMC = ItemRegistry.getRegistryItemAsItemStack("MACHINE_CORE_STRANGE");
		ItemStack SAR = ItemRegistry.getRegistryItemAsItemStack("STRANGE_ALLOY_ROD");
		ItemStack CMP = ItemRegistry.getRegistryItemAsItemStack("COSMIC_PARTICLE");
		
		setRecipeType(Items.MOLECULAR_CRAFTER_RECIPE_TYPE);
		
		MolecularWorkbenchRecipe recipe = new MolecularWorkbenchRecipe(
				"COSMIC_FORGE_RING_RECIPE",
				200,
				this.getItem().asQuantity(8),
				new ItemStack[] {
						null,null,null,null,null,null,null,
						SAP,SAP,SAP,SAP,SAP,SAP,SAP,
						SAR,SAR,SMC,CMP,SMC,SAR,SAR,
						SAP,SAP,SAP,SAP,SAP,SAP,SAP,
						null,null,null,null,null,null,null,

				}
		);
		recipe.setDeviationChance(50); // 50% chance per work tick to deviate
	}

}
