package dev.cworldstar.anosf.svoltz;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import net.kyori.adventure.text.Component;

public interface SVCapacitor extends SVContainer {
	public default void charge(ItemStack item, SVoltz v) {
		
	}
	
	public default void applyLore(ItemStack item, SVoltz voltz) {
		item.editMeta(meta -> {
			List<Component> lore = meta.lore();
			lore.add(FormatUtils.mm("-------"));
			lore.add(FormatUtils.mm("Voltz: " + Double.toString(SVoltz.getWatts())));
			
			meta.lore(lore);
		});
	}
}
