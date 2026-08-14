package dev.cworldstar.anosf.items.machines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.anosf.impl.radiation.ExtendedRadiationInfo;
import dev.cworldstar.anosf.impl.radiation.ExtendedRadiationInfo.RadiationSeverity;
import dev.cworldstar.anosf.impl.radiation.ExtendedRadiationInfo.RadiationType;
import dev.cworldstar.anosf.impl.radiation.RadiationEmitter;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.recipes.GeneratorRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.abstracts.AbstractTickingMenuBlock;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.utils.BlockStorageHelper;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import lombok.Getter;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class DecayGenerator extends AbstractTickingMenuBlock implements EnergyNetProvider, RadiationEmitter {
	
	@Getter
	private List<GeneratorRecipe> recipes;
	private BiFunction<Location, Config, Boolean> generate;
	private int perTickGeneration = 0;
	@Getter
	private int processingSpeed = 1;
	@Getter
	private final ItemTier tier;
	
	private static SlimefunItemStack createSFItem(String id, Material itemMaterial, ItemTier tier, String type, String name, String[] lore, int processingSpeed, int perTickGeneration) {
		List<String> newLore = new ArrayList<String>();
		newLore.addAll(Arrays.asList(lore));
		newLore.add("<gray>Base Power Generation: <aqua>" + String.valueOf(perTickGeneration) + "<yellow>J");
		newLore.add("<gray>Machine Efficiency: <aqua>" + String.valueOf(processingSpeed) + "<gray>x");
		newLore.add("");
		newLore.add("<gray>Generates power by decaying particles.");
		newLore.add("<red> Warning: may cause unexpected reality tears.");
		newLore.add("");
		newLore.add(tier.makeItemString(type));
		
		Material finalMaterial;
		
		switch(tier) {
		case STRANGE:
			finalMaterial = Material.PINK_GLAZED_TERRACOTTA;
			break;
		case HIGH:
			finalMaterial = Material.LIME_TERRACOTTA;
			break;
		case COSMIC:
			finalMaterial = Material.LIME_GLAZED_TERRACOTTA;
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
	
	public ItemStack[] getRecipe(ItemTier tier) {
		switch(tier) {
			case STRANGE:
				return new ItemStack[] { 
						
				};
			case HIGH:
				return new ItemStack[] { 
						
				};
			case COSMIC:
				return new ItemStack[] { 
						
				};
			default:
				return new ItemStack[] {
						
				};
		}
	}
	
	public DecayGenerator(
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
			int perTickGeneration,
			BiFunction<Location, Config, Boolean> canGenerate,
			List<GeneratorRecipe> recipes
	) {
		super(
				ItemRegistry.getItemGroup("MACHINE_CATEGORY"), 
				createSFItem(id, itemMaterial, tier, itemType, itemName, lore, processingSpeed, perTickGeneration), 
				recipeType, 
				recipe
		);
		
		this.tier = tier;
		this.recipes = recipes;
		this.generate = canGenerate;
		this.perTickGeneration = perTickGeneration;
		Validate.isTrue(Math.signum(processingSpeed) != -1D);
		this.processingSpeed = processingSpeed;
	}
	@Override
	public EnergyNetComponentType getEnergyComponentType() {
		return EnergyNetComponentType.GENERATOR;
	}
	
	private static final ItemStack BACKGROUND_ITEM = ItemStackBuilder.create(Material.BLACK_STAINED_GLASS_PANE).empty().item();
	private static final ItemStack INPUT_BACKGROUND_ITEM = ItemStackBuilder.create(Material.LIME_STAINED_GLASS_PANE).name("<green>Input Slot").item();
	private static final ItemStack OUTPUT_BACKGROUND_ITEM = ItemStackBuilder.create(Material.RED_STAINED_GLASS_PANE).name("<red>Output Slot").item();
	private static final ItemStack STATUS_ITEM = ItemStackBuilder.create(Material.GREEN_STAINED_GLASS_PANE).name("<gray>Status: Idle").item();
	private static final ItemStack NOT_RUNNING_ITEM = ItemStackBuilder.create(Material.RED_STAINED_GLASS_PANE).name("<gray>Status: Idle").item();

	
	@Override
	public void setup(BlockMenuPreset preset) {
		preset.drawBackground(BACKGROUND_ITEM, new int[] {
				0,2,3,4,5,6,8,
				12,14,
				18,20,21,22,23,24,26
		});
		preset.drawBackground(INPUT_BACKGROUND_ITEM, new int[] {
				1,9,11,19
		});
		preset.drawBackground(OUTPUT_BACKGROUND_ITEM, new int[] {
				7,15,17,25
		});
		preset.addItem(13, STATUS_ITEM, ChestMenuUtils.getEmptyClickHandler());
	}

	@Override
	public int[] getInputSlots() {
		return new int[] {
				10
		};
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {
				16
		};
	}

	@Override
	public int getGeneratedOutput(Location l, Config data) {
		boolean isGenerating = BlockStorageHelper.getBoolean(l, "active");
		boolean canGenerate = generate.apply(l, data).booleanValue();
		if(isGenerating && canGenerate) {
			String recipeID = BlockStorageHelper.getString(l, "recipe");
			GeneratorRecipe recipe = getRecipe(recipeID);
			if(recipe == null) {
				return 0;
			}
			return ((perTickGeneration + recipe.getPowerPerTick()) * processingSpeed);
		} else {
			return 0;
		}
	}

	@Override
	public void onNewInstance(BlockMenu menu, Block b) {
		
	}
	
	@Override
	public void oplace(BlockPlaceEvent e, BlockMenu menu) {
		BlockStorageHelper.set(e.getBlock().getLocation(), "active", false);
		BlockStorageHelper.set(e.getBlock().getLocation(), "ticks", 0);
		BlockStorageHelper.set(e.getBlock().getLocation(), "recipe", "");
	}
	
	@Override
	public int getCapacity() {
		return 0;
	}

	@Nullable
	public GeneratorRecipe getRecipe(@NotNull String recipeID) {
		for(GeneratorRecipe recipe : recipes) {
			if(recipe.getRecipeID().equals(recipeID)) {
				return recipe;
			}
		}
		return null;
	}
	
	@Override
	protected void tick(Block b, BlockMenu menu) {
		ItemStack inputItem = menu.getItemInSlot(getInputSlots()[0]);
		ItemStack statusItem = menu.getItemInSlot(13);
		
		if(
				inputItem != null && 
				!BlockStorageHelper.getBoolean(b.getLocation(), "active")
		) {
			// search for generator recipe
			for(GeneratorRecipe recipe : recipes) {
				if(recipe.matches(inputItem)) {
					// we have a match, mark active & set ticks
					inputItem.subtract();
					BlockStorageHelper.set(b.getLocation(), "active", true);
					BlockStorageHelper.set(b.getLocation(), "ticks", 0);
					BlockStorageHelper.set(b.getLocation(), "recipe", recipe.getRecipeID());

				}
			}
		} else if(BlockStorageHelper.getBoolean(b.getLocation(), "active")) {
			if(!(statusItem.getType() == Material.GREEN_STAINED_GLASS_PANE)) {
				menu.replaceExistingItem(13, STATUS_ITEM);
			}
			int ticks = BlockStorageHelper.getInteger(b.getLocation(), "ticks");
			String recipeID = BlockStorageHelper.getString(b.getLocation(), "recipe");
			GeneratorRecipe recipe = getRecipe(recipeID);
			Validate.notNull(recipe, "Attempted to lookup GeneratorRecipe " + recipeID + ", it did not exist.");
			ItemMeta meta = statusItem.getItemMeta();
			String completed = FormatUtils.makeMachineCompletion(ticks, recipe.getLastingTicks());
			meta.displayName(FormatUtils.mm("<gray>Decaying."));
			meta.lore(FormatUtils.lore(new String[] {
					"<gray> Ticks: " + completed + "<gray> ( <green>" + ticks + "<gray> / <red>"+ recipe.getLastingTicks() + "<gray> )",
					"<gray> Processing Recipe: <aqua>" + recipeID.replace("_", " ") + "<gray>.",
					"<gray> Per Tick Generation: <aqua>" + String.valueOf(((perTickGeneration + recipe.getPowerPerTick()) * processingSpeed)),
					"<gray> Machine Tier: " + getTier().identifier() + "<gray>.",
					"<gray> Machine Efficiency: <aqua>" + String.valueOf(getProcessingSpeed()) + "</aqua>x"
			}));
			statusItem.setItemMeta(meta);
			
			BlockStorageHelper.set(b.getLocation(), "ticks", ticks + 1);
			
			if(ticks >= recipe.getLastingTicks()) {
				// recipe done, reset and consume next
				if(recipe.hasOutput()) {
					menu.pushItem(recipe.getOutput().clone(), getOutputSlots());
				}
				BlockStorageHelper.set(b.getLocation(), "active", false);
				BlockStorageHelper.set(b.getLocation(), "ticks", 0);
				BlockStorageHelper.set(b.getLocation(), "recipe", "");
			}
		} else {
			if(!(statusItem.getType() == Material.RED_STAINED_GLASS_PANE)) {
				menu.replaceExistingItem(13, NOT_RUNNING_ITEM);
			}
		}
	}

	@Override
	public ExtendedRadiationInfo getRadiationInfo() {
		return new ExtendedRadiationInfo(RadiationType.GAMMA_RADIATION, RadiationSeverity.CERTAIN_DEATH);
	}

	@Override
	public int getStrength() {
		return 8;
	}

}
