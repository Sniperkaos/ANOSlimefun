package dev.cworldstar.libs.cwlib.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemMendEvent;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.handlers.DurabilityLossHandler;
import dev.cworldstar.libs.cwlib.handlers.ItemMendHandler;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

/**
 * 
 * This {@link AbstractListener} is what makes the {@link DurabilityLossHandler} work.
 * 
 * @author cworldstar
 * @date 7/4/2025
 */
public class DurabilityDamageListener extends AbstractListener {
	public DurabilityDamageListener(AbstractSFAddon addon) {
		super(addon);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDurabilityDamage(PlayerItemDamageEvent e) {
		SlimefunItem item = SlimefunItem.getByItem(e.getItem());
		if(item == null) {
			return;
		}
		item.callItemHandler(DurabilityLossHandler.class, handler -> handler.onDurabilityLoss(e, e.getPlayer(), e.getItem()));
	}
	
	/**
	 * This listener triggers the negative DurabilityLossHandler and the ItemMendHandler.
	 * @param e The {@link PlayerItemMendEvent} associated with this listener.
	 */
	@EventHandler()
	public void onItemMendEvent(PlayerItemMendEvent e) {
		SlimefunItem item = SlimefunItem.getByItem(e.getItem());
		if(item == null) {
			return;
		}
		// we don't call this event, since it's a proxy event and will never actually trigger.
		// disabled for now | item.callItemHandler(DurabilityLossHandler.class, handler -> handler.onDurabilityLoss(new PlayerItemDamageEvent(e.getPlayer(), e.getItem(), -e.getRepairAmount(), -e.getRepairAmount()), e.getPlayer(), e.getItem()));
		item.callItemHandler(ItemMendHandler.class, handler -> handler.onItemMend(e, e.getPlayer(), e.getItem()));
	}
}
