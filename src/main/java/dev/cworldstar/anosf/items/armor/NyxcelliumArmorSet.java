package dev.cworldstar.anosf.items.armor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.LeatherArmorBuilder;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet;
import dev.cworldstar.libs.cwlib.impl.armor.ArmorSetPiece;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;

public class NyxcelliumArmorSet extends AbstractArmorSet {
	
	private static final String[] NYXCELLIUM_ARMOR_LORE = new String[] {
			"<italic><gradient:dark_green:gray:#964B00>Now I can finally remove mycelium from the world!",
			"<italic><gradient:#964B00:dark_green:gray>Just what I wanted, what a terrible block!",
			"",
			"<gradient:#964B00:dark_green:gray><underlined>Nyxcellum Set Abilities:", 
			" <gray>[<gradient:#964B00:dark_green:gray>Green Thumb<gray>]: - Nearby mycelium blocks are converted to <green>grass<gray>.",
			" <gray>[<gradient:#964B00:dark_green:gray>Worn<gray>]: - Gain <yellow>Speed III<gray> + <blue>Jump Boost II<gray>.",
			" "
	};
	
	private Map<UUID, Boolean> equipped = new HashMap<UUID, Boolean>();
		public NyxcelliumArmorSet() {
			super(ANOSF.key("NYXCELLIUM_ARMOR_SET"));
			addArmorPiece(new ArmorSetPiece(ANOSF.key("NYXCELLIUM_ARMOR_SET"), ItemRegistry.getItemGroup("armor_category"),
					new LeatherArmorBuilder(Material.LEATHER_BOOTS).setColor(Color.fromRGB(150, 75, 0)).attribute("ARMOR_BUFF", Attribute.ARMOR, 12, EquipmentSlotGroup.FEET, Operation.ADD_NUMBER).setMaxDurability(1337).name("<gradient:dark_green:gray:#964B00>Nyxcellium Boots").lore(NYXCELLIUM_ARMOR_LORE).build(), "NYXCELLIUM_BOOTS", 
					RecipeType.ARMOR_FORGE,
					new ItemStack[] {
							null,null,null,
							ItemRegistry.getRegistryItem("NYXCELLIUM_INGOT").getItem(),null,ItemRegistry.getRegistryItem("NYXCELLIUM_INGOT").getItem(),
							ItemRegistry.getRegistryItem("NYXCELLIUM_INGOT").getItem(),null,ItemRegistry.getRegistryItem("NYXCELLIUM_INGOT").getItem()
					}
			));
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
						
						for(int x=-3; x<=3; x++) {
							for(int y=-3; y<=3; y++) {
								for(int z=-3; z<=3; z++) {
									Location loc = p.getLocation().add(new Vector(x,y,z));
									Block below = loc.getBlock();
									if(below.getType().equals(Material.MYCELIUM)) {
										if(Slimefun.getProtectionManager().hasPermission(p, below, Interaction.PLACE_BLOCK)) {
											below.setType(Material.GRASS_BLOCK);
										}
									}
								}	
							}
						}
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 2));
						p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 60, 1));
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
