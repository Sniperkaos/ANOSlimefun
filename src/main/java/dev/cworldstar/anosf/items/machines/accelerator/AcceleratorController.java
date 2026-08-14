package dev.cworldstar.anosf.items.machines.accelerator;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.ANOSFItem;
import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class AcceleratorController extends ANOSFItem implements WitherProof {

	private static final ItemStack ACCELERATOR_CONTROLLER = new ItemStackBuilder(Material.AMETHYST_BLOCK)
			.setName("<#DB7093>Accelerator Controller")
			.setLore(new String[] {
					"<yellow>Right-click</yellow><gray> to open the Accelerator's UI.",
					"",
					"<gradient:#DB7093:gray><italic>\"If the core is the heart...",
					"<gradient:#DB7093:gray><italic>then this is the brain.\"</gradient> <gray>- changelater, 20XX",
					"",
					Items.ItemTier.makeItemString(ItemTier.HIGH, "Multiblock Component")
			})
			.get();
	
	public AcceleratorController() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), new SlimefunItemStack("ACCELERATOR_CONTROLLER", ACCELERATOR_CONTROLLER));
		addItemHandler(new BlockUseHandler() {
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				Optional<Block> isClickedBlock = e.getClickedBlock();
				if(!isClickedBlock.isPresent()) {
					return;
				}
				Block clickedBlock = isClickedBlock.get();
				Block acceleratorMain = clickedBlock.getWorld().getBlockAt(clickedBlock.getLocation().add(19, 0, 0));
				BlockMenu menu = BlockStorage.getInventory(acceleratorMain);
				if(menu == null) {
					e.getPlayer().sendMessage(FormatUtils.mm("<red>Could not find connected accelerator core at " + acceleratorMain.getLocation().toBlockLocation().toVector().toString() + ". Material: " + acceleratorMain.getType().toString()));
				}
				menu.open(e.getPlayer());
			}
		});
	}

	@Override
	public void onAttack(Block block, Wither wither) {
		
	}

}
