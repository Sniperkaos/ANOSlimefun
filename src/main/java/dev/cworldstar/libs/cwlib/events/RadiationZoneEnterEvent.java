package dev.cworldstar.libs.cwlib.events;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class RadiationZoneEnterEvent extends PlayerEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	public RadiationZoneEnterEvent(@NotNull Player who) {
		super(who);
	}

	public static @Nonnull HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public @NotNull HandlerList getHandlers() {
		return getHandlerList();
	}

}
