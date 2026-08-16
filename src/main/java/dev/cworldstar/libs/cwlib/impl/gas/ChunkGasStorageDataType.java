package dev.cworldstar.libs.cwlib.impl.gas;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ChunkGasStorageDataType implements PersistentDataType<byte[], ChunkGasStorage> {
	@Override
	public @NotNull Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}

	@Override
	public @NotNull Class<ChunkGasStorage> getComplexType() {
		return ChunkGasStorage.class;
	}

	@Override
	public @NotNull byte[] toPrimitive(@NotNull ChunkGasStorage complex, @NotNull PersistentDataAdapterContext context) {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ObjectOutputStream objectOutput = new ObjectOutputStream(output);
			objectOutput.writeObject(complex);
			objectOutput.close();
			return output.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	@Override
	public @NotNull ChunkGasStorage fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
		try {
			ByteArrayInputStream input = new ByteArrayInputStream(primitive);
			ObjectInputStream objectInput = new ObjectInputStream(input);
			ChunkGasStorage storage = (ChunkGasStorage) objectInput.readObject();
			objectInput.close();
			return storage;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
