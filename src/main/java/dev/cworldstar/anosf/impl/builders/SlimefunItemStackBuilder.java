package dev.cworldstar.anosf.impl.builders;

import org.bukkit.Material;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;

public class SlimefunItemStackBuilder {
	
	private String id;
	private ItemStackBuilder item;
	
	public SlimefunItemStackBuilder(Material m) {
		item = new ItemStackBuilder(m);
	}
	
	public ItemStackBuilder edit() {
		return item;
	}
	
	public void withId(String id) {
		this.id = id;
	}
	
	public SlimefunItemStack build() {
		return new SlimefunItemStack(id, item.build());
	}
}
