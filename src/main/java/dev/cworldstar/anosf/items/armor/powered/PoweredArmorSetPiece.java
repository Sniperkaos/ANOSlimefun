package dev.cworldstar.anosf.items.armor.powered;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.datatypes.ANOSFDataType;
import dev.cworldstar.anosf.svoltz.SVCapacitor;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet.ArmorContext;
import dev.cworldstar.libs.cwlib.impl.armor.ArmorSetPiece;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.papermc.paper.persistence.PersistentDataContainerView;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuCloseHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;

public class PoweredArmorSetPiece extends ArmorSetPiece implements SVCapacitor {
	
	private ArmorContext context;
	private long SVCapacity;
	
	public PoweredArmorSetPiece(
			NamespacedKey armorSetId, 
			ItemGroup itemGroup, 
			ItemStack item, 
			String id
	) {
		super(armorSetId, itemGroup, item, id);
		addItemHandler(new ItemUseHandler() {
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				if(e.getPlayer().isSneaking()) {
					openMenu(e.getPlayer(), e.getItem());
				}
			}
		});
	}
	
	private static final ItemStack DISPLAY_ITEM = new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").build();
	
	private static final int[] DISPLAY_SLOTS = new int[] {
			0,1,2,3,4,5,6,7,8,
			9,17,
			18,19,20,21,23,24,25,26
	};
	
	private static final int[] UPGRADE_SLOTS = new int[] {
		10,11,12,13,14,15,16
	};
	
	private static final int CAPACITOR_SLOT = 22;
			
	private static final NamespacedKey CAPACITOR_KEY = ANOSF.key("INSTALLED_CAPACITOR");
	
	protected void openMenu(@NotNull Player player, ItemStack armorPiece) {
		ChestMenu menu = new ChestMenu("Powered Armor | Upgrade Menu");
		
		ItemStack capacitor = getInstalledCapacitor(armorPiece);
		if(capacitor == null) {
			capacitor = new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE)
				.name("<orange>No capacitor installed!</orange>")
				.build();
		}
		
		PoweredArmorUpgrade[] upgrades = armorUpgrades(armorPiece);
		if(upgrades == null) {
			upgrades = new PoweredArmorUpgrade[7];
		}
		
		List<PoweredArmorUpgrade> currentUpgrades = Arrays.asList(upgrades);
		
		for(int slot : DISPLAY_SLOTS) {
			menu.addItem(slot, DISPLAY_ITEM);
			menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
		}
		
		int i = 0;
		
		for(int slot : UPGRADE_SLOTS) {
			menu.addItem(slot, upgrades[i].getItem());
			menu.addMenuClickHandler(slot, new MenuClickHandler() {
				@Override
				public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
					if(menu.getItemInSlot(slot) != null) {
						p.setItemOnCursor(item);
						menu.toInventory().setItem(slot, null);
					} else if(p.getItemOnCursor() != null) {
						// check if this is an upgrade
						SlimefunItem sfItem = SlimefunItem.getByItem(item);
						if(sfItem != null && sfItem instanceof PoweredArmorUpgrade) {
							menu.toInventory().setItem(slot, p.getItemOnCursor());
							p.setItemOnCursor(null);
						}
					}
					return false;
				}
			});
			i++;
		}
		
		menu.addItem(CAPACITOR_SLOT, capacitor);
		menu.addMenuClickHandler(CAPACITOR_SLOT, new MenuClickHandler() {
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				return false;
			}
		});
		
		menu.addMenuCloseHandler(new MenuCloseHandler() {
			@Override
			public void onClose(Player p) {
				for(int slot : UPGRADE_SLOTS) {
					ItemStack item = menu.getItemInSlot(slot);
					SlimefunItem sfItem = SlimefunItem.getByItem(item);
					if(sfItem != null && sfItem instanceof PoweredArmorUpgrade upgrade) {
						if(!(currentUpgrades.contains(upgrade))) {
							currentUpgrades.add(upgrade);
						}
					}
				}
				
				setArmorUpgrades(armorPiece, currentUpgrades.toArray(PoweredArmorUpgrade[]::new));
			}
		});
		
		menu.build().open(player);
	}
	
	public ItemStack getInstalledCapacitor(ItemStack armorPiece) {
		ItemMeta meta = armorPiece.getItemMeta();
		if(meta == null) return null;
		return meta.getPersistentDataContainer().get(CAPACITOR_KEY, ANOSFDataType.ITEM_DATA_TYPE);
	}

	@Override
	public double getResistance(ItemStack item) {
		return 0;
	}		
	
	public <T extends Event> void triggerArmorEvent(T event, ItemStack item) {
		for(PoweredArmorUpgrade upgrade : armorUpgrades(item)) {
			if(upgrade.getUpgradeType().eventMatches(event)) {
				upgrade.trigger(item, event);
			}
		}
	}
	
	public <T extends PoweredArmorUpgrade> void addArmorUpgrade(ItemStack item, T upgrade) {
		ItemMeta meta = item.getItemMeta();
		if(meta == null) {
			meta = Bukkit.getItemFactory().getItemMeta(item.getType());
		}
		
		PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
		
		PoweredArmorUpgradeContainer container = dataContainer.getOrDefault(
			PoweredArmorUpgradeContainer.POWERED_ARMOR_UPGRADE_CONTAINER_KEY, 
			ANOSFDataType.CONTAINER_DATA_TYPE, 
			new PoweredArmorUpgradeContainer()
		);		
		
		container.addUpgrade(upgrade.getUpgradeKey());
		
		dataContainer.set(
				PoweredArmorUpgradeContainer.POWERED_ARMOR_UPGRADE_CONTAINER_KEY, 
				ANOSFDataType.CONTAINER_DATA_TYPE, 
				container
		);
		
		item.setItemMeta(meta);
	}
	
	public <T extends PoweredArmorUpgrade> void setArmorUpgrades(ItemStack item, PoweredArmorUpgrade[] upgrades) {
		ItemMeta meta = item.getItemMeta();
		if(meta == null) {
			meta = Bukkit.getItemFactory().getItemMeta(item.getType());
		}
		
		PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
		
		PoweredArmorUpgradeContainer container = dataContainer.getOrDefault(
			PoweredArmorUpgradeContainer.POWERED_ARMOR_UPGRADE_CONTAINER_KEY, 
			ANOSFDataType.CONTAINER_DATA_TYPE, 
			new PoweredArmorUpgradeContainer()
		);
		
		
		for(PoweredArmorUpgrade upgrade : upgrades) {
			container.addUpgrade(upgrade.getUpgradeKey());
		}
		
		dataContainer.set(
				PoweredArmorUpgradeContainer.POWERED_ARMOR_UPGRADE_CONTAINER_KEY, 
				ANOSFDataType.CONTAINER_DATA_TYPE, 
				container
		);
		
		item.setItemMeta(meta);
	}
	
	public PoweredArmorUpgrade[] armorUpgrades(ItemStack item) {
		PersistentDataContainerView view = item.getPersistentDataContainer();			
		if(view.has(PoweredArmorUpgradeContainer.POWERED_ARMOR_UPGRADE_CONTAINER_KEY)) {
			PoweredArmorUpgradeContainer container = view.get(
				PoweredArmorUpgradeContainer.POWERED_ARMOR_UPGRADE_CONTAINER_KEY, 
				ANOSFDataType.CONTAINER_DATA_TYPE
			);
			return container.getUpgrades().stream().map(str -> PoweredArmorUpgrade.getUpgrade(str)).toArray(PoweredArmorUpgrade[]::new);
		}
		return new PoweredArmorUpgrade[0];
	}

	public ArmorContext getContext() {
		return context;
	}
	
	public void setContext(ArmorContext context) {
		this.context = context;
	}
}
