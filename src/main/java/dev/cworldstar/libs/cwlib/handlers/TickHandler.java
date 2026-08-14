package dev.cworldstar.libs.cwlib.handlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;


/**
 * This {@link ItemHandler} is called when the HighriseSF ticker is ticked.
 * @see me.TickListener.highriseSF.impl.listeners.HighriseSFTickListener
 * @author cworldstar
 *
 */
public abstract class TickHandler implements ItemHandler {

	public abstract void onTick(SlimefunItem thisItem, Player p, ItemStack item, int slot);
	
	@Override
	public Class<? extends ItemHandler> getIdentifier() {
		return TickHandler.class;
	}
	
}
