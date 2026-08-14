package dev.cworldstar.anosf.items.machines.multiblocks;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.drugworkbench.DrugWorkbenchCasing;
import dev.cworldstar.anosf.items.machines.drugworkbench.DrugWorkbenchVent;
import dev.cworldstar.anosf.items.recipes.DrugWorkbenchRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.builders.MultiblockStructureBuilder;
import dev.cworldstar.libs.cwlib.impl.multiblock.MultiblockChoice;
import dev.cworldstar.libs.cwlib.impl.multiblock.MultiblockCore;
import dev.cworldstar.libs.cwlib.utils.BlockHelper;
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

public class DrugWorkbenchMultiblock extends MultiblockCore  {

	private static final ItemStack DRUG_WORKBENCH_CORE = new ItemStackBuilder(Material.GILDED_BLACKSTONE)
			.setName("<gradient:white:gray>Drug Workbench Core")
			.setLore(new String[] {
					"",
					"<gray>The core of the drug workbench.",
					"<gray>It must have a direct path to the <white>sky<gray>, otherwise",
					"<gray>it will<red> explode<gray>.",
					"",
					Items.ItemTier.makeItemString(ItemTier.BASIC, "Multiblock Core")
			})
			.get();
	
	public DrugWorkbenchMultiblock() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), new SlimefunItemStack("DRUG_WORKBENCH_CORE", DRUG_WORKBENCH_CORE), RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				SlimefunItems.SILICON.asOne(), SlimefunItems.SILICON.asOne(), SlimefunItems.SILICON.asOne(),
				SlimefunItems.SILICON.asOne(), ItemRegistry.getRegistryItem("MACHINE_CORE_BASIC").getItem(), SlimefunItems.SILICON.asOne(),
				SlimefunItems.WITHER_PROOF_OBSIDIAN.asOne(), SlimefunItems.WITHER_PROOF_OBSIDIAN.asOne(), SlimefunItems.WITHER_PROOF_OBSIDIAN.asOne()
		});
		new DrugWorkbenchCasing();
		new DrugWorkbenchVent();
		
		fromStructureBuilder(new MultiblockStructureBuilder(getId())
			.cube(new MultiblockChoice(Color.GRAY, "FUME_SEALANT_BLOCK"), 1)
			.solidSquare(new MultiblockChoice(Color.ORANGE, "DRUG_WORKBENCH_VENT"), 1, 1)
			.at(new Vector(0, 0, -1), "AIR", null)
		);
		ItemRegistry.registerItem(this);
	}
	
	@Override
	protected void onRightClick(Player who, Block b) {
		BlockStorage.getInventory(b).open(who);
	}

	@Override
	protected void onMultiblockAssemble(@NotNull Player who, Location at) {
		who.sendMessage(FormatUtils.mm("<light_purple>Drug Workbench Assembled!</light_purple>"));
		BlockStorageHelper.set(at, "producing", String.valueOf(false));
		BlockStorageHelper.set(at, "work", String.valueOf(0));
		BlockStorageHelper.set(at, "recipe", "");
	}

	private boolean isProducing(Block core) {
		return BlockStorageHelper.getBoolean(core, "producing");
	}
	
	private void explode(Block core) {
		Bukkit.getScheduler().runTask(ANOSF.get(), () -> {
			core.getLocation().createExplosion(16F);
			BlockHelper.breakSlimefunBlock(core);
		});
	}
	
	@Override
	protected void onMultiblockTick(BlockMenu menu, Block core) {
		Location aboveLocation = core.getLocation().add(new Vector(0, 2, 0));
		for(int y = aboveLocation.getBlockY(); y<=aboveLocation.getBlockY()+16; y++) {
			Location newLocation = aboveLocation.clone().add(new Vector(0, y, 0));
			Block blockAt = newLocation.getBlock();
			if(!(blockAt.getType().equals(Material.AIR))) {
				explode(core);
			}
		}
		if(isProducing(core)) {
			work(menu, core, DrugWorkbenchRecipe.tryFindRecipe(BlockStorageHelper.getString(core, "recipe")));
		}
	}

	private void work(BlockMenu menu, Block core, @Nullable DrugWorkbenchRecipe recipe) {
		if(recipe == null) {
			return;
		}
		
		int work = BlockStorageHelper.getInteger(core, "work");
		
		ItemStack statusItem = menu.getItemInSlot(4);
		statusItem.editMeta(meta -> {
			meta.displayName(FormatUtils.mm(FormatUtils.makeMachineCompletion(work, recipe.getWork())));
		});
		
		core.getLocation().getWorld().spawnParticle(Particle.LARGE_SMOKE, core.getLocation().add(new Vector(RandomUtils.nextInt(0, 2)-1, 3, RandomUtils.nextInt(0, 2)-1)), 1);
		
		if(work >= recipe.getWork()) {
			menu.pushItem(recipe.getOutput().clone(), getOutputSlots());
			BlockStorageHelper.set(core, "producing", false);
			BlockStorageHelper.set(core, "work", 0);
			BlockStorageHelper.set(core, "recipe", "");
			return;
		}
		
		BlockStorageHelper.set(core, "work", work + 1);
	}

	private static final ItemStack CRAFT_ITEM = new ItemStackBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("<green>Click to craft!").get();
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
				45,46,52,53
		});
		preset.addItem(4, STATUS_ITEM);
		preset.addItem(49, CRAFT_ITEM);
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
				if(BlockStorageHelper.getBoolean(core, "producing")) {
					p.sendMessage(FormatUtils.mm("<gray>The workbench is already producing something."));
					return false;
				} else {
					DrugWorkbenchRecipe recipe = DrugWorkbenchRecipe.tryFindRecipe(getCraftingSlotContents(menu));
					if(recipe != null) {
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
				47,48,50,51
		};
	}

}
