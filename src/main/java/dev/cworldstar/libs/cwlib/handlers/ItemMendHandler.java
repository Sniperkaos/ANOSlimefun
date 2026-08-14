package dev.cworldstar.libs.cwlib.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;

/**
 * This {@link ItemHandler} fires when the ItemMendEvent is triggered.
 * It is for more advanced items, as {@link DurabilityLossListener} will trigger
 * as well.
 * @author cworldstar
 *
 */
public abstract class ItemMendHandler implements ItemHandler {
	
	@Override
	public Class<? extends ItemHandler> getIdentifier() {
		return ItemMendHandler.class;
	}
	public abstract boolean onItemMend(PlayerItemMendEvent e, Player p, ItemStack item);

}
