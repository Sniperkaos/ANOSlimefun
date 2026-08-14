package dev.cworldstar.anosf.items.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNullableByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.items.ANOSFItem;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import lombok.Getter;
import lombok.Setter;

public class MolecularWorkbenchRecipe {
	
	private static List<MolecularWorkbenchRecipe> registeredRecipes = new ArrayList<MolecularWorkbenchRecipe>();
	
	public static final ItemStack DEVIATED_ITEM = new ItemStackBuilder(Material.GUNPOWDER).glowing().name("<gray:white>Deviated Material").lore(
		new String[] {
			"",
			"<light_purple>How did this happen?",
			"",
			ItemTier.ELITE.makeItemString("Junk")
		}
	).item();
	
	public static void registerDeviatedItem() {
		new ANOSFItem(ItemRegistry.getItemGroup("MATERIAL_CATEGORY"), new SlimefunItemStack("DEVIATED_ITEM", DEVIATED_ITEM), new RecipeType(ANOSF.key("BYPRODUCT"), DEVIATED_ITEM), new ItemStack[0]);
	}
	
	private ArrayList<ItemStack> contents = new ArrayList<ItemStack>();
	@Getter
	private int work;
	@Getter
	private String ID;
	@Getter
	private ItemStack output;
	@Getter
	@Setter
	private boolean usable = true;
	@Getter
	@Setter
	private int deviationThreshhold = 100;
	@Getter
	@Setter
	private int deviationChance = 15;
	
	
	public static @Nullable MolecularWorkbenchRecipe tryFindRecipe(ArrayList<ItemStack> items) {
		for(MolecularWorkbenchRecipe recipe : registeredRecipes) {
			if(recipe.validateContents(items) && recipe.isUsable()) {
				return recipe;
			}
		}
		return null;
	}
	
	public static @Nullable MolecularWorkbenchRecipe tryFindRecipe(String id) {
		for(MolecularWorkbenchRecipe recipe : registeredRecipes) {
			if(recipe.getID().equals(id)) {
				return recipe;
			}
		}
		return null;
	}
	
	public static void registerRecipe(@Nullable ItemStack[] recipe, @Nonnull ItemStack output) {
		if(recipe == null || SlimefunItem.getByItem(output) == null) {
			Bukkit.getLogger().log(Level.SEVERE, "There was an error parsing a recipe! Output item: ", output.toString());
			return;
		}
		registeredRecipes.add(
			new MolecularWorkbenchRecipe(
					SlimefunItem.getByItem(output).getId(),
					1080,
					output,
					recipe
			)
		);
	}
	
	public MolecularWorkbenchRecipe(String id, int workRequired, ItemStack outputItem, ItemStack[] recipe) {	
		ID = id;
		work = workRequired;
		output = outputItem;
	
		for(ItemStack item : recipe) {
			contents.add(item);
		}
		while(contents.size() < 35) {
			contents.add(null);
		}
		registeredRecipes.add(this);
	}
	
	public String toString() {
		return serializeArrayList(getID(), contents);
	}
	
	public String serializeArrayList(String prefix, ArrayList<ItemStack> list) {
		String toReturn = prefix + ": ";
		for(ItemStack item : list) {
			if(item == null) {
				toReturn = toReturn + "null, ";
			} else {
				toReturn = toReturn + SlimefunItem.getByItem(item).getId() + ", ";
			}	
		}
		return toReturn;
	}
	
	@ParametersAreNullableByDefault
	private static boolean validateItem(ItemStack item1, ItemStack item2) {
		SlimefunItem item1SF = SlimefunItem.getByItem(item1);
		SlimefunItem item2SF = SlimefunItem.getByItem(item2);
		
		if(item1SF != null && item2SF != null) {
			return item1SF.getId().equals(item2SF.getId());
		} else if(item1SF == null && item2SF == null) {
			return item1.getType().equals(item2.getType());
		} else if(item1 == null && item2 == null) {
			return true;
		}
		return false;
	}
	
	private boolean validateContents(ArrayList<ItemStack> arrayList) {
		int slot = 0;
		int matches = 0;
		for(ItemStack content : arrayList) {
			if(content == null && this.contents.get(slot) == null) {
				matches += 1;
				continue;
			} else if(content == null || this.contents.get(slot) == null) {
				continue;
			}
			if(validateItem(this.contents.get(slot), content)) {
				matches += 1;
			} else {
				continue;
			}
			slot += 1;
		}
		return matches >= this.contents.size();
	}

	public static @Nullable MolecularWorkbenchRecipe lookupOutput(ItemStack item) {
		for(MolecularWorkbenchRecipe recipe : registeredRecipes) {
			if(validateItem(item, recipe.getOutput())) {
				return recipe;
			}
		}
		return null;
	}

	public ArrayList<ItemStack> contents() {
		return contents;
	}

	private static final MolecularWorkbenchRecipe EMPTY_RECIPE = new MolecularWorkbenchRecipe("EMPTY_RECIPE", 1, new ItemStackBuilder(Material.BARRIER).name("<bold><red>Error").lore(new String[] {"<red>An error occured getting the recipe", "<red>for this item."}).build(), new ItemStack[0]);
	
	public static MolecularWorkbenchRecipe emptyRecipe() {
		EMPTY_RECIPE.setUsable(false);
		return EMPTY_RECIPE;
	}
}
