package dev.cworldstar.anosf.items.recipes;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

public class GeneratorRecipe {
	@Getter
	@Setter
	private String recipeID = "unset";
	@Getter
	@Setter
	private int powerPerTick = 0;
	@Getter
	@Setter
	private int lastingTicks = 0;
	@Getter
	private ItemStack output;
	@Getter
	@Setter
	private ItemStack itemRequired;
	
	public GeneratorRecipe(
			String recipeId,
			ItemStack recipeItem,
			int ticks,
			int powerPerTick
	) {
		setRecipeID(recipeId);
		setLastingTicks(ticks);
		setItemRequired(recipeItem);
		setPowerPerTick(powerPerTick);
	}
	
	public GeneratorRecipe(
			String recipeId,
			ItemStack recipeItem,
			int ticks,
			int powerPerTick,
			@Nullable ItemStack output
	) {
		this(recipeId, recipeItem, ticks, powerPerTick);
		this.output = output;
	}
	
	public boolean hasOutput() {
		return this.output != null;
	}
	
	public boolean matches(ItemStack matchItem) {
		return itemRequired.isSimilar(matchItem);
	}
}
