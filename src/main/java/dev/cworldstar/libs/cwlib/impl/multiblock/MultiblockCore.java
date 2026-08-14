package dev.cworldstar.libs.cwlib.impl.multiblock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.abstracts.AbstractTickingMenuBlock;
import dev.cworldstar.libs.cwlib.builders.MultiblockStructureBuilder;
import dev.cworldstar.libs.cwlib.utils.BlockStorageHelper;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
//import eu.decentsoftware.holograms.api.DHAPI;
//import eu.decentsoftware.holograms.api.holograms.Hologram;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
	
public abstract class MultiblockCore extends AbstractTickingMenuBlock {

	/**
	 * This method is triggered ONLY when the multiblock is assembled.
	 * It will not tick.
	 * @param who The player who triggered this multiblock's assembly.
	 * @param at Where the multiblock was assembled.
	 */
	protected abstract void onMultiblockAssemble(@NotNull Player who, Location at);
	
	/**
	 * This method is triggered ONLY when the multiblock is disassembled.
	 * It is not a required method.
	 * @param who The player who triggered this multiblock's assembly.
	 * @param at Where the multiblock was assembled.
	 */
	protected void onMultiblockDisassemble(@Nullable Player who, Location at) {
		//DHAPI.removeHologram(BlockStorageHelper.stringFromJSON(
		//		BlockStorage.getBlockInfoAsJson(at),
		//		"hologramID"
		//	)
		//);
		if(who != null) {
			who.sendMessage(FormatUtils.mm("<red>You have disassembled this multiblock."));
		} else {
			Bukkit.getScheduler().runTask(ANOSF.get(), () -> {
				at.getNearbyEntitiesByType(Player.class, 32).forEach(player -> {
					player.sendMessage(FormatUtils.mm("<red>The multiblock at " + at.toVector().toString() + " has been disassembled!"));
				});
			});
		}
	}
	
	/**
	 * This method is triggered ONLY when the core is placed.
	 * It is not a required method.
	 * @param who The player who placed this core.
	 * @param at Where the core was placed
	 */
	protected void onCorePlace(@NotNull Player who, Location at) {
		who.sendMessage(FormatUtils.mm("<red>You have disassembled this multiblock."));
	}
	
	/**
	 * This method is triggered ONLY when the core is broken.
	 * It is not a required method.
	 * @param who The player who broke this core.
	 * @param at Where the core was broken
	 */
	protected void onCoreBreak(@NotNull Player who, Location at) {
		who.sendMessage(FormatUtils.mm("<red>You have disassembled this multiblock."));
	}
	
	/**
	 * This method is triggered whenever the ticker updates multiblocks.
	 * Use this for more advanced machines that produce items.
	 * @param core The MultiblockCore of this Multiblock.
	 * @param menu The BlockMenu of this Multiblock.
	 */
	protected abstract void onMultiblockTick(BlockMenu menu, Block core);
	
	/**
	 * This method is triggered whenever the MultiblockCore is right-clicked.
	 * This method will not trigger if the Multiblock isn't completed.
	 * Use this for more advanced machines that have UI components.
	 * @param core The MultiblockCore of this Multiblock.
	 * @param data The config data of this Multiblock.
	 */
	protected void onRightClick(Player who, Block b) {
		who.playSound(Sound.sound().type(Key.key("minecraft:entity.experience_orb.pickup")).volume(1).pitch(1).build());
		who.sendMessage(FormatUtils.mm("<green>This multiblock is assembled!"));
	}
	
	private String multiblockId = "NULL";
	
	public String id() {
		return multiblockId;
	}
	
	//public Hologram getHologram(Location loc) {
	//	return DHAPI.getHologram(BlockStorageHelper.stringFromJSON(
	//			BlockStorage.getBlockInfoAsJson(loc), 
	//			"hologramID"
	//		)
	//	);
	//}
	
	public MultiblockCore(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe);
		
		this.multiblockId = item.getItemId();
		
