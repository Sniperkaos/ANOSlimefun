package dev.cworldstar.libs.cwlib.impl;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.Getter;
import lombok.Setter;

public class StorageEntry<T> {
	public enum StorageEntryType {
		MATERIAL,
		SFITEM,
		NULL
	}
	@Getter
	private @Nonnull StorageEntryType storageType = StorageEntryType.NULL;
	@Getter
	private T entry = null;
	@Getter
	@Setter
	private @Nonnull long amount = 0L;
	
	public ConfigurationSection serialize(YamlConfiguration parent) {
		ConfigurationSection section = parent.createSection(String.valueOf(parent.getKeys(false).size()));
		section.set("entry", entry);
		section.set("type", storageType);
		section.set("amount", amount);
		return section;
	}
	
	public StorageEntry(T m, long amount) {
		if(m instanceof Material) {
			this.storageType = StorageEntryType.MATERIAL;
		} else if(m instanceof SlimefunItem) {
			this.storageType = StorageEntryType.SFITEM;
		} else {
			throw new UnsupportedOperationException("The given type must be Material or SlimefunItem!");
		}
		this.entry = m;
	}
	
	public void addAmount(long amount) {
		this.amount += amount;
	}
	
	public void subtractAmount(long amount) {
		this.amount -= amount;
	}
}
