package dev.cworldstar.anosf.items.recipes;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

public class DustExtractorRecipe {
	
	private static List<DustExtractorRecipe> registeredRecipes = new ArrayList<DustExtractorRecipe>();

	public static @Nullable DustExtractorRecipe findRecipe(@Nonnull final String id) {
		for(DustExtractorRecipe recipe : registeredRecipes) {
			if(recipe.getRecipeID().equalsIgnoreCase(id)) {
				return recipe;
			}
		}
		return null;
	}
	
	public static @Nullable DustExtractorRecipe findRecipe(@Nonnull final ItemStack input) {
		for(DustExtractorRecipe recipe : registeredRecipes) {
			if(recipe.getInputItem().isSimilar(input)) {
				return recipe;
			}
		}
		return null;
	}
	
	@Getter
	@Setter
	private String recipeID = "unset";
	@Getter
	@Setter
	private int powerPerTick = 0;
	@Getter
	@Setter
	private int requiredWork = 0;
	@Getter
	@Setter
	private ItemStack[] outputItems;
	@Getter
	@Setter
	private ItemStack inputItem;
	
	public DustExtractorRecipe(
			String recipeId,
			ItemStack recipeItem,
			ItemStack outputItem,
			int work,
			int powerPerTick
	) {
		this(recipeId, recipeItem, new ItemStack[] {outputItem}, work, powerPerTick);
	}
	
	public DustExtractorRecipe(
			String recipeId,
			ItemStack recipeItem,
			ItemStack[] outputItems,
			int work,
			int powerPerTick
	) {
		setRecipeID(recipeId);
		setRequiredWork(work);
		setInputItem(recipeItem);
		setOutputItems(outputItems);
		setPowerPerTick(powerPerTick);
		registeredRecipes.add(this);
	}

	public boolean matches(@Nonnull final ItemStack matchItem) {
		return inputItem.isSimilar(matchItem);
	}

	public int getRequiredWork(int processingSpeed) {
		return (int) Math.floor((double) this.requiredWork / processingSpeed);
	}
	
}
