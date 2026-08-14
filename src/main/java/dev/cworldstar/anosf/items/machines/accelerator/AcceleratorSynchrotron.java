package dev.cworldstar.anosf.items.machines.accelerator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.ANOSFItem;
import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;

public class AcceleratorSynchrotron extends ANOSFItem implements WitherProof {

	private static final ItemStack ACCELERATOR_SYNCHROTRON = new ItemStackBuilder(Material.WAXED_OXIDIZED_COPPER_BULB)
			.setName("<gradient:gray:yellow:gray:yellow:gray>Accelerator Synchrotron")
			.setLore(new String[] {
					"",
					"<red>This multiblock will explode!<gray> Keep it away from your important builds.",
					"",
					"<gradient:#DB7093:gray><italic>\"Temporary. Will be changed to another multiblock later.",
					"",
					Items.ItemTier.makeItemString(ItemTier.HIGH, "Multiblock Component")
			})
			.get();
	
	public AcceleratorSynchrotron() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), new SlimefunItemStack("ACCELERATOR_SYNCHROTRON", ACCELERATOR_SYNCHROTRON));
	}

	@Override
	public void onAttack(Block block, Wither wither) {
		
	}

}
