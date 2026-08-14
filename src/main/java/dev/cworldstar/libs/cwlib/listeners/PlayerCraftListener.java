package dev.cworldstar.libs.cwlib.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.handlers.ItemCraftHandler;
import dev.cworldstar.libs.cwlib.handlers.NewInstanceHandler;
import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunItemSpawnEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
public class PlayerCraftListener extends AbstractListener {
	
	public PlayerCraftListener(AbstractSFAddon addon) {
		super(addon);
	}
	
	@EventHandler
	public void onMultiBlockCraftEvent(MultiBlockCraftEvent e) {
		SlimefunItem item = SlimefunItem.getByItem(e.getOutput());
		if(item != null) {
			item.callItemHandler(NewInstanceHandler.class, handler -> handler.onNewInstance(e.getOutput()));
			item.callItemHandler(ItemCraftHandler.class, handler -> handler.onCraft(e, (Player) e.getPlayer(), e.getInput(), e.getOutput()));
		}
	}
	
	@EventHandler
	public void onSlimefunItemSpawnEvent(SlimefunItemSpawnEvent e) {
		SlimefunItem item = SlimefunItem.getByItem(e.getItemStack());
		if(item != null) {
			item.callItemHandler(NewInstanceHandler.class, handler -> handler.onNewInstance(e.getItemStack()));
		}
	}
	
}
