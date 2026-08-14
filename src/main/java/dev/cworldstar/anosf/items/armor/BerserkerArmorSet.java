package dev.cworldstar.anosf.items.armor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet;
import dev.cworldstar.libs.cwlib.impl.armor.ArmorSetPiece;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.LeatherArmorBuilder;
import dev.cworldstar.libs.cwlib.handlers.PlayerAttackHandler;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class BerserkerArmorSet extends AbstractArmorSet {
	
	private Map<UUID, Boolean> equipped = new HashMap<UUID, Boolean>();
	
	private static final String[] BERSERKER_ARMOR_LORE = new String[] {
			"<italic><gradient:red:dark_red>My rage is unyielding- unrelenting,",
			"<italic><gradient:dark_red:red>like waves crashing against the rocks.",
			"",
			"<red><underlined>Berserker Set Abilities:", 
			" <gray>[<gold>Robust<gray>]: - <gray>You heal 5% of your damage dealt, and are given",
			" <green>Life Boost III</green>",
			" <gray>[<gold>Enraged<gray>]: - You deal up to <red>an extra 100%<gray> damage,",
			" <gray>based on your missing health.",
	};
	
	public BerserkerArmorSet() {
		super(ANOSF.key("BERSERKER_ARMOR_SET"));
		
		addArmorPiece(
				new ArmorSetPiece(ANOSF.key("BERSERKER_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
							new LeatherArmorBuilder(Material.LEATHER_HELMET)
								.setColor(Color.GREEN)
								.addUnsafeEnchantment(Enchantment.PROTECTION, 5)
								.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_HELMET", 7, EquipmentSlotGroup.HEAD)
								.addFlag(ItemFlag.HIDE_UNBREAKABLE)
								.setName("<gradient:red:dark_red:dark_red>Berserker's Helmet")
								.setLore(BERSERKER_ARMOR_LORE)
								.setMaxDurability(750)
								.get(), "BERSERKER_HELMET", RecipeType.NULL, null
						)
				);
		
		
		ArmorSetPiece piece = new ArmorSetPiece(ANOSF.key("BERSERKER_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
				new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE)
				.setColor(Color.ORANGE)
				.addUnsafeEnchantment(Enchantment.PROTECTION, 5)
				.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_CHEST", 10, EquipmentSlotGroup.HEAD)
				.addFlag(ItemFlag.HIDE_UNBREAKABLE)
				.setName("<gradient:red:dark_red:dark_red>Berserker's Chestplate")
				.setLore(BERSERKER_ARMOR_LORE)
				.setMaxDurability(1080)
				.get(), "BERSERKER_CHESTPLATE", RecipeType.NULL, null
		);
		
		piece.addItemHandler(new PlayerAttackHandler() {
			@Override
			public boolean onPlayerAttack(EntityDamageEvent e, Player player, @NotNull Entity entity) {
				if(BerserkerArmorSet.this.active(player)) {
					double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
					double damageIncrease = (maxHealth - player.getHealth());
					e.setDamage(e.getDamage() + (e.getDamage() * (damageIncrease/maxHealth)));
					if(damageIncrease > 50) {
						Location entityloc = entity.getLocation();
						entityloc.getWorld().spawnParticle(Particle.DRIPPING_LAVA, 3, 3, 3, 8);
						entityloc.getWorld().playSound(entityloc, Sound.ENTITY_WARDEN_ATTACK_IMPACT, 1, (float) 0.1);
					}
					player.setHealth(Math.max(0, Math.min(maxHealth, player.getHealth() + (e.getDamage() * 0.05))));
				}
				return false;
			}
		});
		
		addArmorPiece(piece);
				
		addArmorPiece(
				new ArmorSetPiece(ANOSF.key("BERSERKER_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
							new LeatherArmorBuilder(Material.LEATHER_LEGGINGS)
								.setColor(Color.RED)
								.addUnsafeEnchantment(Enchantment.PROTECTION, 5)
								.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_LEGGINGS", 6, EquipmentSlotGroup.LEGS)
								.addFlag(ItemFlag.HIDE_UNBREAKABLE)
								.setName("<gradient:red:dark_red:dark_red>Berserker's Leggings")
								.setLore(BERSERKER_ARMOR_LORE)
								.setMaxDurability(854)
								.get(), "BERSERKER_LEGGINGS", RecipeType.NULL, null
						)
				);
		addArmorPiece(
				new ArmorSetPiece(ANOSF.key("BERSERKER_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
							new LeatherArmorBuilder(Material.LEATHER_BOOTS)
								.setColor(Color.RED)
								.addUnsafeEnchantment(Enchantment.PROTECTION, 5)
								.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_BOOTS", 4, EquipmentSlotGroup.FEET)
								.addFlag(ItemFlag.HIDE_UNBREAKABLE)
								.setName("<gradient:red:dark_red:dark_red>Berserker's Boots")
								.setLore(BERSERKER_ARMOR_LORE)
								.setMaxDurability(750)
								.get(), "BERSERKER_BOOTS", RecipeType.NULL, null
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
					if(!p.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 999999999, 2));
					}
				} else {
					if(this.equipped.get(p.getUniqueId()) == true) {
						this.equipped.put(p.getUniqueId(), false);
						p.removePotionEffect(PotionEffectType.HEALTH_BOOST);
					};
				}
			}
		};
	}

}
