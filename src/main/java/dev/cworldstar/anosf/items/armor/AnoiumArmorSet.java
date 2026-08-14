package dev.cworldstar.anosf.items.armor;

import java.util.function.Consumer;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet;

public class AnoiumArmorSet extends AbstractArmorSet implements Listener {

	private static final NamespacedKey ANOIUM_ARMOR_SET_KEY = ANOSF.key("ANOIUM_ARMOR_SET");
	
	public AnoiumArmorSet() {
		super(ANOIUM_ARMOR_SET_KEY);
	}

	@Override
	public Consumer<AbstractArmorSet> armorTick() {
		return DEFAULT_ARMOR_TICK;
	}
	
}
