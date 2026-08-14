package dev.cworldstar.anosf.datatypes;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.anosf.items.armor.powered.PoweredArmorUpgradeContainer;

public class UpgradeContainerDataType implements PersistentDataType<byte[], PoweredArmorUpgradeContainer> {

	@Override
	public @NotNull Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}

	@Override
	public @NotNull Class<PoweredArmorUpgradeContainer> getComplexType() {
		return PoweredArmorUpgradeContainer.class;
	}

	@Override
	public byte[] toPrimitive(@NotNull PoweredArmorUpgradeContainer complex,
			@NotNull PersistentDataAdapterContext context) {
		return complex.toByteArray();
	}

	@Override
	public @NotNull PoweredArmorUpgradeContainer fromPrimitive(byte[] primitive,
			@NotNull PersistentDataAdapterContext context) {
		return new PoweredArmorUpgradeContainer(primitive);
	}

}

