package dev.cworldstar.libs.cwlib.handlers;

import javax.annotation.Nonnull;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;
import io.papermc.paper.event.player.PlayerArmSwingEvent;

/**
 * A {@link SwordSwingHandler} is an implementation of {@link PlayerAttackHandler},
 * which uses {@link PlayerArmSwingEvent} to determine whether or not a player
 * has attacked an entity. This bypasses creative mode, since it directly sets HP to zero.
 */
public abstract class SwordSwingHandler implements ItemHandler {
	/**
	 * @param e The {@link PlayerArmSwingEvent} associated with this swing.
	 * @param hit The {@link Entity} being attacked. Can be null if nothing was hit.
	 * @param player The {@link Player} who is attacking.
	 * @return False if the event should be cancelled, True if not.
	 */
    public abstract boolean onPlayerArmSwing(@Nonnull PlayerArmSwingEvent e, @Nonnull Player player, @Nullable LivingEntity hit);
	
	@Override
	public Class<? extends ItemHandler> getIdentifier() {
		return SwordSwingHandler.class;
	}
	
}