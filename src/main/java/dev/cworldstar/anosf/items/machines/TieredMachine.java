package dev.cworldstar.anosf.items.machines;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.libs.cwlib.abstracts.AbstractMachineBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import lombok.Getter;
import lombok.Setter;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class TieredMachine extends AbstractMachineBlock {

	@Getter
	@Setter
	private int capacity;
	@Getter
	@Setter
	private int processSlot;
	
	public TieredMachine(ItemGroup category,  SlimefunItemStack item , RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}

	@Override
	public EnergyNetComponentType getEnergyComponentType() {
		return EnergyNetComponentType.CONSUMER;
	}

	@Override
	public String getMachineIdentifier() {
		return this.getId();
	}

	@Override
	public boolean process(Block b, BlockMenu menu) {
		return false;
	}

	@Override
	public void powerLoss(Block b, BlockMenu menu) {
		
	}

	@Override
	public ItemStack getProcessingItem(Block b) {
		return null;
	}

	@Override
	public void setup(BlockMenuPreset preset) {
		
	}

	@Override
	public int[] getInputSlots() {
		return null;
	}

	@Override
	public int[] getOutputSlots() {
		// TODO Auto-generated method stub
		return null;
	}

}
