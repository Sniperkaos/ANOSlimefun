package dev.cworldstar.libs.cwlib.listeners;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;

public abstract class AbstractListener implements Listener {
	
	private boolean registered = false;	
	
	public void register() {
		this.registered = true;
	}

	public boolean registered() {
		return this.registered;
	}
	
	public <T extends AbstractSFAddon> AbstractListener(T addon) {
		Bukkit.getServer().getPluginManager().registerEvents(this, addon);
	}
	
	public static <T extends AbstractSFAddon> void register(T addon, Class<? extends AbstractListener> clazz) {
		try {
			AbstractListener listener = clazz.getDeclaredConstructor(AbstractSFAddon.class).newInstance(addon);
			listener.register();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
}
