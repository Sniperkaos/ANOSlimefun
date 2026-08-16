package dev.cworldstar.libs.cwlib.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.libs.cwlib.impl.breathing.Lungs;
import dev.cworldstar.libs.cwlib.impl.breathing.Lungs.LungAction;
import dev.cworldstar.libs.cwlib.impl.gas.GasStorage;
import lombok.Getter;
import lombok.Setter;

public class LivingEntityBreatheEvent extends EntityEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private @Getter Lungs lungs;
	private @Getter @Setter GasStorage environment;
	private @Getter LungAction action;
	private boolean cancelled;
	
	/**
	 * You can edit the {@link environment} to change
	 * where the lungs should pull from while breathing.
	 * Also changes where the exhale gas goes when you change it.
	 * 
	 * @param entity
	 * @param lungs
	 * @param environment
	 * @param action
	 */
	public LivingEntityBreatheEvent(
			@NotNull LivingEntity entity,
			@NotNull Lungs lungs,
			@NotNull GasStorage environment,
			@NotNull LungAction action
	) {
		super(entity);
		this.lungs = lungs;
		this.environment = environment;
		this.action = action;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	public void cancel() {
		setCancelled(true);
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

}
