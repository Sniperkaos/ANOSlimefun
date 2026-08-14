package dev.cworldstar.anosf.items.armor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.LeatherArmorBuilder;
import dev.cworldstar.libs.cwlib.handlers.TickHandler;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet;
import dev.cworldstar.libs.cwlib.impl.armor.ArmorSetPiece;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;

public class CosmicArmorSet extends AbstractArmorSet implements Listener {

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		Entity entity = e.getEntity();
		if(entity instanceof Player) {
			Player player = (Player) entity;
			if(this.active(player)) {
				e.setCancelled(true);
			}
		}
	}
	
	@Getter
	private static final List<PotionEffectType> negativeEffects = Arrays.asList(new PotionEffectType[] {
			PotionEffectType.BAD_OMEN,
			PotionEffectType.BLINDNESS,
			PotionEffectType.DARKNESS,
			PotionEffectType.HUNGER,
			PotionEffectType.WITHER,
			PotionEffectType.WEAKNESS,
			PotionEffectType.UNLUCK,
			PotionEffectType.SLOWNESS,
			PotionEffectType.MINING_FATIGUE,
			PotionEffectType.NAUSEA,
			PotionEffectType.POISON
	});
	
	private static final String[] COSMIC_ARMOR_LORE = new String[] {
			"<italic><gradient:gold:#f7c662:#fadb9d>I was once asked a question: Where is the path forward?",
			"<italic><gradient:#f7c662:#fadb9d:gold>I could not answer, for when I looked down...",
			"<italic><gradient:dark_red:#d18b06:light_purple>my feet were gone, merged with the infinite cosmos.",
			"",
			"<gradient:white:gold><underlined>Cosmic Set Abilities:", 
			" <gray>[<gradient:yellow:gold>Unbound</gradient>]: - All damage taken is <red>negated</red>.",
			" <gray>[<gradient:yellow:gold>4th Dimensional</gradient>]: - Every <green>positive</green> potion effect is applied to you, and",
			" <gray>every <red>negative</red> potion effect is removed from you.",
			" <gray>[<gradient:red:gold>Pressure III</gradient>]: - Deals a constant <red>15</red> damage to nearby hostile mobs.",
	};
	
	@Getter
	private static final Map<PotionEffectType, Integer> positiveEffects = new HashMap<PotionEffectType, Integer>();
	
	static {
		positiveEffects.putAll(Map.of(
				PotionEffectType.WATER_BREATHING, 11,
				PotionEffectType.STRENGTH, 11,
				PotionEffectType.SATURATION, 11,
				PotionEffectType.REGENERATION, 11,
				PotionEffectType.NIGHT_VISION, 11		
		));
		positiveEffects.putAll(Map.of(
				PotionEffectType.ABSORPTION, 11,
				PotionEffectType.CONDUIT_POWER, 11,
				PotionEffectType.DOLPHINS_GRACE, 11,
				PotionEffectType.FIRE_RESISTANCE, 11,
				PotionEffectType.HASTE, 11,
				PotionEffectType.HEALTH_BOOST, 11,
				PotionEffectType.HERO_OF_THE_VILLAGE, 11,
				PotionEffectType.JUMP_BOOST, 1,
				PotionEffectType.SPEED, 1,
				PotionEffectType.LUCK, 11		
		));
	}
	
	private Map<UUID, Boolean> equipped = new HashMap<UUID, Boolean>();
	
	public CosmicArmorSet() {
		super(ANOSF.key("COSMIC_ARMOR_SET"));
		
		Bukkit.getPluginManager().registerEvents(this, ANOSF.get());
		
		addArmorPiece(
			new ArmorSetPiece(ANOSF.key("COSMIC_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
					new LeatherArmorBuilder(Material.LEATHER_HELMET)
						.setColor(Color.WHITE)
						.setUnbreakable(true)
						.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR", 500, EquipmentSlotGroup.BODY)
						.setAttributeValue(Attribute.KNOCKBACK_RESISTANCE, "ARMOR_SET_KB_RESIST", 1, EquipmentSlotGroup.BODY)
						.setName("<gradient:white:gold>Cosmic Helmet")
						.setLore(COSMIC_ARMOR_LORE).get(), 
					"COSMIC_HELMET", 
					RecipeType.ARMOR_FORGE, 
					new ItemStack[] {}
			)
		);
		
		ArmorSetPiece piece = new ArmorSetPiece(ANOSF.key("COSMIC_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
				new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE)
				.setColor(Color.fromRGB(245, 243, 155))
				.setUnbreakable(true)
				.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_CHEST", 750, EquipmentSlotGroup.BODY)
				.setAttributeValue(Attribute.KNOCKBACK_RESISTANCE, "ARMOR_SET_KB_RESIST_CHEST", 1, EquipmentSlotGroup.BODY)
				.setName("<gradient:white:gold>Cosmic Chestplate")
				.setLore(COSMIC_ARMOR_LORE).get(), 
			"COSMIC_CHESTPLATE", 
			RecipeType.ARMOR_FORGE, 
			new ItemStack[] {}
		);
		
		addArmorPiece(
				piece
		);
		addArmorPiece(
				new ArmorSetPiece(ANOSF.key("COSMIC_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
						new LeatherArmorBuilder(Material.LEATHER_LEGGINGS)
							.setColor(Color.fromRGB(245, 243, 129))
							.setUnbreakable(true)
							.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_LEGS", 690, EquipmentSlotGroup.LEGS)
							.setAttributeValue(Attribute.KNOCKBACK_RESISTANCE, "ARMOR_SET_KB_RESIST_LEGS", 1, EquipmentSlotGroup.LEGS)							.setName("<gradient:white:gold>Cosmic Leggings")
							.setLore(COSMIC_ARMOR_LORE).get(), 
						"COSMIC_LEGGINGS", 
						RecipeType.ARMOR_FORGE, 
						new ItemStack[] {}
				)
		);
		addArmorPiece(
				new ArmorSetPiece(ANOSF.key("COSMIC_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
						new LeatherArmorBuilder(Material.LEATHER_BOOTS)
							.setColor(Color.fromRGB(245, 242, 77))
							.setUnbreakable(true)
							.setAttributeValue(Attribute.ARMOR, "ARMOR_SET_ARMOR_BOOTS", 600, EquipmentSlotGroup.FEET)
							.setAttributeValue(Attribute.KNOCKBACK_RESISTANCE, "ARMOR_SET_KB_RESIST_BOOTS", 1, EquipmentSlotGroup.FEET)
							.setName("<gradient:white:gold>Cosmic Boots")
							.setLore(COSMIC_ARMOR_LORE).get(), 
						"COSMIC_BOOTS", 
						RecipeType.ARMOR_FORGE, 
						new ItemStack[] {}
				)		
		);
		
		piece.addItemHandler(new TickHandler() {
			@Override
			public void onTick(SlimefunItem thisItem, Player p, ItemStack item, int slot) {
				if(CosmicArmorSet.this.active(p)) {
					if(!p.hasMetadata("cosmicSetActive")) {
						p.setMetadata("cosmicSetActive", new FixedMetadataValue(ANOSF.get(), true));
					}
					for(Entry<PotionEffectType, Integer> entry : getPositiveEffects().entrySet()) {
						PotionEffect effect = new PotionEffect(entry.getKey(), 320, entry.getValue());
						effect.apply(p);
					}
					for(PotionEffect effect : p.getActivePotionEffects()) {
						if(getNegativeEffects().contains(effect.getType())) {
							p.removePotionEffect(effect.getType());
						}
					}
					
					for(Entity e : p.getNearbyEntities(12, 12, 12)) {
						if(e instanceof Enemy) {
							((LivingEntity) e).damage(15, DamageSource.builder(DamageType.MAGIC).withDamageLocation(e.getLocation()).build());
						}
						if(e instanceof LivingEntity && !(e instanceof Player)) {
							LivingEntity entity = (LivingEntity) e;
							entity.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 11, 60));
							entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 11, 60));

						}
					}
				} else {
					if(p.hasMetadata("cosmicSetActive")) {
						p.removeMetadata("cosmicSetActive", ANOSF.get());
						for(PotionEffect effect : p.getActivePotionEffects()) {
							if(getPositiveEffects().containsKey(effect.getType())) {
								p.removePotionEffect(effect.getType());
							}
						}
						p.setAllowFlight(false);
					}
				}
			}
		});	
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
