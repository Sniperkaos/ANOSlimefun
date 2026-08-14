package dev.cworldstar.anosf.items.machines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.recipes.DustExtractorRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.abstracts.AbstractTickingMenuBlock;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.utils.BlockStorageHelper;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class DustExtractor extends AbstractTickingMenuBlock implements EnergyNetComponent {
	
	@Getter
	private List<DustExtractorRecipe> recipes;
	@Getter
	private int processingSpeed = 1;
	@Getter
	private final ItemTier tier;
	
	private static SlimefunItemStack createSFItem(String id, Material itemMaterial, ItemTier tier, String type, String name, String[] lore, int processingSpeed) {
		List<String> newLore = new ArrayList<String>();
		newLore.addAll(Arrays.asList(lore));
		newLore.add("<gray>Machine Efficiency: <aqua>" + String.valueOf(processingSpeed) + "<gray>x");
		newLore.add("");
		newLore.add("<gray>Creates dust by processing cobblestone.");
		newLore.add("<aqua>Produces special dust types.");
		newLore.add("");
		newLore.add(tier.makeItemString(type));
		
		Material finalMaterial;
		
		switch(tier) {
		case STRANGE:
			finalMaterial = Material.PURPLE_CONCRETE;
			break;
		case HIGH:
			finalMaterial = Material.RED_CONCRETE;
			break;
		case COSMIC:
			finalMaterial = Material.WHITE_CONCRETE;
			break;
		case BASIC:
		case ADVANCED:
		case ELITE:
			finalMaterial = itemMaterial;
		default:
			finalMaterial = itemMaterial;
		}
		
		return new SlimefunItemStack(id, new ItemStackBuilder(finalMaterial)
				.name(tier.makeName(name))
				.lore(newLore.stream().toArray(String[]::new))
				.item()
		);
	}
	
	private static ItemStack[] createRecipe(ItemTier tier) {
		switch(tier) {
		case ELITE:
			return new ItemStack[] {
				ItemRegistry.get("HEAVY_CHROME_ALLOY_PLATE"), ItemRegistry.get("HALYNIX_CIRCUIT_BOARD"), ItemRegistry.get("HEAVY_CHROME_ALLOY_PLATE"), 
				ItemRegistry.get("HALYNIX_CIRCUIT_BOARD"), ItemRegistry.get("MACHINE_CORE_ELITE"), ItemRegistry.get("HALYNIX_CIRCUIT_BOARD"), 
				ItemRegistry.get("HEAVY_CHROME_ALLOY_PLATE"), ItemRegistry.get("HALYNIX_CIRCUIT_BOARD"), ItemRegistry.get("HEAVY_CHROME_ALLOY_PLATE"), 
			};
		case HIGH:
			return new ItemStack[] {
				ItemRegistry.get("BLACKENED_CHROME_ALLOY_PLATE"), ItemRegistry.get("HIGH_CIRCUIT"), ItemRegistry.get("BLACKENED_CHROME_ALLOY_PLATE"), 
				ItemRegistry.get("HIGH_CIRCUIT"), ItemRegistry.get("MACHINE_CORE_HIGH"), ItemRegistry.get("HIGH_CIRCUIT"), 
				ItemRegistry.get("BLACKENED_CHROME_ALLOY_PLATE"), ItemRegistry.get("HIGH_CIRCUIT"), ItemRegistry.get("BLACKENED_CHROME_ALLOY_PLATE"), 
			};
		case STRANGE:
		case COSMIC:
		default:
			return new ItemStack[0];
		}
	}
	
	public DustExtractor(
			ItemGroup category, 
			Material itemMaterial,
			ItemTier tier,
			String id,
			String itemType,
			String itemName,
			String[] lore,
			RecipeType recipeType, 
			int processingSpeed,
			ItemStack[] recipe,
			List<DustExtractorRecipe> recipes
	) {
		super(
				ItemRegistry.getItemGroup("MACHINE_CATEGORY"), 
				createSFItem(id, itemMaterial, tier, itemType, itemName, lore, processingSpeed), 
				recipeType, 
				createRecipe(tier)
		);
		
		this.tier = tier;
		this.recipes = recipes;
		Validate.isTrue(Math.signum(processingSpeed) != -1D);
		this.processingSpeed = processingSpeed;
	}
	@Override
	public EnergyNetComponentType getEnergyComponentType() {
		return EnergyNetComponentType.CONSUMER;
	}
	
	private static final ItemStack BACKGROUND_ITEM = ItemStackBuilder.create(Material.BLACK_STAINED_GLASS_PANE).empty().item();
	private static final ItemStack INPUT_BACKGROUND_ITEM = ItemStackBuilder.create(Material.LIME_STAINED_GLASS_PANE).name("<green>Input Slots").item();
	private static final ItemStack OUTPUT_BACKGROUND_ITEM = ItemStackBuilder.create(Material.RED_STAINED_GLASS_PANE).name("<red>Output Slots").item();
	private static final ItemStack STATUS_ITEM	 = ItemStackBuilder.create(Material.GREEN_STAINED_GLASS_PANE).name("<gray>Status: Idle").item();
	private static final ItemStack NOT_RUNNING_ITEM = ItemStackBuilder.create(Material.RED_STAINED_GLASS_PANE).name("<gray>Status: Idle").item();
	private static final ItemStack NO_POWER_ITEM = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setName("<red>Not Enough Power!").get();
	private static final ItemStack OUTPUT_SLOTS_FULL = new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("<red>No Output Slots!").get();

	
	@Override
	public void setup(BlockMenuPreset preset) {
		preset.drawBackground(BACKGROUND_ITEM, new int[] {
				0,1,2,3,4,5,6,7,8,
				
				18,19,20,21,22,23,24,25,26
		});
		preset.drawBackground(INPUT_BACKGROUND_ITEM, new int[] {
				9,12
		});
		preset.drawBackground(OUTPUT_BACKGROUND_ITEM, new int[] {
				14,17
		});
		preset.addItem(13, STATUS_ITEM, ChestMenuUtils.getEmptyClickHandler());
	}

	@Override
	public int[] getInputSlots() {
		return new int[] {
				10,11
		};
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {
				15,16
		};
	}

	@Override
	public void onNewInstance(BlockMenu menu, Block b) {
		
	}
	
	@Override
	public void oplace(BlockPlaceEvent e, BlockMenu menu) {
		BlockStorageHelper.set(e.getBlock().getLocation(), "active", false);
		BlockStorageHelper.set(e.getBlock().getLocation(), "ticks", 0);
		BlockStorageHelper.set(e.getBlock().getLocation(), "pushing", "");
		BlockStorageHelper.set(e.getBlock().getLocation(), "recipe", "");
	}
	
	@Override
	public int getCapacity() {
		return 1700 * (2 * processingSpeed);
	}

	@Nullable
	public DustExtractorRecipe getRecipe(@NotNull String recipeID) {
		for(DustExtractorRecipe recipe : recipes) {
			if(recipe.getRecipeID().equals(recipeID)) {
				return recipe;
			}
		}
		return null;
	}
	
	public ItemStack[] getItemsInSlots(BlockMenu menu, int[] slots) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for(int slot : slots) {
			items.add(menu.getItemInSlot(slot));
		}
		return items.toArray(new ItemStack[0]);
	}
	
	@Override
	protected void tick(Block b, BlockMenu menu) {
		ItemStack[] inputItems = getItemsInSlots(menu, getInputSlots());
		ItemStack statusItem = menu.getItemInSlot(13);
	
		if(
				inputItems != null && 
				!BlockStorageHelper.getBoolean(b.getLocation(), "active")
		) {
			// search for generator recipe
			boolean found = false;
			
			for(DustExtractorRecipe recipe : recipes) {
				for(ItemStack inputItem : inputItems) {
					if(inputItem == null) {
						continue;
					}
					if(recipe.matches(inputItem)) {
						found = true;
						// we have a match, mark active & set ticks
						inputItem.subtract();
						BlockStorageHelper.set(b.getLocation(), "active", true);
						BlockStorageHelper.set(b.getLocation(), "recipe", recipe.getRecipeID());
						break;
					}
				}
				if(found) {
					break;
				}
			}
		} else if(BlockStorageHelper.getBoolean(b.getLocation(), "active")) {

			int ticks = BlockStorageHelper.getInteger(b.getLocation(), "ticks");
			String recipeID = BlockStorageHelper.getString(b.getLocation(), "recipe");
			DustExtractorRecipe recipe = getRecipe(recipeID);
			Validate.notNull(recipe, "Attempted to lookup DustExtractorRecipe " + recipeID + ", it did not exist.");
			
			if(getCharge(b.getLocation()) < recipe.getPowerPerTick()) {
				if(!menu.getItemInSlot(13).getType().equals(Material.RED_STAINED_GLASS_PANE)) {
					ItemStack item = NO_POWER_ITEM.clone();
					item.editMeta(meta -> {
						meta.lore(FormatUtils.lore(new String[] {
								"<red>No Power!",
								"",
								"<gray>Required Power: " + "<red>" + String.valueOf(recipe.getPowerPerTick()),
								"<gray>Current Power: " + "<red>" + String.valueOf(getCharge(b.getLocation()))
						}));
					});
					menu.replaceExistingItem(13, NO_POWER_ITEM);
				}
				return;
			}
			
			if(ticks >= recipe.getRequiredWork()) {				
				ItemStack toPush = recipe.getOutputItems()[RandomUtils.nextInt(0, recipe.getOutputItems().length)].clone();
				SlimefunItem item = SlimefunItem.getById(BlockStorageHelper.getString(b, "pushing"));
				if(item != null) {
					toPush = item.getItem();
				} else {
					BlockStorageHelper.set(b.getLocation(), "pushing", SlimefunItem.getByItem(toPush).getId());
				}
				
				boolean hasRoom = menu.fits(toPush, getOutputSlots());
				if(!hasRoom) {
					if(!menu.getItemInSlot(13).getType().equals(Material.ORANGE_STAINED_GLASS_PANE)) {
						menu.replaceExistingItem(13, OUTPUT_SLOTS_FULL);
					}
					return;
				}
				


				menu.pushItem(toPush.clone(), getOutputSlots());
				BlockStorageHelper.set(b.getLocation(), "active", false);
				BlockStorageHelper.set(b.getLocation(), "ticks", ticks - recipe.getRequiredWork());
				BlockStorageHelper.set(b.getLocation(), "recipe", "");
				BlockStorageHelper.set(b.getLocation(), "pushing", "");
			} else {
				if(!(statusItem.getType() == Material.GREEN_STAINED_GLASS_PANE)) {
					menu.replaceExistingItem(13, STATUS_ITEM);
				}
				removeCharge(b.getLocation(), recipe.getPowerPerTick());
				
				ItemMeta meta = statusItem.getItemMeta();
				String completed = FormatUtils.makeMachineCompletion(ticks, recipe.getRequiredWork());
				meta.displayName(FormatUtils.mm("<gray>Extracting Dust."));
				meta.lore(FormatUtils.lore(new String[] {
						"<gray> Ticks: " + completed + "<gray> ( <green>" + ticks + "<gray> / <red>"+ recipe.getRequiredWork() + "<gray> )",
						"<gray> Processing Recipe: <aqua>" + recipeID.replace("_", " ") + "<gray>.",
						"<gray> Machine Tier: " + getTier().identifier() + "<gray>.",
						"<gray> Machine Efficiency: <aqua>" + String.valueOf(getProcessingSpeed()) + "</aqua>x"
				}));
				statusItem.setItemMeta(meta);
				
				BlockStorageHelper.set(b.getLocation(), "ticks", ticks + (1*processingSpeed));
			}
		} else {
			if(!(statusItem.getType() == Material.RED_STAINED_GLASS_PANE)) {
				menu.replaceExistingItem(13, NOT_RUNNING_ITEM);
			}
		}
	}
}
