package dev.cworldstar.anosf.items.materials.chrome;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;

public class ChromeDust extends SlimefunItem {

	private static final ItemStack CHROME_DUST_STACK = new ItemStackBuilder(Material.GLOWSTONE_DUST)
			.name("<gradient:gray:white>Chromium Dust")
			.lore(new String[] {
				"<gray>Drops from using a <aqua>Sieving Hammer<gray> on stone.",
				"",
				Items.ItemTier.makeItemString(ItemTier.SIMPLE, "Material")
			})
			.glowing()
			.get();
	
	public ChromeDust() {
		super(ItemRegistry.getItemGroup("MATERIAL_CATEGORY"), new SlimefunItemStack("CHROME_DUST", CHROME_DUST_STACK));
		ItemRegistry.registerItem(this);
	}

}
