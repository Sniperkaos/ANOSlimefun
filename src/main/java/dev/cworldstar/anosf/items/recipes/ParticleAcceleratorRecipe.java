package dev.cworldstar.anosf.items.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.Validate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import lombok.Getter;
import lombok.Setter;

public class ParticleAcceleratorRecipe {
	
	@Getter
	private String recipeId;
	@Getter
	private ItemStack inputItem;
	@Getter
	private ItemStack collisionItem;
	@Getter
	private ItemStack outputItem;
	@Getter
	@Setter
	private int requiredWork;
	@Getter
	@Setter
	private int speed;
	
	public static Map<String, ParticleAcceleratorRecipe> recipes = new HashMap<String, ParticleAcceleratorRecipe>();
	
	public ParticleAcceleratorRecipe(ItemStack input, ItemStack output, String recipeId) {
		this.inputItem = input.clone();
		this.outputItem = output.clone();
		this.requiredWork = 64;
		this.speed = 1;
		this.recipeId = recipeId;
		recipes.put(recipeId, this);
	}

	public ParticleAcceleratorRecipe(ItemStack input, ItemStack output, int requiredWork, String recipeid) {
		this(input, output, recipeid);
		setRequiredWork(requiredWork);
	}
	
	public ParticleAcceleratorRecipe(ItemStack input, ItemStack output, ItemStack collision, int requiredWork, String recipeid) {
		this(input, output, requiredWork, recipeid);
		collisionItem = collision;
	}

	public static void registerAcceleratorRecipe(ItemStack[] recipe, ItemStack output, int work, String recipeId) {
		ItemStack inputItem = recipe[0];
		ItemStack collisionItem = recipe[1];
		Validate.notNull(inputItem, "Input item cannot be null!");
		if(collisionItem != null) {
			new ParticleAcceleratorRecipe(inputItem, output, collisionItem, work, recipeId);
		} else {
			new ParticleAcceleratorRecipe(inputItem, output, work, recipeId);
		}
	}
	
	public static @Nullable ParticleAcceleratorRecipe lookup(@NotNull String recipeId) {
		return recipes.get(recipeId);
	}
	
	public static @NotNull ArrayList<ParticleAcceleratorRecipe> lookupOutput(@NotNull ItemStack output) {
		ArrayList<ParticleAcceleratorRecipe> list = new ArrayList<ParticleAcceleratorRecipe>();
		for(Entry<String, ParticleAcceleratorRecipe> recipe : recipes.entrySet()) {
			ParticleAcceleratorRecipe realRecipe = recipe.getValue();
			if(SlimefunUtils.isItemSimilar(realRecipe.getOutputItem(), output, false)) {
				list.add(realRecipe);
			}
		}
		return list;
	}
	
	public static @Nullable ParticleAcceleratorRecipe lookup(@NotNull ItemStack inputItem, @Nullable ItemStack collisionInputItem) {
		for(Entry<String, ParticleAcceleratorRecipe> recipe : recipes.entrySet()) {
			ParticleAcceleratorRecipe realRecipe = recipe.getValue();
			boolean hasCollisionItem = realRecipe.getCollisionItem() != null;
			if(hasCollisionItem && collisionInputItem == null) continue;
			SlimefunItem maybeSFItem = SlimefunItem.getByItem(inputItem);
			SlimefunItem maybeSFInputItem = SlimefunItem.getByItem(realRecipe.getInputItem());
			if(maybeSFItem != null && maybeSFInputItem != null) {
				if(maybeSFItem.getId().equals(maybeSFInputItem.getId())) {
					if(collisionInputItem != null) {
						SlimefunItem maybeCollisionItem = SlimefunItem.getByItem(realRecipe.getCollisionItem());
						SlimefunItem maybeCollisionInputItem = SlimefunItem.getByItem(collisionInputItem);
						if(maybeCollisionItem != null && maybeCollisionInputItem != null) {
							if(maybeCollisionItem.getId().equals(maybeCollisionInputItem.getId())) {
								return realRecipe;
							} else {
								continue;
							}
						} else {
							continue;
						}
					} else {
						return realRecipe;
					}
				}
			}
			
			if(recipe.getValue().getInputItem().isSimilar(inputItem)) {
				return realRecipe;
			};
		};
		return null;
	}
	
}
