package dev.cworldstar.anosf.impl.builders;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SlimefunItemBuilder {
	
	private ItemGroup group;
	private SlimefunItemStack item;
	private RecipeType recipeType;
	private ItemStack[] recipe;
	
	public void group(ItemGroup group) {
		this.group = group;
	}
	
	public void item(SlimefunItemStack item) {
		this.item = item;
	}
	
	public void recipeType(RecipeType type) {
		this.recipeType = type;
	}
	
	public void recipe(ItemStack[] recipe) {
		this.recipe = recipe;
	}
	
	public SlimefunItem build() {
		return new SlimefunItem(group, item, recipeType, recipe);
	}
	
	
}
