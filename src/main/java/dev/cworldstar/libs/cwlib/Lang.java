package dev.cworldstar.libs.cwlib;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.math.RandomUtils;
import net.kyori.adventure.text.Component;

public class Lang {
	
	private String language = "en-us";
	private boolean prefixEnabled = false;
	private Component prefix;
	
	private static HashMap<String, ConfigurationSection> LANG_CACHE = new HashMap<String, ConfigurationSection>();
	private static String DEFAULT_LANG = "<red><bold>An error occured finding lang section %lang%.</bold><red>";
	
	/**
	 * Shorthand of Map.of(String toLook, String replace)
	 * @param toLook
	 * @param replace
	 * @return
	 */
	public static Map<String, String> replacement(String toLook, String replace) {
		Map<String, String> replacements = new HashMap<String, String>();
		replacements.put(toLook, replace);
		return replacements;
	}
	
	/**
	 * Static shorthand of {@code ANOLC.getLang().get(@Nonnull String lang, @Nullable Map<String, String> replacements)}.
	 * @param lang
	 * @param replacements
	 * @return
	 */
	public static Component getComponent(@Nonnull String lang, @Nullable Map<String, String> replacements) {
		return AbstractSFAddon.getLang().get(lang, replacements);
	}
	
	/**
	 * Gets a MiniMessage component of a given lang lookup string with optional replacements.
	 * @param lang
	 * @param replacements
	 * @return
	 */
	public Component get(@Nonnull String lang, @Nullable Map<String, String> replacements) {
		Validate.notNull(lang, "Method Lang#get must contain a lang lookup key.");
		String value = null;

		if(LANG_CACHE.get(language).getString(lang) != null) {
			value = LANG_CACHE.get(language).getString(lang);
		} else if(LANG_CACHE.get(language).getStringList(lang) != null) {
			List<String> toPick = LANG_CACHE.get(language).getStringList(lang);
			value = toPick.get(RandomUtils.nextInt(toPick.size()-1));
		}
		
		// handle keys
		if(replacements != null) {
			for(Entry<String, String> replacement : replacements.entrySet()) {
				value = value.replace(replacement.getKey(), replacement.getValue());
			}
		}
		
		if(value == null) {
			if(prefixEnabled) {
				return prefix.append(FormatUtils.createMiniMessageComponent(DEFAULT_LANG.replace("%lang%", lang)));
			}
			return FormatUtils.createMiniMessageComponent(DEFAULT_LANG.replace("%lang%", lang));
		}
		
		if(prefixEnabled) {
			return prefix.append(FormatUtils.createMiniMessageComponent(value));
		}
		return FormatUtils.createMiniMessageComponent(value);
	}
	
	/**
	 * Reloads the lang file.
	 */
	public void reload() {
		AbstractSFAddon thisInstance = AbstractSFAddon.get();
		File langDirectory = new File(thisInstance.getDataFolder() + "/lang");
		if(!langDirectory.exists()) {
			langDirectory.mkdirs();
		}
		File langFile = new File(langDirectory + "/lang.yml");
		YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
		ConfigurationSection prefixSettings = langConfig.getConfigurationSection("prefix");
		prefixEnabled = prefixSettings.getBoolean("enabled");
		prefix = FormatUtils.createMiniMessageComponent(prefixSettings.getString("value"));
		
		for(Entry<String, Object> set : langConfig.getValues(false).entrySet()) {
			if(set.getValue() instanceof ConfigurationSection) {
				if(set.getKey().contains("prefix")) continue;
				LANG_CACHE.put(set.getKey(), (ConfigurationSection) set.getValue());
			}
		}
	}
	
	public Lang() {
		AbstractSFAddon thisInstance = AbstractSFAddon.get();
		File langDirectory = new File(thisInstance.getDataFolder() + "/lang");
		if(!langDirectory.exists()) {
			langDirectory.mkdirs();
		}
		File langFile = new File(langDirectory + "/lang.yml");
		if(!langFile.exists()) {
			try {
				langFile.createNewFile();
				InputStreamReader reader = new InputStreamReader(thisInstance.getResource("lang.yml"));
				YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(reader);
				langConfig.save(langFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
		ConfigurationSection prefixSettings = langConfig.getConfigurationSection("prefix");
		prefixEnabled = prefixSettings.getBoolean("enabled");
		prefix = FormatUtils.createMiniMessageComponent(prefixSettings.getString("value"));
		
		for(Entry<String, Object> set : langConfig.getValues(false).entrySet()) {
			if(set.getValue() instanceof ConfigurationSection) {
				if(set.getKey().contains("prefix")) continue;
				LANG_CACHE.put(set.getKey(), (ConfigurationSection) set.getValue());
			}
		}
	}
}
