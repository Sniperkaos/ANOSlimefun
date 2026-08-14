package dev.cworldstar.anosf.items.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;

public class DrugWorkbenchRecipe {

	private static List<DrugWorkbenchRecipe> registeredRecipes = new ArrayList<DrugWorkbenchRecipe>();
	
	private ArrayList<ItemStack> contents = new ArrayList<ItemStack>();
	@Getter
	private int work;
	@Getter
	private String ID;
	@Getter
	private ItemStack output;
	
	public static @Nullable DrugWorkbenchRecipe tryFindRecipe(ArrayList<ItemStack> arrayList) {
		for(DrugWorkbenchRecipe recipe : registeredRecipes) {
			Bukkit.getLogger().log(Level.INFO, "validating contents of recipe " + recipe.toString());
			if(recipe.validateContents(arrayList)) {
				return recipe;
			}
		}
		return null;
	}
	
	public static @Nullable DrugWorkbenchRecipe tryFindRecipe(String id) {
		for(DrugWorkbenchRecipe recipe : registeredRecipes) {
			if(recipe.getID().equals(id)) {
				return recipe;
			}
		}
		return null;
	}
	
	public DrugWorkbenchRecipe(String id, int workRequired, ItemStack outputItem, ItemStack[] recipe) {
		ID = id;
		work = workRequired;
		output = outputItem;
		for(ItemStack item : recipe) {
			contents.add(item);
		}
		while(contents.size() < 28) {
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
				toReturn = toReturn + item.toString() + ", ";
			}	
		}
		return toReturn;
	}
	
	private boolean validateContents(ArrayList<ItemStack> arrayList) {
		int slot = 0;
		int matches = 0;
		for(ItemStack content : arrayList) {
			if(content == null && this.contents.get(slot) == null) {
				matches += 1;
				continue;
			} else if(content == null || this.contents.get(slot) == null) {
				Bukkit.getLogger().log(Level.INFO, "breaking");
				break;
			}
			if(this.contents.get(slot).isSimilar(content)) {
				Bukkit.getLogger().log(Level.INFO, "matches " + String.valueOf(matches) + ", size " + contents.size());
				matches += 1;
			} else {
				break;
			}
			slot += 1;
		}
		Bukkit.getLogger().log(Level.INFO, "matches " + String.valueOf(matches) + ", size " + contents.size());
		return matches >= this.contents.size();
	}
}
