package dev.cworldstar.libs.cwlib.builders;

import org.bukkit.inventory.ItemStack;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.groups.InvisibleGroup;

public class ItemGroupBuilder {
	
	private ItemStack item;
	private String id;
	private int tier;
	
	public ItemGroupBuilder() {
		
	}
	
	
	public ItemGroupBuilder setItem(ItemStack item) {
		this.item = item;
		return this;
	}
	
	public ItemGroupBuilder setTier(int tier) {
		this.tier = tier;
		return this;
	}
	
	public ItemGroupBuilder setID(String id) {
		this.id = id;
		return this;
	}
	
	public InvisibleGroup build() {
		return new InvisibleGroup(AbstractSFAddon.key(id), item, tier);
	}
	
}
