package dev.cworldstar.libs.cwlib.handlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;

public abstract class ItemCraftHandler implements ItemHandler {
	@Override
	public Class<? extends ItemHandler> getIdentifier() {
		return ItemCraftHandler.class;
	}
	
	public abstract boolean onCraft(MultiBlockCraftEvent e, Player player, ItemStack[] input, ItemStack output);
}
