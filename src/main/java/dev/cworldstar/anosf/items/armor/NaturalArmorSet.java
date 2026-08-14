package dev.cworldstar.anosf.items.armor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.LeatherArmorBuilder;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet;
import dev.cworldstar.libs.cwlib.impl.armor.ArmorSetPiece;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class NaturalArmorSet extends AbstractArmorSet {
	
	private Map<UUID, Boolean> equipped = new HashMap<UUID, Boolean>();
	
	private static final String[] NATURAL_ARMOR_LORE = new String[] {
			"<italic><gradient:dark_green:green>I've always had an affinity for nature... and now,",
			"<italic><gradient:green:dark_green>nature has an affinity for me.",
			"",
			"<green><underlined>Druid Set Abilities:", 
			" <gray>[<dark_green>Resonating<gray>] - <gray>Gain <green>Life Boost III<gray>.",
			" <gray>[<dark_green>Life Energy I<gray>]: - Gain <green>Regeneration I<gray>.",
			" ",
			" <gray>[<gradient:dark_red:dark_green>Binding I</gradient><gray>]: - Nearby <red>mobs<gray> are given <gradient:dark_red:dark_green>Weakness I<gray>.",
			" <gray>and <gradient:dark_red:dark_green>Slowness I<gray>."
	};
	
	public NaturalArmorSet() {
		super(AbstractSFAddon.key("NATURAL_ARMOR_SET"));
		
		addArmorPiece(
				new ArmorSetPiece(ANOSF.key("NATURAL_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
							new LeatherArmorBuilder(Material.LEATHER_HELMET)
								.setColor(Color.fromRGB(88, 191, 29))
								.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_HELMET", 6, EquipmentSlotGroup.HEAD)
								.addFlag(ItemFlag.HIDE_UNBREAKABLE)
								.setName("&aDruid's Helmet")
								.setLore(NATURAL_ARMOR_LORE)
								.setMaxDurability(615)
								.get(), "DRUID_HELMET", RecipeType.NULL, null
						)
				);
		
		addArmorPiece(
				new ArmorSetPiece(ANOSF.key("NATURAL_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
							new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE)
								.setColor(Color.fromRGB(95, 150, 23))
								.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_CHESTPLATE", 10, EquipmentSlotGroup.CHEST)
								.addFlag(ItemFlag.HIDE_UNBREAKABLE)
								.setName("&aDruid's Chestplate")
								.setLore(NATURAL_ARMOR_LORE)
								.setMaxDurability(725)
								.get(), "DRUID_CHESTPLATE", RecipeType.NULL, null
						)
				);
		addArmorPiece(
				new ArmorSetPiece(ANOSF.key("NATURAL_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
							new LeatherArmorBuilder(Material.LEATHER_LEGGINGS)
								.setColor(Color.fromRGB(102, 109, 17))
								.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_LEGGINGS", 7, EquipmentSlotGroup.LEGS)
								.addFlag(ItemFlag.HIDE_UNBREAKABLE)
								.setName("&aDruid's Leggings")
								.setLore(NATURAL_ARMOR_LORE)
								.setMaxDurability(675)
								.get(), "DRUID_LEGGINGS", RecipeType.NULL, null
						)
				);
		addArmorPiece(
				new ArmorSetPiece(ANOSF.key("NATURAL_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
							new LeatherArmorBuilder(Material.LEATHER_BOOTS)
								.setColor(Color.fromRGB(109, 68, 11))
								.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_BOOTS", 5, EquipmentSlotGroup.FEET)
								.addFlag(ItemFlag.HIDE_UNBREAKABLE)
								.setName("&aDruid's Boots")
								.setLore(NATURAL_ARMOR_LORE)
								.setMaxDurability(650)
								.get(), "DRUID_BOOTS", RecipeType.NULL, null
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
					if(AbstractSFAddon.get().getLastSlimefunTick() % 10 == 0) {
						for(Entity e : p.getLocation().getNearbyEntities(10, 10, 10)) {
							if(e instanceof LivingEntity) {
								LivingEntity entity = (LivingEntity) e;
								if(entity instanceof Player) continue;
								if(!entity.hasPotionEffect(PotionEffectType.WEAKNESS)) {
									entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0));
								}
								if(!entity.hasPotionEffect(PotionEffectType.SLOWNESS)) {
									entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 0));
								}
							}
						}
					}
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 1));
					if(!p.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 500000000, 2));
					}
				} else {
					if(this.equipped.get(p.getUniqueId()) == true) {
						this.equipped.put(p.getUniqueId(), false);
						p.removePotionEffect(PotionEffectType.REGENERATION);
						p.removePotionEffect(PotionEffectType.HEALTH_BOOST);
					};
				}
			}
		};
	}

}
