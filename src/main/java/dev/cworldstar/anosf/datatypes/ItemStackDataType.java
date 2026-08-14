package dev.cworldstar.anosf.datatypes;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ItemStackDataType implements PersistentDataType<byte[], ItemStack> {

	@Override
	public @NotNull Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}

	@Override
	public @NotNull Class<ItemStack> getComplexType() {
		// TODO Auto-generated method stub
		return ItemStack.class;
	}

	@Override
	public @NotNull byte[] toPrimitive(ItemStack complex, @NotNull PersistentDataAdapterContext context) {
		return complex.serializeAsBytes();
	}

	@Override
	public ItemStack fromPrimitive(@NotNull byte [] primitive, @NotNull PersistentDataAdapterContext context) {
		return ItemStack.deserializeBytes(primitive);
	}

}
