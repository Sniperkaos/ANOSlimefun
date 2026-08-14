package dev.cworldstar.anosf.items.machines.accelerator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

public class AcceleratorGlass extends SlimefunItem implements WitherProof {
	private static final ItemStack ACCELERATOR_MAGNET_GLASS_STACK = new ItemStackBuilder(Material.BROWN_STAINED_GLASS)
			.setName("<#D2B48C>Accelerator Magnet Glass")
			.setLore(new String[] {
					"",
					"<gradient:#D2B48C:gray><italic>\"Lazy-ass particles... if only we could",
					"<gradient:#D2B48C:gray><italic>force them to move??\"</gradient> <gray>- changelater, 20XX",
					"",
					Items.ItemTier.makeItemString(ItemTier.HIGH, "Multiblock Component")
			})
			.get();
	
	public AcceleratorGlass() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), new SlimefunItemStack("ACCELERATOR_GLASS", ACCELERATOR_MAGNET_GLASS_STACK), RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
			ItemRegistry.getRegistryItem("ACCELERATOR_MAGNET").getItem(),ItemRegistry.getRegistryItem("ACCELERATOR_MAGNET").getItem(),ItemRegistry.getRegistryItem("ACCELERATOR_MAGNET").getItem(),
			ItemRegistry.getRegistryItem("ACCELERATOR_MAGNET").getItem(),SlimefunItems.HARDENED_GLASS.asOne(), ItemRegistry.getRegistryItem("ACCELERATOR_MAGNET").getItem(),
			ItemRegistry.getRegistryItem("ACCELERATOR_MAGNET").getItem(),ItemRegistry.getRegistryItem("ACCELERATOR_MAGNET").getItem(),ItemRegistry.getRegistryItem("ACCELERATOR_MAGNET").getItem()
		}, new SlimefunItemStack("ACCELERATOR_GLASS", ACCELERATOR_MAGNET_GLASS_STACK).asQuantity(8));
		ItemRegistry.registerItem(this);
	}

	@Override
	public void onAttack(Block block, Wither wither) {
		
	}
}
