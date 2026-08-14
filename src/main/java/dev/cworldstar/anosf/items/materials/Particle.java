package dev.cworldstar.anosf.items.materials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.builders.PlayerHeadBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;

public class Particle extends SlimefunItem {
	
	private static ItemStack makeItem(Material material, ItemTier tier, String prefix, String type, String[] lore, boolean glowing) {
		List<String> loreList = new ArrayList<String>();
		loreList.addAll(Arrays.asList(lore));
		loreList.add("");
		loreList.add(tier.makeItemString(type));
		return new ItemStackBuilder(material)
				.name(prefix)
				.stackSize(64)
				.lore(loreList.stream().toArray(String[]::new))
				.condition(glowing, builder -> builder.glowing())
				.get();
	}
	
	private static ItemStack makeItem(String skin, ItemTier tier, String prefix, String type, String[] lore, boolean glowing) {
		List<String> loreList = new ArrayList<String>();
		loreList.addAll(Arrays.asList(lore));
		loreList.add("");
		loreList.add(tier.makeItemString(type));
		return new PlayerHeadBuilder()
				.texture(skin)
				.name(prefix)
				.stackSize(64)
				.lore(loreList.stream().toArray(String[]::new))
				.condition(glowing, builder -> builder.glowing())
				.get();
	}
	
	/**
	 * @param id The ID of the SF item
	 * @param material The Material of this item
	 * @param tier The ItemTier of this item
	 * @param prefix The item's name
	 * @param type The string type
	 * @param lore The lore of the item
	 * @param recipeType RecipeType
	 * @param recipe the recipe
	 */
	public Particle(String id, Material material, ItemTier tier, String prefix, String type, String[] lore, RecipeType recipeType, ItemStack[] recipe) {
		super(ItemRegistry.getItemGroup("MATERIAL_CATEGORY"), new SlimefunItemStack(id, makeItem(material, tier, prefix, type, lore, false)), recipeType, recipe);
		ItemRegistry.registerItem(this);
	}

	public Particle(String id, Material ingotMaterial, ItemTier tier, String name, String string, String[] lore) {
		this(id,ingotMaterial,tier,name,string,lore,RecipeType.NULL,null);
	}

	public Particle(String id, Material material, boolean glowing, ItemTier tier, String prefix, String type, String[] lore, RecipeType recipeType, ItemStack[] recipe) {
		super(ItemRegistry.getItemGroup("MATERIAL_CATEGORY"), new SlimefunItemStack(id, makeItem(material, tier, prefix, type, lore, glowing)), recipeType, recipe);
		ItemRegistry.registerItem(this);
	}

	public Particle(String id, Material playerHead, String skin, ItemTier tier, String prefix,
			String type, String[] lore, RecipeType recipeType, ItemStack[] recipe) {
		super(ItemRegistry.getItemGroup("MATERIAL_CATEGORY"), new SlimefunItemStack(id, makeItem(skin, tier, prefix, type, lore, false)), recipeType, recipe);
		Validate.isTrue(playerHead.equals(Material.PLAYER_HEAD), "This method must use player head.");
		ItemRegistry.registerItem(this);
	}

}