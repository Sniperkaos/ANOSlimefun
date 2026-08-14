package dev.cworldstar.anosf.items.materials.chrome;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

public class ChromeIngot extends SlimefunItem {

	private static final ItemStack CHROME_INGOT_STACK = new ItemStackBuilder(Material.IRON_INGOT)
			.name("<gradient:gray:white>Chromium Ingot")
			.lore(new String[] {
				"",
				"<gradient:gray:white><italic>\"I was once told, \"Be like iron, son- strong, impenetrable, unyielding.\"",
				"<gradient:gray:white><italic>but even the thickest iron plate corrodes with enough thermite.\"</gradient>",
				"<gray>- Unknown, 19XX",
				"",
				Items.ItemTier.makeItemString(ItemTier.BASIC, "Material")

			})
			.glowing()
			.get();
	
	public ChromeIngot() {
		super(ItemRegistry.getItemGroup("MATERIAL_CATEGORY"), new SlimefunItemStack("CHROME_INGOT", CHROME_INGOT_STACK),
				RecipeType.SMELTERY, 
				new ItemStack[] {
						ItemRegistry.getRegistryItem("CHROME_DUST").getItem(),
						SlimefunItems.ALUMINUM_INGOT.asQuantity(8),
						SlimefunItems.PLASTIC_SHEET.asQuantity(8),
						SlimefunItems.BLISTERING_INGOT_2.asQuantity(4)
						
		});
		ItemRegistry.registerItem(this);
	}

}
