package dev.cworldstar.anosf.items.machines.multiblocks;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.recipes.AlloyForgeRecipe;
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
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class AlloyForgeMultiblock extends MultiblockCore {

	private static final int[] BACKGROUND_SLOTS = new int[] {
			4,
			9,17,
			18,26,
			27,28,29,30,32,33,34,35,
			36,44,
			45,46,47,48,49,50,51,52,53
	};
	
	private static final ItemStack ALLOY_FORGE_CORE = new ItemStackBuilder(Material.RED_MUSHROOM_BLOCK)
			.name(ItemTier.ADVANCED.getColor() + "Alloy Forge Core")
			.setLore(new String[] {
					"",
					"<gray>The core of the alloy forge.",
					"",
					Items.ItemTier.makeItemString(ItemTier.BASIC, "Multiblock Core")
			})
			.get();
	
	public AlloyForgeMultiblock() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), new SlimefunItemStack("ALLOY_FORGE_CORE", ALLOY_FORGE_CORE), RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				SlimefunItems.WITHER_PROOF_GLASS.asOne(), SlimefunItems.WITHER_PROOF_GLASS.asOne(), SlimefunItems.WITHER_PROOF_GLASS.asOne(),
				SlimefunItems.WITHER_PROOF_GLASS.asOne(), ItemRegistry.getRegistryItem("MACHINE_CORE_BASIC").getItem(), SlimefunItems.WITHER_PROOF_GLASS.asOne(),
				SlimefunItems.WITHER_PROOF_OBSIDIAN.asOne(),ItemRegistry.getRegistryItem("BLACK_IRON_BLOCK").getItem(),SlimefunItems.WITHER_PROOF_OBSIDIAN.asOne()
		});
		
		fromStructureBuilder(new MultiblockStructureBuilder(this.getId())
				.solidSquare(new MultiblockChoice(Color.BLACK, "NETHER_BRICKS"), 2, -1)
				.solidSquare(new MultiblockChoice(Color.RED, "NETHERRACK"), 1, -1)
				.column(new MultiblockChoice(Color.BLACK, "NETHER_BRICKS"), 2, 2, 2, 1)
				.column(new MultiblockChoice(Color.BLACK, "NETHER_BRICKS"), 2, -2, 2, 1)
				.column(new MultiblockChoice(Color.BLACK, "NETHER_BRICKS"), 2, -2, -2, 1)
				.column(new MultiblockChoice(Color.BLACK, "NETHER_BRICKS"), 2, 2, -2, 1)
				.square(new MultiblockChoice(Color.BLACK, "NETHER_BRICKS"), 3, 2)
		);
		ItemRegistry.registerItem(this);
	}

	@Override
	protected void onMultiblockAssemble(@NotNull Player who, Location at) {
		who.sendMessage(FormatUtils.mm("<red>Alloy Forge Assembled!"));
		BlockStorageHelper.set(at, "producing", String.valueOf(false));
		BlockStorageHelper.set(at, "work", String.valueOf(0));
		BlockStorageHelper.set(at, "recipe", "");
		BlockStorageHelper.set(at, "items", "");
	}
	
	private static final ItemStack STATUS_ITEM = new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("<gray>Idle.").get();
	private static final ItemStack NEED_FIRE = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setName("<red>No Fire!").get();

	
	private boolean isProducing(Block core) {
		return BlockStorageHelper.getBoolean(core, "producing");
	}

	private void work(BlockMenu menu, Block core, @Nullable AlloyForgeRecipe recipe) {
		if(recipe == null) {
			return;
		}
		
		int work = BlockStorageHelper.getInteger(core, "work");
		
		ItemStack statusItem = menu.getItemInSlot(31);
		
		if(!statusItem.getType().equals(Material.GREEN_STAINED_GLASS_PANE)) {
			statusItem = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
			menu.replaceExistingItem(31, statusItem);
		}
		
		
		statusItem.editMeta(meta -> {
			meta.displayName(FormatUtils.mm("Crafting..."));
			meta.lore(FormatUtils.lore(new String[] {
					"<gray>Recipe: <aqua>" + recipe.getId(),
					"<gray>Completion: " + FormatUtils.makeMachineCompletion(work, recipe.getWork()) + " <gray>(<green> " + work + "<gray> / <red>" + recipe.getWork() + "<gray>)"	
			}));
		});
		
		if(work >= recipe.getWork()) {
			
			if(!statusItem.getType().equals(Material.YELLOW_STAINED_GLASS_PANE)) {
				menu.replaceExistingItem(31, STATUS_ITEM.clone());
			}
			
			menu.pushItem(recipe.getOutput().clone(), getOutputSlots());
			BlockStorageHelper.set(core, "producing", false);
			BlockStorageHelper.set(core, "work", 0);
			return;
		}
		
		boolean removeFireBlock = RandomUtils.nextInt() * 10 > 8;
		if(removeFireBlock) {
			int x = RandomUtils.nextInt(0, 2) - 1;
			int z = RandomUtils.nextInt(0, 2) - 1;
			
			if(x == 0 && z == 0) {
				x += 1;
			}
			
			boolean isFire = core.getLocation().add(new Vector(x, 0, z)).getBlock().getType().equals(Material.FIRE);
			if(isFire) {
				core.getLocation().add(new Vector(x, 0, z)).getBlock().setType(Material.AIR);
				core.getLocation().getWorld().playSound(core.getLocation(), org.bukkit.Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
			}
		}
		
		BlockStorageHelper.set(core, "work", work + 1);
	}
	
	
	
	@Override
	protected void onMultiblockTick(BlockMenu menu, Block core) {
		// handle input slots
		
		if(menu.hasViewer()) {
			clean(menu, core);
			for(SlimefunItemEntry entry : BlockStorageHelper.getSlimefunItemEntries(core, "items")) {
				display(menu, core, entry);
			}
		}
		
		for(int slot : getInputSlots()) {
			ItemStack inSlot = menu.getItemInSlot(slot);
			if(inSlot != null) {
				int amount = inSlot.getAmount();
				if(!BlockStorageHelper.exists(core, "items")) {
					SlimefunItem item = SlimefunItem.getByItem(inSlot);
					SlimefunItemEntry entry;
					if(item == null) {
						entry = new SlimefunItemEntry(inSlot, amount);
					} else {
						entry = new SlimefunItemEntry(item.getId(), amount);
					}
					BlockStorageHelper.storeSlimefunEntry(core, "items", entry);
					inSlot.setAmount(0);
				} else {
					SlimefunItemEntry entry = BlockStorageHelper.getSlimefunItemEntry(core, "items", inSlot);
					if(entry.isNull()) {
						SlimefunItem item = SlimefunItem.getByItem(inSlot);
						if(item == null) {
							entry = new SlimefunItemEntry(inSlot, 0);
						} else {
							entry = new SlimefunItemEntry(item.getId(), 0);
						}

					}
					entry.addAmount(amount);
					BlockStorageHelper.storeSlimefunEntry(core, "items", entry);
					inSlot.setAmount(0);
				}

			}
		}
		
		clean(menu, core, 37,38,39,40,41,42,43);
		for(Entry<String, AlloyForgeRecipe> recipeEntry : AlloyForgeRecipe.recipes().entrySet()) {
			AlloyForgeRecipe recipe = recipeEntry.getValue();
			String recipeID = recipeEntry.getKey();
			if(match(core, recipe.inputs())) {
				display(menu, core, recipe,37,38,39,40,41,42,43);
			}
		}
		
		// do work now
		if(isProducing(core)) {
			for(int x=-1; x<=1; x++) {
				for(int z=-1; z<=1; z++) {
					if(x == 0 && z == 0) continue;
					boolean isFire = core.getLocation().add(new Vector(x, 0, z)).getBlock().getType().equals(Material.FIRE);
					if(!isFire) {
						menu.replaceExistingItem(31, NEED_FIRE);
						return;
					}
				}
			}
			work(menu, core, AlloyForgeRecipe.getAlloyForgeRecipe(BlockStorageHelper.getString(core, "recipe")));
		} else {
			AlloyForgeRecipe recipe = AlloyForgeRecipe.getAlloyForgeRecipe(BlockStorageHelper.getString(core, "recipe"));
			if(recipe != null && !isProducing(core)) {
				// check if we have the required materials
				List<SlimefunItemEntry> inputs = recipe.inputs();
				if(match(core, inputs)) {
					for(SlimefunItemEntry entry : inputs) {
						SlimefunItemEntry itemEntry = BlockStorageHelper.getSlimefunItemEntry(core, "items", entry.getSlimefunItemId());
						if(itemEntry.isNull()) {
							continue;
						}
						itemEntry.subtract(entry.getAmount());
						BlockStorageHelper.storeSlimefunEntry(core, "items", itemEntry);
					}
					
					for(int x=-1; x<=1; x++) {
						for(int z=-1; z<=1; z++) {
							if(x == 0 && z == 0) continue;
							boolean isFire = core.getLocation().add(new Vector(x, 0, z)).getBlock().getType().equals(Material.FIRE);
							if(!isFire) {
								menu.replaceExistingItem(31, NEED_FIRE);
								// the alloy forge must be lit!
								return;
							}
						}
					}
					
					BlockStorageHelper.set(core, "producing", true);
					work(menu, core, recipe);
				}
			}
		}
	}

	private boolean match(Block core, List<SlimefunItemEntry> entries) {
		int matches = 0;
		for(SlimefunItemEntry entry : entries) {
			SlimefunItemEntry itemEntry = BlockStorageHelper.getSlimefunItemEntry(core, "items", entry.getSlimefunItemId());
			if(itemEntry.isNull()) {
				continue;
			}
			if(itemEntry.getAmount() >= entry.getAmount()) {
				matches++;
			}
		}
		return matches >= entries.size();
	}
	
	private void clean(BlockMenu menu, Block core, int ...ints) {
		for(int slot : ints) {
			menu.replaceExistingItem(slot, new ItemStack(Material.AIR));
		}
	}

	private void clean(BlockMenu menu, Block core) {
		for(int slot : itemDisplaySlots) {
			ItemStack item = menu.getItemInSlot(slot);
			if(item != null && item.getType().equals(Material.WHITE_STAINED_GLASS_PANE)) {
				if(item.hasItemMeta()) {
					if(item.getItemMeta().hasEnchantmentGlintOverride()) {
						continue;
					}
				}
			}
			menu.replaceExistingItem(slot, new ItemStack(Material.AIR));
		}
	}

	private static final int[] itemDisplaySlots = new int[] {
			10,11,12,13,14,15,16,
			19,20,21,22,23,24,25
	};
	
	private static final int[] recipeDisplaySlots = new int[] {
			37,38,39,40,41,42,43
	};
	
	private void display(BlockMenu menu, Block core, SlimefunItemEntry entry) {
		for(int slot : itemDisplaySlots) {
			if(menu.getItemInSlot(slot) == null) {
				menu.replaceExistingItem(slot, entry.toDisplayItem());
				break;
			}
		}
	}
	
	@Override
	public void onNewInstance(BlockMenu menu, Block b) {
		
		for(int slot : itemDisplaySlots) {
			menu.addMenuClickHandler(slot, new MenuClickHandler() {
				@Override
				public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
					SlimefunItem sfItem = SlimefunItem.getByItem(item);
					if(sfItem != null) {
						// it's a slimefun item
						SlimefunItemEntry entry = BlockStorageHelper.getSlimefunItemEntry(b, "items", sfItem.getId());
						// get amount that should be subtracted
						long amount = 64-(64-entry.getAmount());
						// subtract amount
						entry.subtract(amount);
						
						// set cursor item
						p.setItemOnCursor(sfItem.getItem().asQuantity((int) amount));
						// re-store sf entry
						BlockStorageHelper.storeSlimefunEntry(b, "items", entry);
					} else {
						// it's a normal item
						SlimefunItemEntry entry = BlockStorageHelper.getSlimefunItemEntry(b, "items", item.getType().toString());
						long amount = 64-(64-entry.getAmount());
						// subtract amount
						entry.subtract(amount);
						// set cursor item
						p.setItemOnCursor(new ItemStack(item.getType(), (int) amount));
						// re-store sf entry
						BlockStorageHelper.storeSlimefunEntry(b, "items", entry);
					}
					return false;
				}
			});
		}
		
		for(int slot : recipeDisplaySlots) {
			menu.addMenuClickHandler(slot, new MenuClickHandler() {
				@Override
				public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
					if(item != null) {
						item.editMeta(meta -> {
							meta.setEnchantmentGlintOverride(true);
						});
						for(int rdslot : recipeDisplaySlots) {
							ItemStack inSlot = menu.getItemInSlot(rdslot);
							if(inSlot != null && slot != rdslot) {
								ItemMeta meta = inSlot.getItemMeta();
								if(meta.hasEnchantmentGlintOverride()) {
									meta.setEnchantmentGlintOverride(false);
								}
							}
						}
						BlockStorageHelper.store(b, "recipe", PlainTextComponentSerializer.plainText().serialize(item.displayName()).replace("[", "").replace("]",""));
					}
					return false;
				}
			});
		}
	}
	
	private void display(BlockMenu menu, Block core, AlloyForgeRecipe entry, int ...ints) {
		for(int slot : ints) {
			if(menu.getItemInSlot(slot) == null) {
				menu.replaceExistingItem(slot, entry.getRecipeDisplay());
				break;
			}
		}
	}

	@Override
	protected void onRightClick(Player who, Block b) {
		try {
			BlockStorage.getInventory(b).open(who);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	//TODO: replace this with changeable pages
	@Override
	public void setup(BlockMenuPreset preset) {
		preset.addItem(31, STATUS_ITEM);
		preset.drawBackground(new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE).name("<green>Input Slots").build(), new int[] {0, 3});
		preset.drawBackground(new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).name("<red>Output Slots").build(), new int[] {5, 8});
		preset.drawBackground(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).empty().build(), BACKGROUND_SLOTS);
	}

	@Override
	public int[] getInputSlots() {
		return new int[] {
				1,2
		};
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {
				6,7
		};
	}
}
