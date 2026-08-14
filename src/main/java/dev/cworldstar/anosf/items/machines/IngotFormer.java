package dev.cworldstar.anosf.items.machines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.Nullable;

import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.recipes.DustExtractorRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.abstracts.AbstractTickingMenuBlock;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.interfaces.BlockStorageSerializable;
import dev.cworldstar.libs.cwlib.utils.BlockStorageHelper;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class IngotFormer extends AbstractTickingMenuBlock implements EnergyNetComponent {

	@Getter
	private List<DustExtractorRecipe> recipes;
	@Getter
	private int processingSpeed = 1;
	@Getter
	private final ItemTier tier;
	
	public static class IngotFormerRecipe {
		
		private static List<IngotFormerRecipe> RECIPES = new ArrayList<>();
		
		@Nullable
		public static IngotFormerRecipe getRecipeByInput(ItemStack input) {
			for(IngotFormerRecipe recipe : RECIPES) {
				if(recipe.getInput().isSimilar(input)) {
					return recipe;
				}
			}
			return null;
		}
		
		
		public static enum IngotFormerRecipeType {
			FURNACE,
			DUST
		}
		
		@Getter
		private IngotFormerRecipeType recipeType;
		@Getter
		private ItemStack input;
		@Getter
		private ItemStack output;
		@Getter
		private int ticks;
		@Getter
		private String id;
		
		public IngotFormerRecipe(ItemStack input, ItemStack output, int ticks, String id) {
			this.recipeType = IngotFormerRecipeType.DUST;
			this.input = input;
			this.output = output;
			this.ticks = ticks;
			this.id = id;
			
			RECIPES.add(this);
		}
		
		public IngotFormerRecipe(RecipeChoice input, ItemStack output, int ticks, String id) {
			
		}

		public static IngotFormerRecipe of(FurnaceRecipe fRecipe) {
			return new IngotFormerRecipe(fRecipe.getInputChoice(), fRecipe.getResult(), fRecipe.getCookingTime(), fRecipe.getKey().getKey());
		}
		
	}
	
	private static SlimefunItemStack createSFItem(String id, Material itemMaterial, ItemTier tier, String type, String name, String[] lore, int processingSpeed) {
		List<String> newLore = new ArrayList<String>();
		newLore.addAll(Arrays.asList(lore));
		newLore.add("<gray>Machine Efficiency: <aqua>" + String.valueOf(processingSpeed) + "<gray>x");
		newLore.add("");
		newLore.add("<gray>Creates ingots when given dust.");
		newLore.add("<aqua>Converts copper and iron dust into");
		newLore.add("<aqua>their Minecraft equivalents.");
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
					
			};
		default:
			return new ItemStack[0];
		}
	}
	
	public IngotFormer(
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

	@Override
	public int getCapacity() {
		return 12800;
	}

	private static final int[] WORK_SLOTS = new int[] {
			10,11,12,13,14,15,16
	};
	
	@Override
	protected void tick(Block b, BlockMenu menu) {
		for(int slot : WORK_SLOTS) {
			process(b, menu, slot);
		}
	}

	private void process(Block b, BlockMenu menu, int slot) {
		ItemStack itemInSlot = menu.getItemInSlot(slot);
		if(itemInSlot == null) return;
		
		SlimefunItem item = SlimefunItem.getByItem(itemInSlot);
		
		if(!isProcessing(b)) {
			if(item == null) {
				// process vanilla furnace recipe
				Iterator<Recipe> iter = Bukkit.recipeIterator();
				
				while(iter.hasNext()) {
					Recipe recipe = iter.next();
					if((recipe instanceof FurnaceRecipe fRecipe)) {
						if(fRecipe.getInputChoice().test(itemInSlot)) {
							setProcessingRecipe(b, IngotFormerRecipe.of(fRecipe));
						}
					}
				}
			}
		}
	}

	private void setProcessingRecipe(Block b, IngotFormerRecipe recipe) {
		BlockStorageHelper.set(b, "Recipe", recipe);
		BlockStorageHelper.set(b,  "work", 0);
	}

	private boolean isProcessing(Block b) {
		return BlockStorageHelper.getString(b, "Recipe") != null;
	}

	@Override
	public void setup(BlockMenuPreset preset) {
		preset.drawBackground(new int[] {
				0,1,2,3,4,5,6,7,8,
				9,17,
				18,19,20,21,22,23,24,25,26,
				27,35,
				36,37,38,39,40,41,42,43,44
		});
	}

	@Override
	public int[] getInputSlots() {
		return new int[] {
				12
		};
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {
				14,15
		};
	}
}
