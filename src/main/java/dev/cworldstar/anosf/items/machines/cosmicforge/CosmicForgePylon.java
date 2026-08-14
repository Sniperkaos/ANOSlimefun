package dev.cworldstar.anosf.items.machines.cosmicforge;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.Casing;
import dev.cworldstar.anosf.items.recipes.MolecularWorkbenchRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class CosmicForgePylon extends Casing {

	public CosmicForgePylon() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), "COSMIC_FORGE_PYLON", Material.YELLOW_GLAZED_TERRACOTTA, ItemTier.COSMIC, "Forge Pylon", "Multiblock Component", RecipeType.NULL, null);
		ItemStack ANOIUM_PLATE = ItemRegistry.getRegistryItemAsItemStack("ANOIUM_ALLOY_PLATE");
		ItemStack STRANGE_MACHINE_CORE = ItemRegistry.getRegistryItemAsItemStack("MACHINE_CORE_STRANGE");
		ItemStack STRANGE_ALLOY_ROD = ItemRegistry.getRegistryItemAsItemStack("STRANGE_ALLOY_ROD");
		ItemStack COSMIC_PARTICLE = ItemRegistry.getRegistryItemAsItemStack("COSMIC_PARTICLE");
		
		setRecipeType(Items.MOLECULAR_CRAFTER_RECIPE_TYPE);
		
		MolecularWorkbenchRecipe recipe = new MolecularWorkbenchRecipe(
				"COSMIC_FORGE_PYLON_RECIPE",
				200,
				this.getItem().asQuantity(8),
				new ItemStack[] {
						null,ANOIUM_PLATE, ANOIUM_PLATE, ANOIUM_PLATE, ANOIUM_PLATE, ANOIUM_PLATE, null,
						null,ANOIUM_PLATE, STRANGE_MACHINE_CORE, STRANGE_ALLOY_ROD, STRANGE_MACHINE_CORE, ANOIUM_PLATE, null,
						null,ANOIUM_PLATE, STRANGE_MACHINE_CORE, COSMIC_PARTICLE, STRANGE_MACHINE_CORE, ANOIUM_PLATE, null,
						null,ANOIUM_PLATE, STRANGE_MACHINE_CORE, STRANGE_ALLOY_ROD, STRANGE_MACHINE_CORE, ANOIUM_PLATE, null,
						null,ANOIUM_PLATE, ANOIUM_PLATE, ANOIUM_PLATE, ANOIUM_PLATE, ANOIUM_PLATE, null,
				}
				);
		
		recipe.setDeviationChance(50); // 50% chance per work tick to deviate
	}

}
