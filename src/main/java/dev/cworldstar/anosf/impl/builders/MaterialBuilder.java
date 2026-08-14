package dev.cworldstar.anosf.impl.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.materials.Particle;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

/**
 * Similar to {@link IngotMaterial}, but in builder form.
 * @author cworldstar
 */
public class MaterialBuilder {
	
	private static enum RECIPE_GRIDS {
		ROD(new String[] {"e", "x", "e", "e", "x", "e", "e", "x", "e"}),
		PLATE(new String[] {"e", "e", "e", "x", "x", "x", "x", "x", "x"}),
		GEAR(new String[] {"e", "x", "e", "x", "e", "x", "e", "x", "e"});
		
		@Getter
		private String[] grid;
		
		public ItemStack[] getRecipe(ItemStack material) {
			return List.of(grid).stream().map(str -> (str != "e") ? material : null).collect(Collectors.toList()).toArray(new ItemStack[0]);
		}
		
		RECIPE_GRIDS(String[] recipeGrid) {
			this.grid = recipeGrid;
		}
	}
	
	private SlimefunItem ingot;
	private List<SlimefunItem> items = new ArrayList<SlimefunItem>();
	private String materialId;
	private String[] lore;
	private String materialName;
	private ItemTier materialTier;
	
	public MaterialBuilder(Material ingotMaterial, String[] lore, String id, String name, ItemTier tier) {
		materialId = id;
		materialName = name;
		materialTier = tier;
		this.lore = lore;
		ingot = new Particle(id, ingotMaterial, tier, name, "Material", lore);
	}
	
	public void rod(Material m, @Nullable RecipeType type, @Nullable ItemStack[] recipe) {
		items.add(new Particle(materialId+"_ROD", m, materialTier, materialName + " Rod", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, RECIPE_GRIDS.ROD.getRecipe(ingot.getItem())));
	}
	
	public void plate(Material m, @Nullable RecipeType type, @Nullable ItemStack[] recipe) {
		items.add(new Particle(materialId+"_PLATE", m, materialTier, materialName+" Plate", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, RECIPE_GRIDS.PLATE.getRecipe(ingot.getItem())));
	}
	
}
