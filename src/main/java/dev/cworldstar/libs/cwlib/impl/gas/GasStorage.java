package dev.cworldstar.libs.cwlib.impl.gas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import dev.cworldstar.libs.cwlib.impl.SlimefunGas;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public abstract class GasStorage {

	protected ArrayList<StoredGas> gasStorage = new ArrayList<>();
	
	public abstract float getAtmospherePressure();
	
	public float getSize() {
		return 101F;
	};
	
	public void addGas(StoredGas gas) {
		gasStorage.add(gas);
	}
	
	public void editGas(Consumer<List<StoredGas>> gasEditor) {
		gasEditor.accept(gasStorage);
	}
	
	public Optional<StoredGas> getGas(SlimefunGas gas) {
		return gasStorage.stream().filter(cgas -> cgas.getGasId().equals(gas.getGasId())).findFirst();
	}
	
	public List<StoredGas> gases() {
		return this.gasStorage;
	}
	
	@AllArgsConstructor
	public static final class StoredGas implements Serializable {
		private static final long serialVersionUID = -6979484728945063062L;
		private @Getter String gasId;
		private @Getter @Setter float volume;
		public StoredGas(SlimefunGas slimefunGas, float value) {
			this.gasId = slimefunGas.getGasId();
			volume = value;
		}
		/**
		 * Gets the pressure that this {@link StoredGas} would provide
		 * given the current volume.
		 * @return
		 */
		public float getPressure() {
			return toSlimefunGas().pressure(volume);
		}
		public void addVolume(float value) {
			volume += value;
		}
		public boolean isOfType(SlimefunGas gas) {
			return gas.getGasId().contentEquals(this.getGasId());
		}
		public SlimefunGas toSlimefunGas() {
			return SlimefunGas.getRegistry().stream().filter(gas -> gas.getGasId().contentEquals(this.gasId)).findFirst().orElse(SlimefunGas.AIR);
		}
	}
}
