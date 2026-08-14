package dev.cworldstar.anosf.items.machines.multiblocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.accelerator.AcceleratorCasing;
import dev.cworldstar.anosf.items.machines.accelerator.AcceleratorCasingGlass;
import dev.cworldstar.anosf.items.machines.accelerator.AcceleratorController;
import dev.cworldstar.anosf.items.machines.accelerator.AcceleratorGlass;
import dev.cworldstar.anosf.items.machines.accelerator.AcceleratorMagnet;
import dev.cworldstar.anosf.items.machines.accelerator.AcceleratorSynchrotron;
import dev.cworldstar.anosf.items.recipes.ParticleAcceleratorRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.builders.MultiblockStructureBuilder;
import dev.cworldstar.libs.cwlib.impl.multiblock.MultiblockChoice;
import dev.cworldstar.libs.cwlib.impl.multiblock.MultiblockCore;
import dev.cworldstar.libs.cwlib.utils.BlockStorageHelper;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.math.RandomUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class ParticleAcceleratorMultiblock extends MultiblockCore implements EnergyNetComponent {
	
	private static HashMap<Location, List<Vector>> VECTOR_CACHE = new HashMap<Location, List<Vector>>();
	
	private static final ItemStack ACCELERATOR_CORE = new ItemStackBuilder(Material.REDSTONE_BLOCK)
			.setName("<gradient:gray:aqua:gray:aqua:gray>Accelerator Core")
			.setLore(new String[] {
					"",
					"<gradient:gray:aqua><italic>\"The start of something",
					"<gradient:gray:aqua><italic>beautiful.\"</gradient> <gray>- changelater, 20XX",
					"",
					Items.ItemTier.makeItemString(ItemTier.HIGH, "Multiblock Core")
			})
			.get();
	
	public static ItemStack getAcceleratorCoreItem() {
		return ACCELERATOR_CORE;
	}
	
	public ParticleAcceleratorMultiblock() {
		super(ItemRegistry.getItemGroup("MACHINE_CATEGORY"), 
				new SlimefunItemStack("ACCELERATOR_CORE", ACCELERATOR_CORE),
				RecipeType.ENHANCED_CRAFTING_TABLE,
				null
		);
		
		new AcceleratorMagnet();
		new AcceleratorCasing();
		new AcceleratorGlass();
		new AcceleratorCasingGlass();
		new AcceleratorController();
		new AcceleratorSynchrotron();
		
		fromStructureBuilder(new MultiblockStructureBuilder("ACCELERATOR_CORE")
				// foundation
				.square(new MultiblockChoice(Color.fromRGB(222,184,135), "ACCELERATOR_MAGNET", "ACCELERATOR_GLASS"), 0, 16)
				.square(new MultiblockChoice(Color.fromRGB(222,184,135), "ACCELERATOR_MAGNET", "ACCELERATOR_GLASS"), 0, 18)
				.square(new MultiblockChoice(Color.fromRGB(222,184,135), "ACCELERATOR_MAGNET", "ACCELERATOR_GLASS"), 1, 17)
				.square(new MultiblockChoice(Color.fromRGB(222,184,135), "ACCELERATOR_MAGNET", "ACCELERATOR_GLASS"), -1, 17)
				// casing
				.square(new MultiblockChoice(Color.BLACK, "ACCELERATOR_CASING", "ACCELERATOR_CASING_GLASS"), 0, 19)
				.square(new MultiblockChoice(Color.BLACK, "ACCELERATOR_CASING", "ACCELERATOR_CASING_GLASS"), 0, 15)
				.square(new MultiblockChoice(Color.BLACK, "ACCELERATOR_CASING", "ACCELERATOR_CASING_GLASS"), -2, 17)
				.square(new MultiblockChoice(Color.BLACK, "ACCELERATOR_CASING", "ACCELERATOR_CASING_GLASS"), 2, 17)
				// casing sides
				.square(new MultiblockChoice(Color.BLACK, "ACCELERATOR_CASING", "ACCELERATOR_CASING_GLASS"), 1, 18)
				.square(new MultiblockChoice(Color.BLACK, "ACCELERATOR_CASING", "ACCELERATOR_CASING_GLASS"), -1, 18)
				.square(new MultiblockChoice(Color.BLACK, "ACCELERATOR_CASING", "ACCELERATOR_CASING_GLASS"), 1, 16)
				.square(new MultiblockChoice(Color.BLACK, "ACCELERATOR_CASING", "ACCELERATOR_CASING_GLASS"), -1, 16)
				// prepare area for accelerator synchrotrons
				.at(new Vector(-17, 0, -18), "AIR", Color.WHITE)
				.at(new Vector(-17, 0, -19), "AIR", Color.WHITE)
				.at(new Vector(-17, 0, -20), "AIR", Color.WHITE)
				.at(new Vector(-17, 0, -21), "AIR", Color.WHITE)
				// create square here
				.at(new Vector(-16, 1, -19), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, 1, -19), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-18, 1, -19), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-16, 0, -19), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-18, 0, -19), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-16, -1, -19), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, -1, -19), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, -2, -19), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, 2, -19), "ACCELERATOR_CASING", Color.BLACK)
				
				// square 2
				.at(new Vector(-16, 1, -20), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, 1, -20), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-18, 1, -20), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-16, 0, -20), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-18, 0, -20), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-16, -1, -20), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, -1, -20), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-18, -1, -20), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, -2, -20), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, 2, -20), "ACCELERATOR_CASING", Color.BLACK)
				
				// square 3
				.at(new Vector(-16, 1, -21), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, 1, -21), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-18, 1, -21), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-16, 0, -21), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-18, 0, -21), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-16, -1, -21), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, -1, -21), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-18, -1, -21), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, -2, -21), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, 2, -21), "ACCELERATOR_CASING", Color.BLACK)
				
				// star 4
				.at(new Vector(-17, 1, -22), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-17, -1, -22), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-16, 0, -22), "ACCELERATOR_CASING", Color.BLACK)
				.at(new Vector(-18, 0, -22), "ACCELERATOR_CASING", Color.BLACK)
				
				// accelerator synchrotron
				.at(new Vector(-17, 0, -22), "ACCELERATOR_SYNCHROTRON", Color.SILVER)
				
				// last piece
				.at(new Vector(-17, 0, -23), "ACCELERATOR_CASING", Color.BLACK)
				
				.at(new Vector(-19,0,0), "ACCELERATOR_CONTROLLER", Color.FUCHSIA)
		);
		ItemRegistry.registerItem(this);
	}

	
	private void buildVectorCache(Location core) {
		ArrayList<Vector> vectorCache = new ArrayList<Vector>();
		ArrayList<Vector> firstLine = MultiblockStructureBuilder.lineXZ(new Vector(-17,0,-17), new Vector(-17,0,17));
		vectorCache.addAll(firstLine);
		ArrayList<Vector> secondLine = MultiblockStructureBuilder.lineXZ(new Vector(-16,0,17), new Vector(17,0,17));
		vectorCache.addAll(secondLine);
		ArrayList<Vector> thirdLine = MultiblockStructureBuilder.lineXZ(new Vector(17,0,16), new Vector(17,0,-17));
		vectorCache.addAll(thirdLine);
		ArrayList<Vector> finalLine = MultiblockStructureBuilder.lineXZ(new Vector(16,0,-17), new Vector(-16,0,-17));
		vectorCache.addAll(finalLine);
		
		VECTOR_CACHE.put(core, vectorCache);
	}
	
	@Override
	protected void onMultiblockAssemble(Player who, Location at) {
		buildVectorCache(at);
		who.sendMessage(FormatUtils.mm("<light_purple>Particle accelerator assembled!</light_purple>"));
		BlockStorageHelper.set(at, "producing", String.valueOf(false));
		BlockStorageHelper.set(at, "speed", String.valueOf(1));
		BlockStorageHelper.set(at, "revolutions", String.valueOf(1));
		BlockStorageHelper.set(at, "work", String.valueOf(0));
		BlockStorageHelper.set(at, "recipe", "");
		BlockStorageHelper.set(at, "particlePosition", 0);
	}
	
	private void reset(Location at) {
		BlockStorageHelper.set(at, "producing", String.valueOf(false));
		BlockStorageHelper.set(at, "speed", String.valueOf(1));
		BlockStorageHelper.set(at, "revolutions", String.valueOf(1));
		BlockStorageHelper.set(at, "work", String.valueOf(0));
		BlockStorageHelper.set(at, "recipe", "");
		BlockStorageHelper.set(at, "particlePosition", 0);
	}
	
	private boolean isProducing(Block core) {
		return BlockStorageHelper.booleanFromJSON(BlockStorage.getBlockInfoAsJson(core), "producing");
	}
	
	private void markProducing(Block core, ParticleAcceleratorRecipe recipe) {
		BlockStorage.addBlockInfo(core, "producing", String.valueOf(true));
		BlockStorage.addBlockInfo(core, "recipe", recipe.getRecipeId());

	}
	
	private float getWork(Block at) {
		return BlockStorageHelper.getFloat(at, "work");
	}
	
	private void addWork(Block at, float f) {
		BlockStorageHelper.set(at.getLocation(), "work", getWork(at) + f);
	}
	
	/*[[private Vector getNextPosition(Vector currentPosition) {
		Vector toReturn = new Vector(0,0,0);
		
		if(
			currentPosition.getX
		) {
			
		}
		
		return toReturn;
	}*/
	
	private static final ItemStack NO_POWER_ITEM = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setName("<red>Not Enough Power!").get();
	private static final ItemStack IDLE_ITEM = new ItemStackBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("<gray>Accelerator Idle.").get();

	private static final float SLOWED_PLANK_CONSTANT = 6.62607015F; // we don't multiply by 10^-28 because we want whole numbers
	
	private float cubic(float origin) {
		return (float) (1-Math.pow(((-2 * origin) + 2), 3) / 2);
	}
	
	private float getSpeed(float work, float momentum) {
		return ( // here we calculate the speed for the particle.
				cubic((SLOWED_PLANK_CONSTANT * (float) Math.exp((double) momentum)) / 10)
		);
	}
	
	private void work(BlockMenu menu, Block core, ParticleAcceleratorRecipe recipe) {
		if(BlockStorageHelper.getInteger(core, "particlePosition") == 0) {
			BlockStorageHelper.set(core, "particlePosition", 0);
		}
		
		removeCharge(core.getLocation(), getCapacity() / 10);
		
		float work = getWork(core);
		if(work >= recipe.getRequiredWork()) {
			complete(menu, core, recipe);
			menu.replaceExistingItem(22, IDLE_ITEM);
			reset(core.getLocation());
		} else {
			float particleSpeed = getSpeed(work, (float) ((float) work * 0.001));
			if(particleSpeed > 20) {
				particleSpeed = 20F; // cap the speed at 20 for performance
			}
			addWork(core, 1 * particleSpeed);
			
			// mimic the particle "speeding up." since we don't want to tick accelerate, this is the best way.
			for(float speed = 0; speed <= Math.floor(particleSpeed); speed++) {
				displayParticlePosition(core); 
			}
			
			
			double workCompletedPercent = ((double) work) / recipe.getRequiredWork();
			String processItemName = "||||||||||||";
			int substr = (int) Math.round(processItemName.length() * workCompletedPercent);
			String completed = "<green>" + processItemName.substring(0, substr) + "<red>";
			completed.replaceAll("|", "I");
			for(int x=substr; x<= processItemName.length(); x++) {
				completed = completed + "|";
			}
			ItemStack item = menu.getItemInSlot(22);
			if(item.getType() != Material.LIME_STAINED_GLASS_PANE) {
				item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
				menu.replaceExistingItem(22, item);
			}
			ItemMeta meta = item.getItemMeta();
			meta.displayName(FormatUtils.mm("<yellow>Working. <gray>Completed: " + completed + "<gray> (<green>" + String.valueOf(work) + " <gray>/ <red>" + String.valueOf(recipe.getRequiredWork()) + "<gray>)"));
			meta.lore(FormatUtils.loreComponent(Arrays.asList(new String[] {
					"<red>Left-Click<gray> to cancel current recipe.",
					"",
					"<gray>Processing Recipe:<aqua> " + recipe.getRecipeId().replace("_", " ") + "</aqua>.",
					"<gray>Particle Speed: <aqua>" + String.valueOf(particleSpeed),
			})));
			item.setItemMeta(meta);
		}
	}
	
	private void displayParticlePosition(Block core) {	
		int particleLocation = BlockStorageHelper.getInteger(core, "particlePosition");
		List<Vector> vectorCache = VECTOR_CACHE.get(core.getLocation());
		final Location relativeLocation = core.getLocation();
		
		if(particleLocation+1 > vectorCache.size()) {
			particleLocation = 0;
		}
		
		Vector nextVector = vectorCache.get(particleLocation);
		BlockStorageHelper.set(core, "particlePosition", particleLocation + 1);
		
		Location newLocation = relativeLocation.add(nextVector);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				core.getWorld().playSound(newLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 0.2F, RandomUtils.nextFloat());
				core.getWorld().playSound(newLocation, Sound.BLOCK_END_PORTAL_SPAWN, 0.2F, RandomUtils.nextFloat());
				core.getWorld().spawnParticle(Particle.DUST, newLocation, 6, new DustOptions(Color.WHITE, 8));
				core.getWorld().spawnParticle(Particle.PORTAL, newLocation, 6);
			}
		}.runTask(ANOSF.get());
	}

	private void complete(BlockMenu menu, Block core, ParticleAcceleratorRecipe recipe) {
		ItemStack item = recipe.getOutputItem();
		if(item == null) {
			return;
		}
		ItemStack fit = menu.pushItem(item.clone(), getOutputSlots());
		if(fit != null) {
			core.getWorld().dropItem(core.getLocation(), item.clone());
		}
	}

	@Override
	protected void onMultiblockTick(BlockMenu menu, Block core) {
		
		if(getCharge(core.getLocation()) < (getCapacity()/10)) {
			if(!menu.getItemInSlot(22).isSimilar(NO_POWER_ITEM)) {
				menu.replaceExistingItem(22, NO_POWER_ITEM);
			}
			return;
		}
		
		if(!isProducing(core)) {
			ItemStack item = menu.getItemInSlot(46);
			ItemStack secondaryItem = menu.getItemInSlot(52);
			if(item == null) {
				return;
			}
			ParticleAcceleratorRecipe recipe = ParticleAcceleratorRecipe.lookup(item, secondaryItem);
			if(recipe != null) {
				Location loc = core.getLocation().add(new Vector(-17, 0, -22));
				Bukkit.getScheduler().runTask(ANOSF.get(), (task) -> {
					loc.createExplosion(16F);
				});

				markProducing(core, recipe);
				item.subtract();
				if(secondaryItem != null) {
					secondaryItem.subtract();
				}
				work(menu, core, recipe);
			}
		} else {
			work(menu, core, ParticleAcceleratorRecipe.lookup(BlockStorageHelper.stringFromJSON(BlockStorage.getBlockInfoAsJson(core), "recipe")));
		}
	}

	@Override
	public EnergyNetComponentType getEnergyComponentType() {
		return EnergyNetComponentType.CONSUMER;
	}

	@Override
	public int getCapacity() {
		return 2000000000;
	}

	private static final ItemStack BLACK_GLASS = new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).empty().get();
	private static final ItemStack RED_GLASS = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).name("<gradient:gray:white:gray>Output").get();
	private static final ItemStack ORANGE_GLASS = new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).name("<gradient:white:gray>Right Collision Input").get();
	private static final ItemStack GREEN_GLASS = new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE).name("<gradient:gray:white>Left Collision Input").get();
	
	@Override
	public void onNewInstance(BlockMenu menu, Block core) {
		buildVectorCache(core.getLocation());
		menu.addMenuClickHandler(22, new AdvancedMenuClickHandler() {
			@Override
			public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
				e.setCancelled(true);
				if(isProducing(core)) {
					p.sendMessage(FormatUtils.mm("<red>The current recipe has been cancelled."));
					cancel(menu, core);
				}
				return false;
			}

			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				return false;
			}			
		});
	}
	
	private void cancel(BlockMenu menu, Block core) {
		menu.replaceExistingItem(22, IDLE_ITEM);
		reset(core.getLocation());		
	}

	@Override
	public void setup(BlockMenuPreset preset) {
		preset.drawBackground(BLACK_GLASS, new int[] {
				0,1,2,3,4,5,6,7,8,
				9,10,11,12,13,14,15,16,17,
				18,19,20,21,23,24,25,26,
				27,28,29,30,31,32,33,34,35,
				36,37,38,39,40,41,42,43,44,
		});
		preset.drawBackground(RED_GLASS, new int[] {
				48, 50
		});
		preset.drawBackground(ORANGE_GLASS, new int[] {
				51, 53
		});
		preset.drawBackground(GREEN_GLASS, new int[] {
				45, 47
		});
		preset.addItem(22, IDLE_ITEM.clone());
	}

	public int getStatusSlot() {
		return 22;
	}
	
	@Override
	public int[] getInputSlots() {
		return new int[] {
				46, 52
		};
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {
				49
		};
	}

}
