package dev.cworldstar.anosf;

import org.bukkit.Bukkit;
import lombok.Getter;


public class PluginIntegrations {
	
	public static enum SupportedSlimefunAddon {
		INFINITY_EXPANSION_2("InfinityExpansion2"),
		SUPREME("Supreme");
		
		@Getter
		private String pluginID;
		
		SupportedSlimefunAddon(String s) {
			pluginID = s;
		}
	}
	
	public static boolean isProtocolLibInstalled() {
		return Bukkit.getPluginManager().isPluginEnabled("ProtocolLib");
	}
	
	public static boolean isSlimefunAddonInstalled(SupportedSlimefunAddon addon) {
		return Bukkit.getPluginManager().isPluginEnabled(addon.getPluginID());
	}
}
