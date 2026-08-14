package dev.cworldstar.anosf.items.machines.molecularcrafter;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import dev.cworldstar.anosf.items.ANOSFItem;
import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.multiblocks.MolecularCrafterMultiblock;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.utils.BlockStorageHelper;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class MolecularWorkbenchController extends ANOSFItem implements WitherProof {
	private static final ItemStack MOLECULAR_WORKBENCH_CONTROLER = new ItemStackBuilder(Material.AMETHYST_BLOCK)
			.setName("<#DB7093>Molecular Workbench Controller")
			.setLore(new String[] {
					"<yellow>Right-click</yellow><gray> to open the Molecular Workbench UI.",
					"",
					Items.ItemTier.makeItemString(ItemTier.HIGH, "Multiblock Component")
			})
			.get();
	
	public @Nullable BlockMenu findMolecularWorkbench(Block controller) {
		for(int x=-3; x<=3; x++) {
			for(int y=-3; y<=3; y++) {
				for(int z=-3; z<=3; z++) {
					Location blockLocation = controller.getLocation().add(new Vector(x,y,z));
					SlimefunItem item = BlockStorage.check(blockLocation.getBlock());
					if(item != null && item instanceof MolecularCrafterMultiblock) {
						return BlockStorage.getInventory(blockLocation.getBlock());
					}
				}
			}
		}
		return null;
	}
	
	public MolecularWorkbenchController() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), new SlimefunItemStack("MOLECULAR_WORKBENCH_CONTROLLER", MOLECULAR_WORKBENCH_CONTROLER));
		addItemHandler(new BlockUseHandler() {
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				Optional<Block> isClickedBlock = e.getClickedBlock();
				if(!isClickedBlock.isPresent()) {
					return;
				}
				Block clickedBlock = isClickedBlock.get();
				BlockMenu menu = findMolecularWorkbench(clickedBlock);
				if(menu == null) {
					e.getPlayer().sendMessage(FormatUtils.mm("<red>Could not find connected molecular workbench core nearby. Is it within 2 blocks?"));
					return;
				}
				if(menu != null) {
					Block b = menu.getBlock();
					if(BlockStorageHelper.getBoolean(b, "assembled")) {
						menu.open(e.getPlayer());
					} else {
						e.getPlayer().sendMessage(FormatUtils.mm("<red>The multiblock is not assembled."));
					}
				}
			}
		});
	}

	@Override
	public void onAttack(Block block, Wither wither) {
		
	}
}
