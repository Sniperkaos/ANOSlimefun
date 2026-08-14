package dev.cworldstar.anosf.items.machines.multiblocks;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.cosmicforge.CosmicForgeInterface;
import dev.cworldstar.anosf.items.machines.cosmicforge.CosmicForgePylon;
import dev.cworldstar.anosf.items.machines.cosmicforge.CosmicForgeRing;
import dev.cworldstar.anosf.items.recipes.CosmicForgeRecipe;
import dev.cworldstar.anosf.items.recipes.MolecularWorkbenchRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.builders.MultiblockStructureBuilder;
import dev.cworldstar.libs.cwlib.impl.multiblock.MultiblockChoice;
import dev.cworldstar.libs.cwlib.impl.multiblock.MultiblockCore;
import dev.cworldstar.libs.cwlib.utils.BlockStorageHelper;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import dev.cworldstar.libs.cwlib.utils.SlimefunItemEntry;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class CosmicForgeMultiblock extends MultiblockCore  {

	private static final ItemStack COSMIC_WORKBENCH_CORE = new ItemStackBuilder(Material.GILDED_BLACKSTONE)
			.setName(ItemTier.COSMIC.makeName("Forge"))
			.setLore(new String[] {
					"",
					"<gray>The core of the cosmic forge.",
					"",
					Items.ItemTier.makeItemString(ItemTier.COSMIC, "Multiblock Core")
			})
			.get();
	
	public static ItemStack getCore() {
		return COSMIC_WORKBENCH_CORE;
	}
	
	private boolean containsAtLeast(Block core, SlimefunItem item, long amount) {
		SlimefunItemEntry entry = BlockStorageHelper.getSlimefunItemEntry(core, "items", item.getItem());
		if(entry.isNull() || entry.isEmpty()) {
			return false;
		}
		return entry.getAmount() < amount;
	}
	
	public CosmicForgeMultiblock() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), new SlimefunItemStack("COSMIC_WORKBENCH_CORE", COSMIC_WORKBENCH_CORE), Items.MOLECULAR_CRAFTER_RECIPE_TYPE, new ItemStack[] {
				SlimefunItems.SILICON.asOne(), SlimefunItems.SILICON.asOne(), SlimefunItems.SILICON.asOne(),
				SlimefunItems.SILICON.asOne(), ItemRegistry.getRegistryItem("MACHINE_CORE_HIGH").getItem(), SlimefunItems.SILICON.asOne(),
				SlimefunItems.WITHER_PROOF_OBSIDIAN.asOne(), SlimefunItems.WITHER_PROOF_OBSIDIAN.asOne(), SlimefunItems.WITHER_PROOF_OBSIDIAN.asOne()
		});
		new CosmicForgePylon();
		new CosmicForgeRing();
		new CosmicForgeInterface();
		
		fromStructureBuilder(new MultiblockStructureBuilder(getId())
			.cube(new MultiblockChoice(Color.YELLOW, "COSMIC_FORGE_PYLON"), 1)
			.line(new MultiblockChoice(Color.YELLOW, "COSMIC_FORGE_PYLON"), new Vector(0, -12, 0), new Vector(0, 12, 0))
			
			.at(new Vector(-1, 1, -1), new MultiblockChoice(Color.WHITE,"AIR"))
			.at(new Vector(1, 1, -1), new MultiblockChoice(Color.WHITE,"AIR"))
			.at(new Vector(-1, 1, 1), new MultiblockChoice(Color.WHITE,"AIR"))
			.at(new Vector(1, 1, 1), new MultiblockChoice(Color.WHITE,"AIR"))
			
			.at(new Vector(-1, -1, -1), new MultiblockChoice(Color.WHITE,"AIR"))
			.at(new Vector(1, -1, -1), new MultiblockChoice(Color.WHITE,"AIR"))
			.at(new Vector(-1, -1, 1), new MultiblockChoice(Color.WHITE,"AIR"))
			.at(new Vector(1, -1, 1), new MultiblockChoice(Color.WHITE,"AIR"))
			
			.at(new Vector(-1, 0, 0), new MultiblockChoice(Color.AQUA,"COSMIC_FORGE_INTERFACE"))
			.at(new Vector(1, 0, 0), new MultiblockChoice(Color.AQUA,"COSMIC_FORGE_INTERFACE"))
			.at(new Vector(0, 0, 1), new MultiblockChoice(Color.AQUA,"COSMIC_FORGE_INTERFACE"))
			.at(new Vector(0, 0,- 1), new MultiblockChoice(Color.AQUA,"COSMIC_FORGE_INTERFACE"))

			.square(new MultiblockChoice(Color.BLACK, "COSMIC_FORGE_RING"), 0, 5)
			.square(new MultiblockChoice(Color.BLACK, "COSMIC_FORGE_RING"), 4, 4)
			.square(new MultiblockChoice(Color.BLACK, "COSMIC_FORGE_RING"), 8, 3)
			.square(new MultiblockChoice(Color.BLACK, "COSMIC_FORGE_RING"), 12, 2)
			.square(new MultiblockChoice(Color.BLACK, "COSMIC_FORGE_RING"), -4, 4)
			.square(new MultiblockChoice(Color.BLACK, "COSMIC_FORGE_RING"), -8, 3)
			.square(new MultiblockChoice(Color.BLACK, "COSMIC_FORGE_RING"), -12, 2)
		);
		ItemRegistry.registerItem(this);
	}
	
	@Override
	protected void onRightClick(Player who, Block b) {
		BlockStorage.getInventory(b).open(who);
	}

	@Override
	protected void onMultiblockAssemble(@NotNull Player who, Location at) {
		who.sendMessage(FormatUtils.mm(ItemTier.COSMIC.makeName("Forge") +  "<gray> Assembled!"));
		BlockStorageHelper.set(at, "producing", String.valueOf(false));
		BlockStorageHelper.set(at, "work", String.valueOf(0));
		BlockStorageHelper.set(at, "recipe", "");
	}

	private boolean isProducing(Block core) {
		return BlockStorageHelper.getBoolean(core, "producing");
	}
	
	@Override
	protected void onMultiblockTick(BlockMenu menu, Block core) {
		if(isProducing(core)) {
			//work(menu, core, MolecularWorkbenchRecipe.tryFindRecipe(BlockStorageHelper.getString(core, "recipe")));
		} else {
			MolecularWorkbenchRecipe recipe = MolecularWorkbenchRecipe.tryFindRecipe(getCraftingSlotContents(menu));
			if(recipe != null) {
				ItemStack statusItem = menu.getItemInSlot(4);
				if(!statusItem.getType().equals(Material.CYAN_STAINED_GLASS_PANE)) {
					menu.replaceExistingItem(4, RECIPE_INFO);
					statusItem = menu.getItemInSlot(4);
				}
				statusItem.editMeta(meta -> {
					meta.lore(FormatUtils.lore(new String[] {
							"<gray>Recipe: <aqua>" + recipe.getID(),
							"<gray>Click to craft!"
					}));
				});
			} else {
				ItemStack statusItem = menu.getItemInSlot(4);
				if(!statusItem.getType().equals(Material.YELLOW_STAINED_GLASS_PANE)) {
					menu.replaceExistingItem(4, STATUS_ITEM);
				}
			}
		}
	}

	private void work(BlockMenu menu, Block core, @Nullable CosmicForgeRecipe recipe) {
		if(recipe == null) {
			return;
		}
	}

	private static final ItemStack CRAFT_ITEM = new ItemStackBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("<green>Click to craft!").get();
	private static final ItemStack OUTPUT_SLOT = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setName("<green>Output Slots").get();
	private static final ItemStack RECIPE_INFO = new ItemStackBuilder(Material.CYAN_STAINED_GLASS_PANE).setName("<blue>Recipe Info").get();

	
	private static final ItemStack STATUS_ITEM = new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("<gray>Idle.").get();

	private int[] craftingSlots = new int[] {
			10,11,12,13,14,15,16,
			19,20,21,22,23,24,25,
			28,29,30,31,32,33,34,
			37,38,39,40,41,42,43
	};
	
	@Override
	public void setup(BlockMenuPreset preset) {
		preset.drawBackground(new int[] {
				0,1,2,3,5,6,7,8,
				9,17,
				18,26,
				27,35,
				36,44,
				45,53
		});
		preset.drawBackground(OUTPUT_SLOT, new int[] {
				46,48,50,52
		});
		preset.addItem(4, STATUS_ITEM.clone());
		preset.addItem(49, CRAFT_ITEM.clone());
		preset.addMenuClickHandler(4, ChestMenuUtils.getEmptyClickHandler());
	}
	
	public ArrayList<ItemStack> getCraftingSlotContents(BlockMenu menu) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for(int slot : craftingSlots) {
			items.add(menu.getItemInSlot(slot));
		}
		return items;
	}
	
	@Override
	public void onNewInstance(BlockMenu menu, Block core) {
		menu.addMenuClickHandler(49, new MenuClickHandler() {
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				
				if(!BlockStorageHelper.getBoolean(core, "assembled")) {
					p.sendMessage(FormatUtils.mm("<red>The workbench is not assembled."));
					return false;
				}
				
				if(BlockStorageHelper.getBoolean(core, "producing")) {
					p.sendMessage(FormatUtils.mm("<gray>The workbench is already producing something."));
					return false;
				} else {
					MolecularWorkbenchRecipe recipe = MolecularWorkbenchRecipe.tryFindRecipe(getCraftingSlotContents(menu));
					if(recipe != null) {
						for(int craftingSlot : craftingSlots) {
							ItemStack itemSlot = menu.getItemInSlot(craftingSlot);
							if(itemSlot != null) {
								itemSlot.subtract();
							}
						}
						BlockStorageHelper.set(core, "recipe", recipe.getID());
						BlockStorageHelper.set(core, "producing", true);
					} else {
						p.sendMessage(FormatUtils.mm("<red>A recipe does not exist for the given items."));
					}
				}
				
				return false;
			}
		});
	}

	@Override
	public int[] getInputSlots() {
		return new int[0];
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {
				50,51,47,48
		};
	}

}
