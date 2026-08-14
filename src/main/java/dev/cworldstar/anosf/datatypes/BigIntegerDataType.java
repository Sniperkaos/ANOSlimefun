package dev.cworldstar.anosf.datatypes;

import java.math.BigInteger;
import java.util.Base64;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class BigIntegerDataType implements PersistentDataType<byte[], BigInteger> {
	@Override
	public @NotNull Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}

	@Override
	public @NotNull Class<BigInteger> getComplexType() {
		return BigInteger.class;
	}

	@Override
	public byte[] toPrimitive(@NotNull BigInteger complex,
			@NotNull PersistentDataAdapterContext context) {
		return Base64.getEncoder().encode(complex.toByteArray());
	}

	@Override
	public @NotNull BigInteger fromPrimitive(byte[] primitive,
			@NotNull PersistentDataAdapterContext context) {
		return new BigInteger(Base64.getDecoder().decode(primitive));
	}
}

