package dev.cworldstar.anosf.impl.drugs;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public abstract class AbstractDrug extends SlimefunItem {
	
	public AbstractDrug(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup,new SlimefunItemStack(id, item), recipeType, recipe);
	}

}
