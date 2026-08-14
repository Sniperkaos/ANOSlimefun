package dev.cworldstar.libs.cwlib.abstracts;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.libs.cwlib.presets.MenuBlockPreset;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

/**
 * AbstractMenuBlock creates a {@link SlimefunItem} with an associated {@link BlockMenu}.
 * @author cworldstar
 */
public abstract class AbstractMenuBlock extends SlimefunItem {

    public static final ItemStack PROCESSING_ITEM = CustomItemStack.create(Material.LIME_STAINED_GLASS_PANE, "&aProcessing...");
    public static final ItemStack NO_ENERGY_ITEM = CustomItemStack.create(Material.RED_STAINED_GLASS_PANE, "&cNot enough energy!");
    public static final ItemStack IDLE_ITEM = CustomItemStack.create(Material.BLACK_STAINED_GLASS_PANE, "&8Idle");
    public static final ItemStack NO_ROOM_ITEM = CustomItemStack.create(Material.ORANGE_STAINED_GLASS_PANE, "&6Not enough room!");
    public static final ItemStack OUTPUT_BORDER = CustomItemStack.create(ChestMenuUtils.getOutputSlotTexture(), "&6Output");
    public static final ItemStack INPUT_BORDER = CustomItemStack.create(ChestMenuUtils.getInputSlotTexture(), "&9Input");
    public static final ItemStack BACKGROUND_ITEM = ChestMenuUtils.getBackground();
	
    public void setup() {
		addItemHandler(new BlockBreakHandler(false, false) {
			@Override
			public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                BlockMenu menu = BlockStorage.getInventory(e.getBlock());
				boolean dropItems = obreak(e, item, drops, menu);
				if(dropItems) {
					Location loc = menu.getLocation();
					menu.dropItems(loc, getInputSlots());
					menu.dropItems(loc, getOutputSlots());
				}
			}
		});
		
		addItemHandler(new BlockPlaceHandler(false) {

			@Override
			public void onPlayerPlace(BlockPlaceEvent e) {
                BlockMenu menu = BlockStorage.getInventory(e.getBlock());
				oplace(e, menu);
			}
			
		});
    }
    
	public AbstractMenuBlock(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType,
			ItemStack[] recipe) {
		this(itemGroup, new SlimefunItemStack(id, item), recipeType, recipe);
	}
	
    public AbstractMenuBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
    	super(itemGroup, item, recipeType, recipe);
    	setup();
    	
	}

	@Override
    public void postRegister() {
        new MenuBlockPreset(this);
    }
	
    public abstract void setup(BlockMenuPreset preset);
	
    @Nonnull
    public final int[] getTransportSlots(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
        switch (flow) {
            case INSERT:
                return getInputSlots(menu, item);
            case WITHDRAW:
                return getOutputSlots();
            default:
                return new int[0];
        }
    }
	
    public int[] getInputSlots(DirtyChestMenu menu, ItemStack item) {
        return getInputSlots();
    }
	
    public abstract int[] getInputSlots();

    public abstract int[] getOutputSlots();
    
    public void onNewInstance(BlockMenu menu, Block b) {

    }
    
	protected @Nonnull boolean obreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops, BlockMenu menu) {
		return true;
	};
	protected void oplace(BlockPlaceEvent e, BlockMenu menu) {};

}
