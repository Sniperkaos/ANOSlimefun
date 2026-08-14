package dev.cworldstar.libs.cwlib.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.YamlConfiguration;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;

public class ConfigUtils {
	public ConfigUtils() {
		throw new IllegalStateException("Static class");
	}
	
	public static void saveDefault(File toSave, String resource) {
		if(!toSave.exists()) {
			try {
				toSave.createNewFile();
				InputStreamReader reader = new InputStreamReader(AbstractSFAddon.get().getResource(resource));
				YamlConfiguration normal = YamlConfiguration.loadConfiguration(reader);
				normal.save(toSave);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}