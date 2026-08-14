package dev.cworldstar.libs.cwlib.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.containers.NumberContainer;
import dev.cworldstar.libs.cwlib.events.SFTickEvent;
import dev.cworldstar.libs.cwlib.handlers.TickHandler;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

public class TickListener extends AbstractListener {
	public TickListener(AbstractSFAddon addon) {
		super(addon);
	}
	
	@EventHandler
	public void onSFTick(SFTickEvent e) {
		AbstractArmorSet.onTick();
		for(Player p : Bukkit.getOnlinePlayers()) {
			final NumberContainer container = new NumberContainer();
			for(ItemStack item : p.getInventory().getContents()) {
				container.increment();
				SlimefunItem sfItem = SlimefunItem.getByItem(item);
				if(sfItem != null) {
					sfItem.callItemHandler(
						TickHandler.class, 
						handler -> handler.onTick(sfItem, p, item, container.asInteger())
					);
				}
			}
		}
	}
}
