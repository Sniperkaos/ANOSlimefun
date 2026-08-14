package dev.cworldstar.libs.cwlib.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.impl.NotEnchantable;
import dev.cworldstar.libs.cwlib.impl.PreventDisenchant;
import dev.cworldstar.libs.cwlib.impl.PreventEnchant;
import io.github.thebusybiscuit.slimefun4.api.events.AutoDisenchantEvent;
import io.github.thebusybiscuit.slimefun4.api.events.AutoEnchantEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

public class AutoDisenchantListener extends AbstractListener {

	public AutoDisenchantListener(AbstractSFAddon addon) {
		super(addon);
	}

	@EventHandler
	public void onAutoDisenchantEvent(AutoDisenchantEvent e) {
		SlimefunItem item = SlimefunItem.getByItem(e.getItem());
		if(item == null) return;
		if(item instanceof PreventDisenchant) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onAutoEnchantEvent(AutoEnchantEvent e) {
		SlimefunItem item = SlimefunItem.getByItem(e.getItem());
		if(item == null) return;
		if(item instanceof PreventEnchant) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEnchantEvent(EnchantItemEvent e) {
		SlimefunItem item = SlimefunItem.getByItem(e.getItem());
		if(item == null) return;
		if(item instanceof NotEnchantable) {
			e.setCancelled(true);
		}
	}
}
