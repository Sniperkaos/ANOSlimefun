package dev.cworldstar.anosf.entities.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.anosf.entities.TrueDamage.TrueDamageType;

/**
 * This {@link EntityEvent} is fired when a {@link AbstractEnemy} is afflicted by {@link TrueDamage}.
 * Can be cancelled to prevent the damage.
 * @author cworldstar
 *
 */
public class TrueDamageDealtEvent extends EntityEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private double damage = 0;
	private boolean cancelled = false;
	private TrueDamageType type;
	
	public TrueDamageDealtEvent(@NotNull Entity entity, double damage, TrueDamageType type) {
		super(entity);
		this.damage = damage;
		this.type = type;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	public double damage() {
		return damage;
	}
	
	public void damage(double damage) {
		this.damage = damage;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean cancelled() {
		return isCancelled();
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	public TrueDamageType getType() {
		return type;
	}

}
