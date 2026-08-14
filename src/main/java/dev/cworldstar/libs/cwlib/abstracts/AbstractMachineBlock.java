package dev.cworldstar.libs.cwlib.abstracts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import lombok.Getter;
import lombok.Setter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class AbstractMachineBlock extends AbstractTickingMenuBlock implements EnergyNetComponent {

	
    protected final List<MachineRecipe> recipes = new ArrayList<>();
    
    @Getter
    @Setter
	private int powerLossPerTick = 1;
    @Getter
    @Setter
	private int powerLossPerProcess = 1;
    @Getter
    @Setter
    private int processingSpeed = 1;
	
	public AbstractMachineBlock(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType,
			ItemStack[] recipe) {
		super(itemGroup, item, id, recipeType, recipe);
	}
	
    public AbstractMachineBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}
    
	
    public abstract String getMachineIdentifier();
    public abstract int getProcessSlot();
	public abstract boolean process(Block b, BlockMenu menu);
	public abstract void powerLoss(Block b, BlockMenu menu);
	public abstract ItemStack getProcessingItem(Block b);
	
	public void setPowerCapacity(int number) {
	
	}
	
	@Override
	public void tick(Block b, BlockMenu menu) {
		Location blockLocation = b.getLocation();
		int charge = getCharge(blockLocation);
		
		if((charge < powerLossPerProcess && powerLossPerProcess != -1) || (charge < powerLossPerTick && powerLossPerTick != -1) ) {
			powerLoss(b, menu);
			menu.replaceExistingItem(getProcessSlot(), NO_ENERGY_ITEM);
			return;
		}
		
		if(process(b, menu)) {
			if(menu.hasViewer()) {
				menu.replaceExistingItem(getProcessSlot(), getProcessingItem(b));	
			}
			removeCharge(blockLocation, powerLossPerProcess);
		}
	
		removeCharge(blockLocation, powerLossPerTick);
	}
}
