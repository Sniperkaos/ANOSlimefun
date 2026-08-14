package dev.cworldstar.anosf.items.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.libs.cwlib.utils.SlimefunItemEntry;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * The Alloy Forge will dynamically create slots based on the size of the 
 * {{@link #recipeInputs} list. As such, even though it is possible, there should
 * NEVER be a null value passed to recipeInputs.
 * @author cworldstar
 *
 */
public class AlloyForgeRecipe {

	private static final Map<String, AlloyForgeRecipe> ALLOY_FORGE_RECIPES = new HashMap<String, AlloyForgeRecipe>(); 
	
	@Nonnull
	private final List<SlimefunItemEntry> recipeInputs = new ArrayList<SlimefunItemEntry>();
	@Getter
	private String id = "UNDEFINED";
	@Getter
	private ItemStack output = null;
	@Getter
	private int work;
	
	public AlloyForgeRecipe(String id, int work, ItemStack output, SlimefunItemEntry[] inputs) {
		Validate.notNull(inputs, "You must pass a SlimefunItemEntry[] value.");
		Validate.notNull(output, "An output is required.");
		
		Validate.noNullElements(inputs, "You cannot pass null in the inputs.");
		
		Validate.isTrue(!(ALLOY_FORGE_RECIPES.containsKey(id)), "The given recipe id " + id + " has already been registered.");
		
		recipeInputs.addAll(Arrays.asList(inputs));
		this.output = output;
		this.id = id;
		this.work = work;
		
		ALLOY_FORGE_RECIPES.put(id, this);
	}

	public List<SlimefunItemEntry> inputs() {
		return this.recipeInputs.stream().collect(Collectors.toUnmodifiableList());
	}
	
	public static void makeRecipe(String id, int work, ItemStack ingot, SlimefunItemEntry[] slimefunItemEntries) {
		new AlloyForgeRecipe(id, work, ingot, slimefunItemEntries);
	}
	
	public static @Nullable AlloyForgeRecipe getAlloyForgeRecipe(String id) {
		return ALLOY_FORGE_RECIPES.get(id);
	}
	
	public static @Nullable AlloyForgeRecipe getAlloyForgeRecipe(ItemStack output) {
		for(Entry<String, AlloyForgeRecipe> recipe : ALLOY_FORGE_RECIPES.entrySet()) {
			if(SlimefunUtils.isItemSimilar(recipe.getValue().getOutput(), output, false)) {
				return recipe.getValue();
			}
		}
		return null;
	}

	private @Nullable SlimefunItemEntry inputMatches(ItemStack input) {
		for(SlimefunItemEntry entry : recipeInputs) {
			if(entry.matches(input)) {
				return entry;
			}
		}
		return null;
	}

	public static Map<String, AlloyForgeRecipe> recipes() {
		return ALLOY_FORGE_RECIPES;
	}

	public ItemStack getRecipeDisplay() {
		ItemStack toReturn = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		toReturn.editMeta(meta -> {
			meta.displayName(Component.text(id));
		});
		return toReturn;
	}

}
