package dev.cworldstar.libs.cwlib.builders;

import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ArmorMeta;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;

public class ArmorBuilder extends ItemStackBuilder {
	public ArmorBuilder(Material armor) {
		super(armor);
		Validate.isInstanceOf(ArmorMeta.class, item.getItemMeta(), "The given Material must contain a default item meta which is an instance of ArmorMeta!");
	}
	
	public ArmorBuilder setAttributeValue(Attribute attr, String id, int amount, EquipmentSlotGroup slot) {
		ArmorMeta meta = (ArmorMeta) item.getItemMeta();
		meta.addAttributeModifier(attr, new AttributeModifier(AbstractSFAddon.key(id), amount, Operation.ADD_NUMBER, slot));
		item.setItemMeta(meta);
		return this;
	}
	
	public ArmorBuilder armor(int armor) {
		return setAttributeValue(Attribute.ARMOR, UUID.randomUUID().toString(), armor, EquipmentSlotGroup.ARMOR);
	}
	
	public ArmorBuilder toughness(int toughness) {
		return setAttributeValue(Attribute.ARMOR_TOUGHNESS, UUID.randomUUID().toString(), toughness, EquipmentSlotGroup.ARMOR);
	}

	public ArmorBuilder addFlag(ItemFlag flag) {
		item.addItemFlags(flag);
		return this;
	}

	public ArmorBuilder setUnbreakable(boolean b) {
		item.editMeta(meta -> {
			meta.setUnbreakable(b);
		});
		return this;
	}
	
	/**
	 * Adds an unsafe enchantment. Deprecated in favor of {@link #enchant(Enchantment, int)}.
	 * @param enchant
	 * @param level
	 * @return
	 */
	@Deprecated
	public ArmorBuilder addUnsafeEnchantment(Enchantment enchant, int level) {
		enchant(enchant, level);
		return this;
	}
}
