package dev.cworldstar.anosf.impl.drugs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.impl.drugs.DrugThread.SeverityLevel;
import lombok.Getter;

public class DrugProfile {
	
	@Getter
	private static final Map<Entity, DrugProfile> profiles = new HashMap<Entity, DrugProfile>();
	
	public static void registerProfile(Player player, DrugProfile profile) {
		profiles.put(player, profile);
	}
	
	// this method deserializes drugthreads and pushes them into memory.
	public DrugProfile fromYamlConfiguration(Entity p, ConfigurationSection config) {
		Bukkit.getLogger().log(Level.INFO, "Recovering threads from config");
		for(Entry<String, Object> thread : config.getValues(false).entrySet()) {
			if(thread.getValue() instanceof ConfigurationSection) {
				ConfigurationSection section = (ConfigurationSection) thread.getValue();
				recoverThread(
						p,
						(int) section.get("duration"), 
						(int) section.get("maxduration"),
						SeverityLevel.valueOf((String) section.get("severity")),
						(int) section.get("usages"),
						(String) section.get("name")
						
				);
			} else {
				Bukkit.getLogger().log(Level.INFO, thread.getValue().toString());
			}
		}
		return null;
	}
	
	public static boolean playerHasProfile(@NotNull Player player) {
		return profiles.containsKey(player);
	}
	
	@Getter
	private boolean overdosed = false;
	@Getter
	private Map<String, DrugThread> threads = new HashMap<String, DrugThread>();
	@Getter
	private List<String> activeDrugs = new ArrayList<String>();
	
	public boolean isActive(String name) {
		return threads.containsKey(name);
	}
	
	private final int TICK_DURATION = 20;
	
	@SuppressWarnings("unchecked")
	public <T extends DrugThread> void recoverThread(
			Entity p, 
			int duration, 
			int maxDuration,
			SeverityLevel level,
			int usages,
			String className
	) {
		try {
			Class<T> threadClass = (Class<T>) Class.forName(className);
			triggerThread(p, duration, threadClass, maxDuration, level, usages).freeze();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public <T extends DrugThread> void triggerThread(Entity owner, int duration, Class<T> clazz) {
		triggerThread(owner, duration, clazz, null, SeverityLevel.VERY_LOW, null);
	}
	
	@Nullable
	public <T extends DrugThread> T triggerThread(Entity p, int duration, Class<T> threadClass, @Nullable Integer maxDuration, @Nullable SeverityLevel level, @Nullable Integer usages) {
		try {
			if(threads.get(threadClass.getCanonicalName()) != null) {
				threads.get(threadClass.getCanonicalName()).refresh();
			} else {
				if(maxDuration == null) {
					maxDuration = duration;
				}
				if(level == null) {
					level = SeverityLevel.VERY_LOW;
				}
				if(usages == null) {
					usages = 1;
				}
				T thread = threadClass.getDeclaredConstructor(LivingEntity.class, Integer.TYPE, String.class, Integer.TYPE, SeverityLevel.class, Integer.TYPE).newInstance(p, duration, threadClass.getCanonicalName(), maxDuration.intValue(), level, usages.intValue());
				if(maxDuration != null) {
					thread.updateMaxDuration(maxDuration.intValue());
				}
				thread.onThreadExpire((t) -> {
					threads.remove(threadClass.getCanonicalName());
				});
				thread.runTaskTimer(ANOSF.get(), 0, TICK_DURATION);
				threads.put(threadClass.getCanonicalName(), thread);
				return thread;
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DrugProfile(Player p, @Nullable ConfigurationSection savedData) {
		if(!profiles.containsKey(p)) {
			profiles.put(p, this);
			if(savedData != null) {
				fromYamlConfiguration(p, savedData);
			}
		}
	}
	
	public static YamlConfiguration toYamlConfiguration() {
		YamlConfiguration config = new YamlConfiguration();
		config.setComments("", List.of("This is a data file. Do not edit this unless you know what you are doing!"));
		for(Entry<Entity, DrugProfile> entries : profiles.entrySet()) {
			ConfigurationSection playerSection = config.createSection(entries.getKey().getUniqueId().toString());
			playerSection.set("player", entries.getKey());
			entries.getValue().populate(playerSection);
		}
		return config;
	}
	
	private <T extends DrugThread> void populate(ConfigurationSection section) {
		for(Entry<String, DrugThread> thread : this.getThreads().entrySet()) {
			section.set(String.valueOf(section.getValues(false).size()), thread.getValue().serialize());
		}
	}

	public static DrugProfile getProfile(@NotNull Entity query) {
		return profiles.get(query);
	}

	public static void freezeThreads(@NotNull Entity player) {
		Bukkit.getLogger().log(Level.INFO, "freezing threads for entity " + player.getName());
		DrugProfile profile = getProfile(player);
		Map<String, DrugThread> threads = profile.getThreads();
		for(Entry<String, DrugThread> threadEntry : threads.entrySet()) {
			threadEntry.getValue().freeze();
		}
	}

	public static void unfreezeThreads(@Nonnull Entity player) {
		Validate.isInstanceOf(LivingEntity.class, player, "The given entity must be an instance of LivingEntity!");
		Bukkit.getLogger().log(Level.INFO, "unfreezing threads for entity " + player.getName());
		DrugProfile profile = getProfile(player);
		Map<String, DrugThread> threads = profile.getThreads();
		for(Entry<String, DrugThread> threadEntry : threads.entrySet()) {
			threadEntry.getValue().unfreeze((LivingEntity) player);
		}
	}

	public static boolean profileExists(Entity query) {
		return profiles.containsKey(query);
	}

	public static void interruptThreads(@Nonnull Entity entity) {
		DrugProfile profile = getProfile(entity);
		Map<String, DrugThread> threads = profile.getThreads();
		for(Entry<String, DrugThread> threadEntry : threads.entrySet()) {
			threadEntry.getValue().interrupt();
		}
	}
}
