package dev.cworldstar.anosf.items.machines;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;

public class Casing extends SlimefunItem implements WitherProof {
	
	private static ItemStack makeItem(Material material, ItemTier tier, String prefix, String type) {
		return new ItemStackBuilder(material)
				.setName(prefix)
				.setLore(new String[] {
						"<gray>Used in crafting and in Multiblocks.</gray>",
						"",
						"<gradient:#cfcabc:gray><italic>\"It's like.. walls.. or whatever...\"",
						"<gradient:#cfcabc:gray><italic> -changelater, 20XX",
						"",
						tier.makeItemString(type)
				})
				.get();
	}
	
	public Casing(ItemGroup itemGroup, String id, Material material, ItemTier tier, String prefix, String type) {
		super(itemGroup, new SlimefunItemStack(id, makeItem(material, tier, prefix, type)));
		ItemRegistry.registerItem(this);
	}

	public Casing(ItemGroup group, String id, Material material, ItemTier tier, String prefix,
			String type, RecipeType recipeType, ItemStack[] recipe) {
		super(group, new SlimefunItemStack(id, makeItem(material, tier, prefix, type)), recipeType, recipe);
		ItemRegistry.registerItem(this);
	}
	
	public Casing(ItemGroup group, String id, Material material, ItemTier tier, String prefix,
			String type, RecipeType recipeType, ItemStack[] recipe, int output) {
		super(group, new SlimefunItemStack(id, makeItem(material, tier, prefix, type)), recipeType, recipe, new SlimefunItemStack(id, makeItem(material, tier, prefix, type)).asQuantity(output));
		ItemRegistry.registerItem(this);
	}

	@Override
	public void onAttack(Block block, Wither wither) {
		
	}

}
