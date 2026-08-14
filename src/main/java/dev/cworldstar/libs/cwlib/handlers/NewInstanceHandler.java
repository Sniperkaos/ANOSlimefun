package dev.cworldstar.libs.cwlib.handlers;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;

/**
 * This {@link ItemHandler} is called when a {@link SlimefunItem} is created in the world, whether be by crafting or dropped by a block.
 * Primarily used to update items that start with unfinished data or variable placeholders. 
 * @see me.cworldstar.highriseSF.impl.items.pets.AbstractPet
 * @author cworldstar
 */
public abstract class NewInstanceHandler implements ItemHandler {
	
	public abstract void onNewInstance(ItemStack item);
	
	@Override
	public Class<? extends ItemHandler> getIdentifier() {
		return NewInstanceHandler.class;
	}

}
