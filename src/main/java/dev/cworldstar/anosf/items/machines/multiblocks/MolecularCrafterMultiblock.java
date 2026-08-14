package dev.cworldstar.anosf.items.machines.multiblocks;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.molecularcrafter.MolecularWorkbenchCasing;
import dev.cworldstar.anosf.items.machines.molecularcrafter.MolecularWorkbenchController;
import dev.cworldstar.anosf.items.machines.molecularcrafter.MolecularWorkbenchCrafter;
import dev.cworldstar.anosf.items.recipes.MolecularWorkbenchRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.builders.MultiblockStructureBuilder;
import dev.cworldstar.libs.cwlib.impl.multiblock.MultiblockChoice;
import dev.cworldstar.libs.cwlib.impl.multiblock.MultiblockCore;
import dev.cworldstar.libs.cwlib.utils.BlockStorageHelper;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class MolecularCrafterMultiblock extends MultiblockCore  {

	private static final ItemStack MOLECULAR_WORKBENCH_CORE = new ItemStackBuilder(Material.GILDED_BLACKSTONE)
			.setName("<gradient:gray:gold:gold:gray>Molecular Assembler Core")
			.setLore(new String[] {
					"",
					"<gray>The core of the molecular assembler.",
					"",
					Items.ItemTier.makeItemString(ItemTier.HIGH, "Multiblock Core")
			})
			.get();
	
	public static ItemStack getCore() {
		return MOLECULAR_WORKBENCH_CORE;
	}
	
	public MolecularCrafterMultiblock() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), new SlimefunItemStack("MOLECULAR_WORKBENCH_CORE", MOLECULAR_WORKBENCH_CORE), RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				SlimefunItems.SILICON.asOne(), SlimefunItems.SILICON.asOne(), SlimefunItems.SILICON.asOne(),
				SlimefunItems.SILICON.asOne(), ItemRegistry.getRegistryItem("MACHINE_CORE_HIGH").getItem(), SlimefunItems.SILICON.asOne(),
				SlimefunItems.WITHER_PROOF_OBSIDIAN.asOne(), SlimefunItems.WITHER_PROOF_OBSIDIAN.asOne(), SlimefunItems.WITHER_PROOF_OBSIDIAN.asOne()
		});
		
		new MolecularWorkbenchCasing();
		new MolecularWorkbenchController();
		new MolecularWorkbenchCrafter();
		
		fromStructureBuilder(new MultiblockStructureBuilder(getId())
			.cube(new MultiblockChoice(Color.GRAY, "MOLECULAR_WORKBENCH_CASING"), 2)
			.cube(new MultiblockChoice(Color.BLACK, "MOLECULAR_WORKBENCH_CRAFTER"), 1)
			.at(new Vector(0, 0, -2), "MOLECULAR_WORKBENCH_CONTROLLER", null)
			.at(new Vector(0, 0, 2), "MOLECULAR_WORKBENCH_CONTROLLER", null)
			.at(new Vector(2, 0, 0), "MOLECULAR_WORKBENCH_CONTROLLER", null)
			.at(new Vector(-2, 0, 0), "MOLECULAR_WORKBENCH_CONTROLLER", null)
			.at(new Vector(0, 2, 0), "MOLECULAR_WORKBENCH_CONTROLLER", null)
			.at(new Vector(0, -2, 0), "MOLECULAR_WORKBENCH_CONTROLLER", null)
			.square(new MultiblockChoice(Color.WHITE, "AIR"), 2, 2)
			.square(new MultiblockChoice(Color.WHITE, "AIR"), -2, 2)
			.column(new MultiblockChoice(Color.WHITE, "AIR"), 2, 2, 2)
			.column(new MultiblockChoice(Color.WHITE, "AIR"), 2, -2, 2)
			.column(new MultiblockChoice(Color.WHITE, "AIR"), 2, -2, -2)
			.column(new MultiblockChoice(Color.WHITE, "AIR"), 2, 2, -2)
		);
		ItemRegistry.registerItem(this);
	}
	
	@Override
	protected void onRightClick(Player who, Block b) {
		BlockStorage.getInventory(b).open(who);
	}

	@Override
	protected void onMultiblockAssemble(@NotNull Player who, Location at) {
		who.sendMessage(FormatUtils.mm("<light_purple>Molecular Assembler Assembled!</light_purple>"));
		BlockStorageHelper.set(at, "producing", String.valueOf(false));
		BlockStorageHelper.set(at, "work", String.valueOf(0));
		BlockStorageHelper.set(at, "recipe", "");
		BlockStorageHelper.set(at, "deviation", 0);
	}

	private boolean isProducing(Block core) {
		return BlockStorageHelper.getBoolean(core, "producing");
	}
	
	@Override
	protected void onMultiblockTick(BlockMenu menu, Block core) {
		if(isProducing(core)) {
			work(menu, core, MolecularWorkbenchRecipe.tryFindRecipe(BlockStorageHelper.getString(core, "recipe")));
		} else {
			MolecularWorkbenchRecipe recipe = MolecularWorkbenchRecipe.tryFindRecipe(getCraftingSlotContents(menu));
			if(recipe != null) {
				ItemStack statusItem = menu.getItemInSlot(49);
				if(!statusItem.getType().equals(Material.CYAN_STAINED_GLASS_PANE)) {
					menu.replaceExistingItem(49, RECIPE_INFO);
					statusItem = menu.getItemInSlot(49);
				}
				statusItem.editMeta(meta -> {
					meta.lore(FormatUtils.lore(new String[] {
							"<gray>Recipe: <aqua>" + recipe.getID(),
							"<gray>Click to craft!"
					}));
				});
			} else {
				ItemStack statusItem = menu.getItemInSlot(49);
				if(!statusItem.getType().equals(Material.YELLOW_STAINED_GLASS_PANE)) {
					menu.replaceExistingItem(49, STATUS_ITEM);
				}
			}
		}
	}

	private void work(BlockMenu menu, Block core, @Nullable MolecularWorkbenchRecipe recipe) {
		if(recipe == null) {
			return;
		}
		
		int work = BlockStorageHelper.getInteger(core, "work");
		int deviation = BlockStorageHelper.getInteger(core, "deviation");
		
		ItemStack statusItem = menu.getItemInSlot(49);
		
		if(!statusItem.getType().equals(Material.GREEN_STAINED_GLASS_PANE)) {
			statusItem = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
			menu.replaceExistingItem(49, statusItem);
		}
		
		
		statusItem.editMeta(meta -> {
			meta.displayName(FormatUtils.mm("Crafting..."));
			meta.lore(FormatUtils.lore(new String[] {
					"<gray>Recipe: <aqua>" + recipe.getID(),
					"<gray>Deviation: <red>" + String.valueOf(deviation) + " / " + "<gray>" + String.valueOf(recipe.getDeviationThreshhold()),
					"<gray>Completion: " + FormatUtils.makeMachineCompletion(work, recipe.getWork()) + " <gray>(<green> " + work + "<gray> / <red>" + recipe.getWork() + "<gray>)"	
			}));
		});
				
		if(deviation >= recipe.getDeviationThreshhold()) {
			menu.pushItem(ItemRegistry.getRegistryItem("DEVIATED_ITEM").getItem().clone(), getOutputSlots());
			BlockStorageHelper.set(core, "producing", false);
			BlockStorageHelper.set(core, "work", 0);
			BlockStorageHelper.set(core, "recipe", "");
			BlockStorageHelper.set(core, "deviation", 0);
			return;
		}
		
		if(work >= recipe.getWork()) {
			
			if(!statusItem.getType().equals(Material.YELLOW_STAINED_GLASS_PANE)) {
				menu.replaceExistingItem(49, STATUS_ITEM.clone());
			}
			
			menu.pushItem(recipe.getOutput().clone(), getOutputSlots());
			BlockStorageHelper.set(core, "producing", false);
			BlockStorageHelper.set(core, "work", 0);
			BlockStorageHelper.set(core, "recipe", "");
			BlockStorageHelper.set(core, "deviation", 0);
			return;
		}
		
		// check for deviation
		boolean deviate = (RandomUtils.nextInt(1, 100) >= recipe.getDeviationChance());
		if(deviate) {
			BlockStorageHelper.set(core, "deviation", deviation + RandomUtils.nextInt(1, 8));
		}
		
		BlockStorageHelper.set(core, "work", work + 1);
	}

	private static final ItemStack OUTPUT_SLOT = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setName("<green>Output Slots").get();
	private static final ItemStack RECIPE_INFO = new ItemStackBuilder(Material.CYAN_STAINED_GLASS_PANE).setName("<blue>Recipe Info").get();

	
	private static final ItemStack STATUS_ITEM = new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("<gray>Idle.").get();

	private int[] craftingSlots = new int[] {
			1,2,3,4,5,6,7,
			10,11,12,13,14,15,16,
			19,20,21,22,23,24,25,
			28,29,30,31,32,33,34,
			37,38,39,40,41,42,43
	};
	
	@Override
	public void setup(BlockMenuPreset preset) {
		preset.drawBackground(new int[] {
				0,8,
				9,17,
				18,26,
				27,35,
				36,44,
				45,53
		});
		preset.drawBackground(OUTPUT_SLOT, new int[] {
				46,48,50,52
		});
		preset.addItem(49, STATUS_ITEM.clone());
	}
	
	public ArrayList<ItemStack> getCraftingSlotContents(BlockMenu menu) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		Inventory inventory = menu.toInventory();
		for(int slot : craftingSlots) {
			items.add(inventory.getItem(slot));
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
					if(
							recipe != null && 
							recipe.isUsable()
					) {
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
