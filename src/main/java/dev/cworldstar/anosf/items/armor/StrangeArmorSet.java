package dev.cworldstar.anosf.items.armor;

import java.util.UUID;
import java.util.function.Consumer;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.entities.TrueDamage;
import dev.cworldstar.anosf.entities.TrueDamage.TrueDamageType;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.LeatherArmorBuilder;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet;
import dev.cworldstar.libs.cwlib.impl.armor.ArmorSetPiece;

public class StrangeArmorSet extends AbstractArmorSet implements Listener {
	
	private static final NamespacedKey STRANGE_ARMOR_SET_KEY = ANOSF.key("STRANGE_ARMOR_SET");
	private static final String[] STRANGE_ARMOR_LORE = new String[] {
			"<italic><gradient:#BA55D3:#DA70D6>There are some things out in the cosmos, son. <underlined>Strange</underlined> things-",
			"<italic><gradient:#DA70D6:#BA55D3>unfathomable and indeterminable- a mere glimpse would shatter your mind to pieces.",
			"",
			"<gradient:#BA55D3:#DA70D6><underlined>Strange Set Abilities:", 
			" <gray>[<#BA55D3>Resistant</gradient>]: - <gray>Gain <white>Resistance X</white><gray>. 50% of damage dealt to you is reflected as true damage.",
			" <gray>[<#BA55D3>Indeterminable</gradient>]: - <gray>While sneaking, each tick you are disguised as a random mob.",

	};
	
	@EventHandler
	public void onPlayerDamaged(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Player)) return;
		DamageSource source = e.getDamageSource();
		if(source == null) return;
		if(source.getCausingEntity() == null) return;
		if(!(source.getCausingEntity() instanceof LivingEntity)) return;
		TrueDamage.dealTrueDamage((LivingEntity) source.getCausingEntity(), e.getDamage() * 0.50, TrueDamageType.LITERAL);
	}
	
	public void onEntityRender(Player player) {
		for(Player observer : Bukkit.getOnlinePlayers()) {
			if(observer.canSee(player)) {
				Location playerLocation = player.getLocation();
				PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
				PacketContainer disguisedPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
				StructureModifier<Integer> integers = disguisedPacket.getIntegers();
				integers.writeSafely(0, player.getEntityId());
				integers.writeSafely(1, RandomUtils.nextInt(0, 152));
				StructureModifier<Double> doubles = disguisedPacket.getDoubles();
				doubles.writeSafely(0, playerLocation.getX());
				doubles.writeSafely(1, playerLocation.getY());
				doubles.writeSafely(2, playerLocation.getZ());
				StructureModifier<UUID> uuids = disguisedPacket.getUUIDs();
				uuids.writeSafely(0, player.getUniqueId());

		        packet.getIntegerArrays().write(0, new int[]{
		        		player.getEntityId()
		        });
		        
				ProtocolLibrary.getProtocolManager().sendServerPacket(observer, packet);
				ProtocolLibrary.getProtocolManager().sendServerPacket(observer, disguisedPacket);
			}
		}
	}
	
	public StrangeArmorSet() {
		super(STRANGE_ARMOR_SET_KEY);
		
		addArmorPiece(
			new ArmorSetPiece(
				STRANGE_ARMOR_SET_KEY, 
				ItemRegistry.getItemGroup("armor_group"), 
				new LeatherArmorBuilder(Material.LEATHER_HELMET)
					.color(Color.fromRGB(218,112,214))
					.armor(65)
					.durability(30000)
					.name(ItemTier.STRANGE.makeName("Helmet"))
					.lore(STRANGE_ARMOR_LORE)
					.build(), 
				"STRANGE_ARMOR_HELMET"
			)
		);
		
		addArmorPiece(
				new ArmorSetPiece(
					STRANGE_ARMOR_SET_KEY, 
					ItemRegistry.getItemGroup("armor_group"), 
					new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE)
						.color(Color.fromRGB(147,112,219))
						.armor(125)
						.durability(30000)
						.name(ItemTier.STRANGE.makeName("Chestplate"))
						.lore(STRANGE_ARMOR_LORE)
						.build(), 
					"STRANGE_ARMOR_CHESTPLATE"
				)
			);
		
		addArmorPiece(
				new ArmorSetPiece(
					STRANGE_ARMOR_SET_KEY, 
					ItemRegistry.getItemGroup("armor_group"), 
					new LeatherArmorBuilder(Material.LEATHER_LEGGINGS)
						.color(Color.fromRGB(148,0,211))
						.armor(95)
						.durability(30000)
						.name(ItemTier.STRANGE.makeName("Leggings"))
						.lore(STRANGE_ARMOR_LORE)
						.build(), 
					"STRANGE_ARMOR_LEGGINGS"
				)
			);
		
		addArmorPiece(
				new ArmorSetPiece(
					STRANGE_ARMOR_SET_KEY, 
					ItemRegistry.getItemGroup("armor_group"), 
					new LeatherArmorBuilder(Material.LEATHER_BOOTS)
						.color(Color.fromRGB(75,0,130))
						.armor(70)
						.durability(30000)
						.name(ItemTier.STRANGE.makeName("Boots"))
						.lore(STRANGE_ARMOR_LORE)
						.build(), 
					"STRANGE_ARMOR_BOOTS"
				)
			);
	}

	private static final PotionEffect effect = new PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 9);
	
	@Override
	public Consumer<AbstractArmorSet> armorTick() {
		return set -> {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(!equipped.containsKey(p.getUniqueId())) {
					equipped.put(p.getUniqueId(), false);
				}
				boolean active = set.active(p);
				if(active) {
					if(p.isSneaking()) {
						onEntityRender(p);
					} else {
						
					}
					equipped.put(p.getUniqueId(), true);
					if(!p.hasPotionEffect(effect.getType())) {
						p.addPotionEffect(effect);
					}
				} else {
					if(this.equipped.get(p.getUniqueId()) == true) {
						equipped.put(p.getUniqueId(), false);
					};
					if(p.hasPotionEffect(effect.getType())) {
							p.removePotionEffect(effect.getType());
					}
				}
			}
		};
	}
}
