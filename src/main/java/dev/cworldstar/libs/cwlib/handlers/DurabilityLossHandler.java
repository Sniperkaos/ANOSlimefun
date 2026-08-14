package dev.cworldstar.libs.cwlib.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;

public abstract class DurabilityLossHandler implements ItemHandler {
	
	@Override
	public Class<? extends ItemHandler> getIdentifier() {
		return DurabilityLossHandler.class;
	}
	public abstract boolean onDurabilityLoss(PlayerItemDamageEvent e, Player p, ItemStack item);
}
