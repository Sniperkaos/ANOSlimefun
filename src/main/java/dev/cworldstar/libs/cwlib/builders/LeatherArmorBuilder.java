package dev.cworldstar.libs.cwlib.builders;

import org.apache.commons.lang3.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherArmorBuilder extends ArmorBuilder {
	public LeatherArmorBuilder(Material leather) {
		super(leather);
		Validate.isInstanceOf(LeatherArmorMeta.class, item.getItemMeta());
	}

	public LeatherArmorBuilder setColor(Color color) {
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);
		item.setItemMeta(meta);
		return this;
	}
	
	public LeatherArmorBuilder color(Color color) {
		setColor(color);
		return this;
	}
}
