package dev.cworldstar.libs.cwlib.utils;

import java.util.ArrayList;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class BlockHelper {
	
    @ParametersAreNonnullByDefault
    public static void placeSlimefunBlock(Player whoPlaced, SlimefunItem sfItem, Block block) {
    	BlockState state = block.getState();
    	block.setType(sfItem.getItem().getType());
        BlockStorage.store(block, sfItem.getId());

        sfItem.callItemHandler(BlockPlaceHandler.class, (handler -> {
        	handler.onPlayerPlace(new BlockPlaceEvent(block, state, block, sfItem.getItem(), whoPlaced, true, EquipmentSlot.HAND));
        }));
    }
    
    @ParametersAreNonnullByDefault
    /**
     * Must be called on a synchronous thread.
     * @param whoIsBreaking
     * @param block
     */
    public static void breakSlimefunBlock(Block block) {
    	SlimefunItem item = BlockStorage.check(block);
    	if(item == null) {
    		block.breakNaturally();
    	} else {
    		BlockBreakEvent event = new BlockBreakEvent(block, Bukkit.getOnlinePlayers().toArray(Player[]::new)[0]);
    		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
    		item.callItemHandler(BlockBreakHandler.class, (handler -> {
            	handler.onPlayerBreak(
            			event, 
            			item.getItem(),
            			drops
            	);
            }));
    		Bukkit.getPluginManager().callEvent(event);
    		if(!event.isCancelled()) {
    			for(ItemStack drop : drops) {
        			block.getLocation().getWorld().dropItem(block.getLocation(), drop);
    			}
    			BlockStorage.clearBlockInfo(block);
    			block.setType(Material.AIR);
    		}
    	}
    }
    
    public static Vector direction(Block origin, Block towards, double magnitude) {
    	return (towards.getLocation().clone().toVector().subtract(origin.getLocation().toVector()));
    }
  
    
    @ParametersAreNonnullByDefault
    public static ArrayList<Block> getBlocksInSphere(Location center, double radius) {
    	ArrayList<Block> blocks = new ArrayList<Block>();
    	
    	double bx = center.getBlockX();
    	double by = center.getBlockY();
    	double bz = center.getBlockZ();
    	
        for (double x = bx - radius; x <= bx + radius; x++) {
            for (double y = by - radius; y <= by + radius; y++) {
                for (double z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y));
                    if (distance < radius * radius && (distance < (radius - 1) * (radius - 1))) {
                        blocks.add(new Location(center.getWorld(), x, y, z).getBlock());
                    }
                }
            }
        }
        return blocks;
    }
    
    @ParametersAreNonnullByDefault
    public static ArrayList<Block> getBlocksInBoundingBox(World world, BoundingBox boundingBox) {
    	ArrayList<Block> blocks = new ArrayList<Block>();
       
    	double minX = boundingBox.getMinX();
        double minY = boundingBox.getMinY();
        double minZ = boundingBox.getMinZ();
        double maxX = boundingBox.getMaxX();
        double maxY = boundingBox.getMaxY();
        double maxZ = boundingBox.getMaxZ();
        
        for (int x = (int) Math.floor(minX); x <= (int) Math.floor(maxX); x++) {
            for (int y = (int) Math.floor(minY); y <= (int) Math.floor(maxY); y++) {
                for (int z = (int) Math.floor(minZ); z <= (int) Math.floor(maxZ); z++) {
                	Block block = world.getBlockAt(x, y, z);
                	blocks.add(block);
                }
            }
        }
        return blocks;
    }

	public static @NotNull Vector randomVector(int xBound, int yBound, int zBound) {
		return new Vector(RandomUtils.nextInt(0, xBound) - (xBound/2), RandomUtils.nextInt(0, yBound) - (yBound/2), RandomUtils.nextInt(0, zBound) - (zBound/2));
	}
    
}
