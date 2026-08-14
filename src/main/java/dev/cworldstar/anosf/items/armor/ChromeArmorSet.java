package dev.cworldstar.anosf.items.armor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ArmorBuilder;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet;
import dev.cworldstar.libs.cwlib.impl.armor.ArmorSetPiece;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class ChromeArmorSet extends AbstractArmorSet {

	private Map<UUID, Boolean> equipped = new HashMap<UUID, Boolean>();
	
	private static final String[] CHROME_ARMOR_LORE = new String[] {
			"<italic><gradient:white:gray:white:white>\"Surely, we can't make steel better?\"",
			"<italic><gradient:white:white:gray:white>I laughed, my face morphing into a disdaining smirk.",
			"<italic><#C0C0C0>\"Those who know...\"",
			"",
			"<gradient:white:gray:white:white><underlined>Chrome Set Abilities:", 
			" <gray>[<#C0C0C0>Shiny<gray>]: This armor is shiny!",
			" <gray>[<#C0C0C0>Resistant<gray>]: This armor is resistant to burning!",
			" <gray>[<#964B00>Brittle<gray>]: The durability of this armor is equal",
			"<gray>to the corresponding <gold>gold <gray>armor piece."
	};
	
	
	public ChromeArmorSet() {
		super(ANOSF.key("CHROME_ARMOR_SET"));
		
		addArmorPiece(
			new ArmorSetPiece(ANOSF.key("CHROME_ARMOR_SET"), ItemRegistry.getItemGroup("ARMOR_CATEGORY"),
				new ArmorBuilder(Material.IRON_HELMET)
					.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 25)
					.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_HELMET", 4, EquipmentSlotGroup.HEAD)
					.setName("<#C0C0C0>Chrome Helmet")
					.setLore(CHROME_ARMOR_LORE)
					.setMaxDurability(77)
					.get(), 
				"CHROME_HELMET", 
				RecipeType.ARMOR_FORGE, 
				new ItemStack[] {
					ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),
					ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),null,ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),
					null,null,null
				}
			)
		);
		addArmorPiece(
			new ArmorSetPiece(ANOSF.key("CHROME_ARMOR_SET"), ItemRegistry.getItemGroup("ARMOR_CATEGORY"),
				new ArmorBuilder(Material.IRON_CHESTPLATE)
					.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 25)
					.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_CHESTPLATE", 8, EquipmentSlotGroup.CHEST)
					.setName("<#C0C0C0>Chrome Chestplate")
					.setLore(CHROME_ARMOR_LORE)
					.setMaxDurability(112)
					.get(), 
				"CHROME_CHESTPLATE", 
				RecipeType.ARMOR_FORGE, 
				new ItemStack[] {
					ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),null,ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),
					ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),
					ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),ItemRegistry.getRegistryItem("CHROME_INGOT").getItem()
				}
			)
		);
		addArmorPiece(
				new ArmorSetPiece(ANOSF.key("CHROME_ARMOR_SET"), ItemRegistry.getItemGroup("ARMOR_CATEGORY"),
					new ArmorBuilder(Material.IRON_LEGGINGS)
						.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 25)
						.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_LEGGINGS", 6, EquipmentSlotGroup.LEGS)
						.setName("<#C0C0C0>Chrome Leggings")
						.setLore(CHROME_ARMOR_LORE)
						.setMaxDurability(105)
						.get(), 
					"CHROME_LEGGINGS", 
					RecipeType.ARMOR_FORGE, 
					new ItemStack[] {
						ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),
						ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),null,ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),
						ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),null,ItemRegistry.getRegistryItem("CHROME_INGOT").getItem()
					}
				)
			);
		addArmorPiece(
				new ArmorSetPiece(ANOSF.key("CHROME_ARMOR_SET"), ItemRegistry.getItemGroup("ARMOR_CATEGORY"),
					new ArmorBuilder(Material.IRON_BOOTS)
						.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 25)
						.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_BOOTS", 3, EquipmentSlotGroup.FEET)
						.setName("<#C0C0C0>Chrome Boots")
						.setLore(CHROME_ARMOR_LORE)
						.setMaxDurability(91)
						.get(), 
					"CHROME_BOOTS", 
					RecipeType.ARMOR_FORGE, 
					new ItemStack[] {
						null,null,null,
						ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),null,ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),
						ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),null,ItemRegistry.getRegistryItem("CHROME_INGOT").getItem()
					}
				)
			);
	}

	@Override
	public Consumer<AbstractArmorSet> armorTick() {
		return (AbstractArmorSet set) -> {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(!equipped.containsKey(p.getUniqueId())) {
					equipped.put(p.getUniqueId(), false);
				}
				
				boolean active = set.active(p);
				if(active) {
					this.equipped.put(p.getUniqueId(), true);
				} else {
					if(this.equipped.get(p.getUniqueId()) == true) {
						this.equipped.put(p.getUniqueId(), false);
					};
				}
			}
		};
	}

}
