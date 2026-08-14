package dev.cworldstar.anosf.items;

import org.bukkit.inventory.ItemStack;

import dev.cworldstar.libs.cwlib.ItemRegistry;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class ANOSFItem extends SlimefunItem {

	public ANOSFItem(ItemGroup itemGroup, SlimefunItemStack item) {
		super(itemGroup, item);
		ItemRegistry.registerItem(this);
	}

	public ANOSFItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType,
			ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe);
		ItemRegistry.registerItem(this);
	}

}
