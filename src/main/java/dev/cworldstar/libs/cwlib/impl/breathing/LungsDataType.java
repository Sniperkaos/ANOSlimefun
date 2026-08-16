package dev.cworldstar.libs.cwlib.impl.breathing;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class LungsDataType implements PersistentDataType<byte[], Lungs> {

	@Override
	public @NotNull Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}

	@Override
	public @NotNull Class<Lungs> getComplexType() {
		return null;
	}

	@Override
	public @NotNull byte[] toPrimitive(@NotNull Lungs complex, @NotNull PersistentDataAdapterContext context) {
		return complex.toBytes();
	}

	@Override
	public @NotNull Lungs fromPrimitive(byte[] primitive, @NotNull PersistentDataAdapterContext context) {
		return Lungs.fromBytes(primitive);
	}

}
