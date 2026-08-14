package dev.cworldstar.anosf.items.armor.powered;

import java.util.HashMap;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.svoltz.SVConsumer;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.events.SFTickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;

/**
 * This is an implementation for powered armor upgrades.
 * To create one, extend PoweredArmorUpgrade and provide it with a 
 * {@link PoweredArmorUpgradeType}. 
 * {@link #PoweredArmorUpgrade(NamespacedKey, PoweredArmorUpgradeType, BiConsumer)}
 * 
 * @author cworldstar
 */
public abstract class PoweredArmorUpgrade extends SlimefunItem implements SVConsumer {
	
	public static enum PoweredArmorUpgradeType {
		TICK(SFTickEvent.class),
		DAMAGE(EntityDamageEvent.class),
		MOVE(PlayerMoveEvent.class);
		
		PoweredArmorUpgradeType(Class<? extends Event> clazz) {
			eventClass = clazz;
		}
		
		private @Getter Class<? extends Event> eventClass;
	
		public <T> boolean eventMatches(T event) {
			return event.getClass().isInstance(eventClass);
		}
	}
	
	protected static final HashMap<NamespacedKey, PoweredArmorUpgrade> upgrades = new HashMap<>();
	
	public static PoweredArmorUpgrade getUpgrade(String key) {
		return upgrades.get(ANOSF.key(key));
	}
	
	/**
	 * Might be needed if the Namespace of the extending key doesn't equal ANOSF's one
	 * @param key
	 * @return
	 */
	public static PoweredArmorUpgrade getUpgrade(NamespacedKey key) {
		return upgrades.get(key);
	}
	
	public static <T extends PoweredArmorUpgrade> void register(T upgrade) {
		upgrades.put(upgrade.getUpgradeKey(), upgrade);
	}
	
	private @Getter NamespacedKey upgradeKey;
	private @Getter PoweredArmorUpgradeType upgradeType;
	private @Getter BiConsumer<ItemStack, Event> upgradeConsumer;

	private PoweredArmorUpgrade(ItemGroup itemGroup, NamespacedKey upgradeKey, ItemStack upgradeItem) {
		super(itemGroup, new SlimefunItemStack(upgradeKey.getKey(), upgradeItem), null, null);
	}
	
	public PoweredArmorUpgrade(ItemStack item, NamespacedKey upgradeKey, PoweredArmorUpgradeType upgradeType, BiConsumer<ItemStack, Event> consumer) {
		this(ItemRegistry.getItemGroup("POWERED_ARMOR_UPGRADES"), upgradeKey, item);
		this.upgradeKey = upgradeKey;
		this.upgradeType = upgradeType;
		upgradeConsumer = consumer;
		PoweredArmorUpgrade.register(this);
	}
	
	public <T extends Event> void trigger(ItemStack item, T event) {
		if(!eventMatches(event)) return;
		try {
			upgradeConsumer.accept(item, event);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public <T extends Event> boolean eventMatches(T event) {
		return upgradeType.eventMatches(event);
	}

	@Override
	public double getResistance(ItemStack item) {
		return 0;
	}
}