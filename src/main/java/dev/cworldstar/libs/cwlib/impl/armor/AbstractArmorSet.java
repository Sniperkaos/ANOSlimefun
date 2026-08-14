package dev.cworldstar.libs.cwlib.impl.armor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;

/**
 * Abstract class for creating armor sets.
 * The code is a little outdated, but it still
 * should work as intended.
 * 
 * @author cworldstar
 */
public abstract class AbstractArmorSet implements ArmorSetHolder {
	
	public static enum ArmorContext {
		HELMET,
		CHEST,
		LEGS,
		BOOTS
	}
	
	protected ProtectionType[] getProtectionTypes() {
		return new ProtectionType[0];
	};
	
	protected Map<UUID, Boolean> equipped = new HashMap<UUID, Boolean>();
	
	private static Map<NamespacedKey, AbstractArmorSet> sets = new HashMap<NamespacedKey, AbstractArmorSet>();
	
	public static void onTick() {
		sets.forEach((NamespacedKey key, AbstractArmorSet set) -> {
			set.armorTick().accept(set);
		});
	}
	
	public static AbstractArmorSet getSet(NamespacedKey key) {
		return sets.get(key);
	}
	
	public static void addSet(NamespacedKey setId, AbstractArmorSet set) {
		AbstractArmorSet.sets.put(setId, set);
	}
	
	public static final boolean isWearing(Player player, NamespacedKey set) {
		return getSet(set).active(player);
	}
	
	public static final boolean isWearing(LivingEntity player, NamespacedKey set) {
		return getSet(set).active(player);
	}
	
	public final Consumer<AbstractArmorSet> DEFAULT_ARMOR_TICK = (AbstractArmorSet set) -> {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!equipped.containsKey(p.getUniqueId())) {
				equipped.put(p.getUniqueId(), false);
			}
			boolean active = set.active(p);
			if(active) {
				equipped.put(p.getUniqueId(), true);
			} else {
				if(equipped.get(p.getUniqueId()) == true) {
					equipped.put(p.getUniqueId(), false);
				};
			}
		}
	};

	public Consumer<AbstractArmorSet> potionArmorTick(PotionEffect ...effects) {
		return (AbstractArmorSet set) -> {	
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(!equipped.containsKey(p.getUniqueId())) {
					equipped.put(p.getUniqueId(), false);
				}
				boolean active = set.active(p);
				if(active) {
					equipped.put(p.getUniqueId(), true);
					for(PotionEffect effect : effects) {
						if(!p.hasPotionEffect(effect.getType())) {
							p.addPotionEffect(effect);
						}
					}
				} else {
					if(this.equipped.get(p.getUniqueId()) == true) {
						equipped.put(p.getUniqueId(), false);
						for(PotionEffect effect : effects) {
							if(p.hasPotionEffect(effect.getType())) {
								p.removePotionEffect(effect.getType());
							}
						}
					};
				}
			}
		};
	}		
	
	private NamespacedKey setId;
	private ArrayList<ArmorSetPiece> pieces;
	public abstract Consumer<AbstractArmorSet> armorTick();
	
	public int getPieces(LivingEntity p) {
		int pieces = 0;
		if((p.getEquipment()) == null) {
			
		}
		for(ItemStack item : p.getEquipment().getArmorContents()) {
			if(item == null) {
				continue;
			}
			SlimefunItem sfItem = SlimefunItem.getByItem(item);
			if(sfItem == null) {
				continue;
			}
			if(sfItem instanceof ArmorSetPiece thisPiece) {
				if(setId.equals(thisPiece.getArmorSetId())) {
					pieces += 1;
				}
			}
		}
		return pieces;
	}
	
	public int getPieces(Player p) {
		int pieces = 0;
		for(ItemStack item : p.getInventory().getArmorContents()) {
			if(item == null) {
				continue;
			}
			SlimefunItem sfItem = SlimefunItem.getByItem(item);
			if(sfItem == null) {
				continue;
			}
			if(sfItem instanceof ArmorSetPiece thisPiece) {
				if(setId.equals(thisPiece.getArmorSetId())) {
					pieces += 1;
				}
			}
		}
		return pieces;
	}
	
	public boolean active(Player p) {
		int pieces = getPieces(p);
		return pieces >= this.pieces.size();
	}
	
	public boolean active(LivingEntity p) {
		int pieces = getPieces(p);
		return pieces >= this.pieces.size();
	}
	
	public NamespacedKey getKey() {
		return setId;
	}
	
	public Set<ArmorSetPiece> getPieceIterator() {
		return pieces.stream().collect(Collectors.toUnmodifiableSet());
	}
	
	public boolean isRelated(ArmorSetPiece piece) {
		for(ArmorSetPiece localPiece : getPieceIterator()) {
			if(piece.equals(localPiece)) {
				return true;
			}
		}
		return false;
	}
	
	public List<ArmorSetPiece> getPieces() {
		return pieces.stream().collect(Collectors.toUnmodifiableList());
	}
	
	public AbstractArmorSet(ArrayList<ArmorSetPiece> pieces, NamespacedKey setId) {
		this.pieces = pieces;
		this.setId = setId;
		AbstractArmorSet.addSet(setId, this);
	}
	
	public AbstractArmorSet(NamespacedKey setId) {
		this(new ArrayList<ArmorSetPiece>(), setId);
	}
	
	/**
	 * Adds an armor set piece to this ArmorSet. If the {@link ArmorSetPiece} has no setId, 
	 * it will set it to this armor set's setId.
	 * @param armorPiece
	 * @return
	 */
	public AbstractArmorSet addArmorPiece(ArmorSetPiece armorPiece) {
		armorPiece.setArmorSetId(setId);
		pieces.add(armorPiece);
		return this;
	}
}
