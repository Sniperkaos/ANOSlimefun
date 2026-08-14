package dev.cworldstar.libs.cwlib.events;

import javax.annotation.Nonnull;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SFTickEvent extends Event {

	private long currentTick = 0;
	
	private static final HandlerList handlers = new HandlerList();
	
	public SFTickEvent(long currentTick) {
		this.currentTick = currentTick;
	}
	
	public long tick() {
		return currentTick;
	}
	
	public boolean subtick(int tick) {
		return currentTick % tick == 0;
	}
	
	public static @Nonnull HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return getHandlerList();
	}
}
