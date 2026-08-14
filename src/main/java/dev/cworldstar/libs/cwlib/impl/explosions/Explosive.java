package dev.cworldstar.libs.cwlib.impl.explosions;

import org.bukkit.inventory.ItemStack;
import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;

public abstract class Explosive extends SlimefunItem {
	public Explosive(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(itemGroup, item, recipeType, recipe);
		
		addItemHandler(new BlockUseHandler() {
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				ExplosionConfiguration config = ExplosionConfiguration.create()
					.explosionRadius(getExplosionRadius())
					.strength(getStrength())
					.falloutRadius(getFalloutRadius())
					.radioactive(isRadioactive())
					.center(e.getClickedBlock().get().getLocation());
				AbstractSFAddon.get().getExplosionManager().explode(e.getPlayer(), config);
			}
		});
	}
	
	public abstract int getStrength();
	public abstract double getExplosionRadius();
	public double getFalloutRadius() {
		return 0;
	};
	public boolean isRadioactive() {
		return false;
	};
}