		addItemHandler(new BlockPlaceHandler(false) {
			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
				String hologramId = UUID.randomUUID().toString().replace("}", "").replace("{", "");
				//Hologram hologram = DHAPI.createHologram(hologramId, e.getBlock().getLocation().toBlockLocation(), true);
				//DHAPI.addHologramLine(hologram, "<gray>Assembled:<red> false");
				BlockStorage.addBlockInfo(e.getBlock(), "hologramID", hologramId);
				BlockStorageHelper.set(e.getBlock(), "assembled", false);
				onCorePlace(e.getPlayer(), e.getBlock().getLocation());
				if(multiblockAssembled(e.getBlock().getLocation())) {
					BlockStorage.addBlockInfo(e.getBlock(), "assembled", String.valueOf(true));
					//DHAPI.setHologramLine(hologram,0,"<gray>Assembled:<green> true");
					onMultiblockAssemble(e.getPlayer(), e.getBlock().getLocation());
				} else {
					e.getPlayer().sendMessage(FormatUtils.asText("This multiblock is invalid!"));
					boolean prints = ((ANOSF) ANOSF.get()).getMultiblockChatOption().getSelectedOption(e.getPlayer(), null).get();
					for(Component s : getMissingPieces(e.getBlock().getLocation(), prints)) {
						e.getPlayer().sendMessage(s);
					}
				}
			}
		});
		
		addItemHandler(new BlockBreakHandler(false, false) {
			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
				//Hologram hologram = getHologram(e.getBlock().getLocation());
				//DHAPI.removeHologram(hologram.getName());
				onCoreBreak(e.getPlayer(), e.getBlock().getLocation());	
			}
		});
		
		addItemHandler(new BlockUseHandler() {
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				
				Block b;
				if(e.getClickedBlock().isPresent()) {
					b = e.getClickedBlock().get();
				} else {
					return;
				}
				
				boolean assembled = BlockStorageHelper.getBoolean(b, "assembled");
				
				if(
						multiblockAssembled(b.getLocation()) && 
						!assembled
				)
				{
					//String hologramId = UUID.randomUUID().toString().replace("}", "").replace("{", "");
					//Hologram hologram = DHAPI.createHologram(hologramId, b.getLocation().toBlockLocation(), true);
					//DHAPI.addHologramLine(hologram, "<gray>Assembled:<red> true");
					BlockStorage.addBlockInfo(b, "assembled", String.valueOf(true));
					onMultiblockAssemble(e.getPlayer(), b.getLocation());
				} else if(!assembled) {
					e.getPlayer().sendMessage(FormatUtils.asText("This multiblock is invalid!"));
					boolean prints = ((ANOSF) ANOSF.get()).getMultiblockChatOption().getSelectedOption(e.getPlayer(), null).get();
					for(Component s : getMissingPieces(b.getLocation(), prints)) {
						e.getPlayer().sendMessage(s);
					}
				} else {
					MultiblockCore.this.onRightClick(e.getPlayer(), b);
				}
			}
		});
		
		
		setLocationItem(new Vector(0,0,0), this.getId(), Color.RED);
	}
	
	private void assembleNaturally(Block core) {
		new BukkitRunnable() {
			private boolean triggeredEntity = false;

			@Override
			public void run() {
				for(LivingEntity entity : core.getLocation().getNearbyLivingEntities(32)) {
					if(entity instanceof Player) {
						entity.sendMessage(FormatUtils.mm("<light_purple>Multiblock Assembled!"));
						if(!triggeredEntity) {
							BlockStorageHelper.set(core.getLocation(), "assembled", true);
							onMultiblockAssemble((Player) entity, core.getLocation());
							triggeredEntity = true;
						}
					}
				}
			}
		}.runTask(AbstractSFAddon.get());
	}

	@Override
	public void tick(Block core, BlockMenu menu) {
		
		boolean multiblockAssembled = multiblockAssembled(core);
		
		boolean assembled = BlockStorageHelper.getBoolean(
				core, 
				"assembled"
		);
		
		if(!multiblockAssembled && assembled) {
			BlockStorageHelper.set(core.getLocation(), "assembled", String.valueOf(false));
			onMultiblockDisassemble(null, core.getLocation().toBlockLocation());
			return;
		}
		
		if(multiblockAssembled && assembled) {
			onMultiblockTick(menu, core);
		} else if(multiblockAssembled && !assembled) {
			assembleNaturally(core);
		}		
	}

	private final Map<Vector, MultiblockChoice> multiblock = new HashMap<Vector, MultiblockChoice>();
	
	public MultiblockCore fromStructureBuilder(MultiblockStructureBuilder builder) {
		for(Entry<Vector, MultiblockChoice> structurePoint : builder.structure().entrySet()) {
			multiblock.put(structurePoint.getKey(), structurePoint.getValue());
		}
		return this;
	}
	
	/**
	 * This method will set a location item. 
	 * @param offsetInObjectSpace The OffsetVector of this object's location. West -1, East +1, South +1, North -1
	 * @param idOrMaterial Either the ID of a {@link SlimefunItem}, or the result of {@link Material#name()}.
	 */
	public MultiblockCore setLocationItem(Vector offsetInObjectSpace, String idOrMaterial, Color errorColor) {
		Validate.notNull(offsetInObjectSpace, "offsetInObjectSpace must not be null!");
		Validate.notNull(idOrMaterial, "ID or Material must not be null!");
		multiblock.put(offsetInObjectSpace, new MultiblockChoice(errorColor, idOrMaterial));
		
		return this;
	}
	
	public List<Component> getMissingPieces(Location multiblockCoreLocation, boolean prints) {
		List<Component> str = new ArrayList<Component>();
		for(Entry<Vector, MultiblockChoice> blockEntry : multiblock.entrySet()) {
			Vector offset = blockEntry.getKey();
			MultiblockChoice choice = blockEntry.getValue();
			
			Location loc = multiblockCoreLocation.clone().add(offset);
			if(!choice.resolve(loc)) {
				str.addAll(Arrays.asList(choice.error(loc, prints)));
			}
		}
		
		if(str.size() == 0 && prints) {
			str.add(FormatUtils.mm("The multiblock is assembled."));
		}
		
		return str;
	}
	
	
	public boolean multiblockAssembled(Location multiblockCoreLocation) {
		int matches = 0;
		for(Entry<Vector, MultiblockChoice> blockEntry : multiblock.entrySet()) {
			Vector offset = blockEntry.getKey();
			MultiblockChoice choice = blockEntry.getValue();
			
			Location loc = multiblockCoreLocation.clone().add(offset);
			if(choice.resolve(loc)) {
				matches += 1;
			}
		}
		return matches >= multiblock.size();
	}
	public boolean multiblockAssembled(Block multiblockCoreLocation) {
		int matches = 0;
		for(Entry<Vector, MultiblockChoice> blockEntry : multiblock.entrySet()) {
			Vector offset = blockEntry.getKey();
			MultiblockChoice choice = blockEntry.getValue();
			
			Location loc = multiblockCoreLocation.getLocation().add(offset);
			if(choice.resolve(loc)) {
				matches += 1;
			}
		}
		return matches >= multiblock.size();
	}
}
