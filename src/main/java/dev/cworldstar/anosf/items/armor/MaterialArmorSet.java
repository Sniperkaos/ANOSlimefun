package dev.cworldstar.anosf.items.armor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ArmorBuilder;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet;
import dev.cworldstar.libs.cwlib.impl.armor.ArmorSetPiece;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public abstract class MaterialArmorSet extends AbstractArmorSet {
	
	protected Map<UUID, Boolean> equipped = new HashMap<UUID, Boolean>();
	
	public MaterialArmorSet(
			String setID,
			String[] armorIDs,
			Material[] armorMaterials,
			int[] durabilities,
			Map<Enchantment, Integer> enchants,
			ArrayList<Map<Attribute, AttributeModifier[]>> hashSet,
			String[] lore,
			String[] names,
			ItemStack[][] recipes
	) {
		super(ANOSF.key(setID));
		NamespacedKey key = ANOSF.key(setID);
		int current = 0;
		for(Material material : armorMaterials) {
			ArmorBuilder armorBuilder = new ArmorBuilder(material);
			for(Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
				armorBuilder.enchant(enchant.getKey(), enchant.getValue());
			}
			for(Entry<Attribute, AttributeModifier[]> modifierList : hashSet.get(current).entrySet()) {
				for(AttributeModifier modifier : modifierList.getValue()) {
					armorBuilder.attribute(modifierList.getKey(), modifier);
				}
			}
			armorBuilder.lore(lore);
			armorBuilder.name(names[current]);
			armorBuilder.setMaxDurability(durabilities[current]);
			addArmorPiece(
				new ArmorSetPiece(
					key, 
					ItemRegistry.getItemGroup("ARMOR_CATEGORY"),
					armorBuilder.build(),
					armorIDs[current].toUpperCase(),
					RecipeType.ARMOR_FORGE,
					recipes[current]
				)
			);
			current += 1;
		}
	}
}
