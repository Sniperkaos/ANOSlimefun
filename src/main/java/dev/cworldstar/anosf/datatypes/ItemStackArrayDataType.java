package dev.cworldstar.anosf.datatypes;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ItemStackArrayDataType implements PersistentDataType<byte[], ItemStack[]> {

	@Override
	public @NotNull Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}

	@Override
	public @NotNull Class<ItemStack[]> getComplexType() {
		return ItemStack[].class;
	}

	@Override
	public byte[] toPrimitive(ItemStack[] complex, @NotNull PersistentDataAdapterContext context) {
		return ItemStack.serializeItemsAsBytes(complex);
	}

	@Override
	public ItemStack[] fromPrimitive(byte[] primitive,
			@NotNull PersistentDataAdapterContext context) {
		return ItemStack.deserializeItemsFromBytes(primitive);
	}

}
