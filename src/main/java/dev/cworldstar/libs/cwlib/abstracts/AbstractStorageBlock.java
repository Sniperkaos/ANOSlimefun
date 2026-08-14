package dev.cworldstar.libs.cwlib.abstracts;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

import dev.cworldstar.libs.cwlib.utils.BlockStorageHelper;

public class AbstractStorageBlock extends AbstractTickingMenuBlock {
	
	public AbstractStorageBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType,
			ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe);
	}

	@Override
	public void setup(BlockMenuPreset preset) {
		preset.setSize(27);
		preset.drawBackground(new int[] {
				0,1,2,3,4,5,6,7,8,
				9,   12,13,16,17,
				18,19,20,21,22,23,24,25,26
		});
	}

	@Override
	public int[] getInputSlots() {
		return new int[] {10,11};
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {14,15};
	}

	@Override
	protected void tick(Block b, BlockMenu menu) {
		for(int slot : getInputSlots()) {
			ItemStack stack = menu.getItemInSlot(slot);
			if(SlimefunUtils.isItemSimilar(stack, BlockStorageHelper.getItemStack(b, "STORED_ITEM"), false)) {
				
			}
		}
	}

}
