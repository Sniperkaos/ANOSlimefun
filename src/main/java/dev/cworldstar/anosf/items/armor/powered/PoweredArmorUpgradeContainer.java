package dev.cworldstar.anosf.items.armor.powered;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

import dev.cworldstar.anosf.ANOSF;

public class PoweredArmorUpgradeContainer {
	
	/**
	 * This is a map of a namespace and a key, since
	 * {@link NamespacedKey} does not extend serializable, as such
	 * we must serialize a hashmap and reconstruct it afterwards in the getter.
	 */
	private HashMap<String, String> upgrades;
	
	/**
	 * Gets a list of upgrades this container has.
	 * @return
	 */
	public List<NamespacedKey> getUpgrades() {
		return upgrades.entrySet().stream().map((entry) -> {
			return new NamespacedKey(entry.getKey(), entry.getValue());
		}).toList();
	}
	
	public static final NamespacedKey POWERED_ARMOR_UPGRADE_CONTAINER_KEY = ANOSF.key("POWERED_ARMOR_UPGRADE_CONTAINER");
	
	/**
	 * Deserializes this container from bytes.
	 * @param byte[] byteArray
	 */
	@SuppressWarnings("unchecked")
	public PoweredArmorUpgradeContainer(byte[] byteArray) {
		try {
			ByteArrayInputStream byteInput = new ByteArrayInputStream(byteArray);
			ObjectInputStream objectInput = new ObjectInputStream(byteInput);
			this.upgrades = (HashMap<String, String>) objectInput.readObject();
			objectInput.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public PoweredArmorUpgradeContainer() {
		upgrades = new HashMap<>();
	}

	/**
	 * Serializes this upgrade container into bytes.
	 * 
	 * TODO: optimize better
	 * 
	 * @author cworldstar
	 * @see #PoweredArmorUpgradeContainer(byte[]) To deserialize.
	 * @return The serialized upgrade container.
	 */
	public @Nullable byte[] toByteArray() {
		try {
			ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
			objectOutput.writeObject(upgrades);
			objectOutput.close();
			return byteOutput.toByteArray();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean equals(Object o) {
		return o.getClass().isInstance(o) &&
			((PoweredArmorUpgradeContainer) o).getUpgrades().containsAll(getUpgrades());
	}

	public void addUpgrade(NamespacedKey upgradeKey) {
		upgrades.putIfAbsent(upgradeKey.getNamespace(), upgradeKey.getKey());
	}
}