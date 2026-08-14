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
import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;

public class AcceleratorMagnet extends SlimefunItem implements WitherProof {
	
	private static final ItemStack ACCELERATOR_MAGNET_STACK = new ItemStackBuilder(Material.BROWN_GLAZED_TERRACOTTA)
			.setName("<#D2B48C>Accelerator Magnet")
			.setLore(new String[] {
					"",
					"<gradient:#D2B48C:gray><italic>\"Lazy-ass particles... if only we could",
					"<gradient:#D2B48C:gray><italic>force them to move??\"</gradient> <gray>- changelater, 20XX",
					"",
					Items.ItemTier.makeItemString(ItemTier.HIGH, "Multiblock Component")
			})
			.get();
	
	public AcceleratorMagnet() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), new SlimefunItemStack("ACCELERATOR_MAGNET", ACCELERATOR_MAGNET_STACK));
		ItemRegistry.registerItem(this);
	}

	@Override
	public void onAttack(Block block, Wither wither) {
		
	}
}
