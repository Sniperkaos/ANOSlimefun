package dev.cworldstar.libs.cwlib;

import org.apache.commons.lang3.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

public final class StaticAttributeModifiers {
	
	public StaticAttributeModifiers() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This is a static class!");
	}
	
	public static final class AttributeModifierBuilder {
		private EquipmentSlotGroup group = EquipmentSlotGroup.ANY;
		private Operation op = Operation.ADD_NUMBER;
		private NamespacedKey key = AbstractSFAddon.key("DEFAULT_KEY");
		private double amount = 0D;
		
		public AttributeModifierBuilder() {
			
		}
		
		public AttributeModifierBuilder key(NamespacedKey key) {
			this.key = key;
			return this;
		}
		
		public AttributeModifierBuilder amount(double amount) {
			this.amount = amount;
			return this;
		}
		
		public AttributeModifierBuilder group(EquipmentSlotGroup group) {
			this.group = group;
			return this;
		}
		
		public AttributeModifier build() {
			Validate.notNull(group, "Equipment group must not be null!");
			Validate.notNull(op, "Operation must not be null!");
			Validate.notNull(key, "NamespacedKey must not be null!");

			return new AttributeModifier(key, amount, op, group);
		}
		
		public AttributeModifierBuilder operation(Operation operation) {
			op = operation;
			return this;
		}
	}
	
	public static final class HelmetAttributeModifier extends AttributeModifier {
		public HelmetAttributeModifier(String key, double amount, @NotNull Operation operation,
				@NotNull EquipmentSlotGroup slot) {
			super(AbstractSFAddon.key(key), amount, operation, EquipmentSlotGroup.HEAD);
		}
	}
	public static final class ChestplateAttributeModifier extends AttributeModifier {
		public ChestplateAttributeModifier(String key, double amount, @NotNull Operation operation,
				@NotNull EquipmentSlotGroup slot) {
			super(AbstractSFAddon.key(key), amount, operation, EquipmentSlotGroup.CHEST);
		}
	}
	public static final class LeggingAttributeModifier extends AttributeModifier {
		public LeggingAttributeModifier(String key, double amount, @NotNull Operation operation,
				@NotNull EquipmentSlotGroup slot) {
			super(AbstractSFAddon.key(key), amount, operation, EquipmentSlotGroup.LEGS);
		}
	}
	public static final class BootsAttributeModifier extends AttributeModifier {
		public BootsAttributeModifier(String key, double amount, @NotNull Operation operation,
				@NotNull EquipmentSlotGroup slot) {
			super(AbstractSFAddon.key(key), amount, operation, EquipmentSlotGroup.FEET);
		}
	}
}
