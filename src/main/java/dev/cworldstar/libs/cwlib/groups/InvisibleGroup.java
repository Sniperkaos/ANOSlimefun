package dev.cworldstar.libs.cwlib.groups;

import javax.annotation.Nonnull;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.impl.groups.ANOSFRecipeGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

public class InvisibleGroup extends ItemGroup {
	
	private ANOSFRecipeGroup proxy;
	private boolean visible = false;
	
	public InvisibleGroup(NamespacedKey key, ItemStack item, int tier) {
		super(key, item, tier);
		proxy = new ANOSFRecipeGroup(key, item, tier);
	}
	
	@Override
	public void add(SlimefunItem item) {
		proxy.addItem(item);
		super.add(item);
	}
	
	@Override
	public boolean isVisible(Player p) {
		return visible;
	}
	
	public void visible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean visible() {
		return visible;
	}
	
	@Override
    public void remove(@Nonnull SlimefunItem item) {
		proxy.removeItem(item);
        super.remove(item);
    }

	public ANOSFRecipeGroup proxy() {
		return proxy;
	}

}
