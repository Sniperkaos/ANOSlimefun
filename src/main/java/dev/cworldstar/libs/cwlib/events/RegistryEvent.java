package dev.cworldstar.libs.cwlib.events;

import javax.annotation.Nonnull;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;


/**
 * 
 * This {@link Event} is called when the {@link me.cworldstar.highriseSF.impl.Registry#finalizeRegistry} method is invoked.
 * It can be cancelled to prevent the registration of an item. 
 * 
 * @author cworldstar
 *
 */

public class RegistryEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	private SlimefunItem item;
	
	/**
	 * @param {@link SlimefunItem} item
	 */
	public RegistryEvent(SlimefunItem item) {
		this.item = item;
	}

	/**
	 * 
	 * @return The {@link SlimefunItem} related in this event.
	 */
	public SlimefunItem getItem() {
		return item;
	}
	
	public static @Nonnull HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	public void cancel() {
		this.cancelled = true;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	public boolean cancelled() {
		return this.cancelled;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return getHandlerList();
	}

}
