package dev.cworldstar.anosf.items.machines.cosmicforge;

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
import dev.cworldstar.anosf.items.machines.multiblocks.CosmicForgeMultiblock;
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

public class CosmicForgeInterface extends ANOSFItem implements WitherProof {

	private static final ItemStack COSMIC_FORGE_INTERFACE = new ItemStackBuilder(Material.SEA_LANTERN)
			.name(ItemTier.COSMIC.makeName("Forge Interface"))
			.lore(new String[] {
					"<yellow>Right-click</yellow><gray> to open the Cosmic Forge UI.",
					"",
					Items.ItemTier.makeItemString(ItemTier.COSMIC, "Multiblock Component")
			})
			.get();
	
	public @Nullable BlockMenu findCosmicForge(Block controller) {
		for(int x=-2; x<=2; x++) {
			for(int y=-2; y<=2; y++) {
				for(int z=-2; z<=2; z++) {
					Location blockLocation = controller.getLocation().add(new Vector(x,y,z));
					SlimefunItem item = BlockStorage.check(blockLocation.getBlock());
					if(item != null && item instanceof CosmicForgeMultiblock) {
						return BlockStorage.getInventory(blockLocation.getBlock());
					}
				}
			}
		}
		return null;
	}
	
	public CosmicForgeInterface() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), new SlimefunItemStack("COSMIC_FORGE_INTERFACE", COSMIC_FORGE_INTERFACE));
		addItemHandler(new BlockUseHandler() {
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				Optional<Block> isClickedBlock = e.getClickedBlock();
				if(!isClickedBlock.isPresent()) {
					return;
				}
				Block clickedBlock = isClickedBlock.get();
				BlockMenu menu = findCosmicForge(clickedBlock);
				if(menu == null) {
					e.getPlayer().sendMessage(FormatUtils.mm("<red>Could not find connected cosmic forge core nearby. Is it within a block?"));
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
