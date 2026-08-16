package dev.cworldstar.libs.cwlib.impl.gas;

import java.io.Serializable;

import org.bukkit.NamespacedKey;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChunkGasStorage extends GasStorage implements Serializable {
	private static final long serialVersionUID = -8653132395335542039L;
	private static final transient NamespacedKey CHUNK_GAS_STORAGE_KEY = AbstractSFAddon.key("CWLIB_CHUNK_GAS_STORAGE");
	public static final transient float CHUNK_BLOCK_SIZE = 54158F;

	public static float getChunkSize() {
		return CHUNK_BLOCK_SIZE;
	}
	
	public float getAtmospherePressure() {
		float atmospherePressure = 0;
		for(StoredGas gas : gasStorage) {
			atmospherePressure += gas.getPressure();
		}
		return atmospherePressure;
	}

	private static final ChunkGasStorageDataType type = new ChunkGasStorageDataType();
	
	public static ChunkGasStorageDataType dataType() {
		return type;
	}

	@Override
	public float getSize() {
		return CHUNK_BLOCK_SIZE;
	}

	public static NamespacedKey key() {
		return CHUNK_GAS_STORAGE_KEY;
	}
}
