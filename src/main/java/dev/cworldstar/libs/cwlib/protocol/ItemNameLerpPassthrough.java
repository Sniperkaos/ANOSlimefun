package dev.cworldstar.libs.cwlib.protocol;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;

public class ItemNameLerpPassthrough {
	private double ticks = 0;
	private boolean reverse = false;
	public ItemNameLerpPassthrough() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if(reverse) {
					ticks -= 1;
				} else {
					ticks += 1;
				}
				if(ticks >= 100 || ticks <= -100) {
					reverse = !reverse;
				}
			}
		}.runTaskTimer(AbstractSFAddon.get(), 0L, 10L);
		ItemEditorProtocol.passthrough((ItemStack item, Player player) -> {
			ItemMeta meta = item.getItemMeta();
			if(meta != null) {
				PersistentDataContainer pdc = meta.getPersistentDataContainer();
				if(pdc.has(ItemStackBuilder.COLOR_LERP_KEY)) {
					meta.itemName(
							FormatUtils.mm(
									pdc.get(
											ItemStackBuilder.COLOR_LERP_KEY, 
											PersistentDataType.STRING
									).replace("%phase%", String.valueOf(1-(ticks/100)))
							)
					);
					item.setItemMeta(meta);
				}
			}
			return item;
		});
	}
}
