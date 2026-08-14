package dev.cworldstar.anosf.events;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.impl.drugs.DrugProfile;

public class PlayerAddedEvent implements Listener {
	public PlayerAddedEvent() {
		Bukkit.getPluginManager().registerEvents(this, ANOSF.get());
	}
	
	@EventHandler
	public void onPlayerAddedEvent(PlayerJoinEvent e) {
		if(!DrugProfile.playerHasProfile(e.getPlayer())) {
			// check if we have a serialized one
			ConfigurationSection section = ((ANOSF) ANOSF.get()).getDrugContainer().getConfigurationSection(e.getPlayer().getUniqueId().toString());
			if(section != null && (section != null && section.getValues(false).size() > 1)) {
				Bukkit.getLogger().log(Level.INFO, "Building profile from serialization.");
				new DrugProfile(e.getPlayer(), section);
				DrugProfile.unfreezeThreads(e.getPlayer());
				return;
			}
			Bukkit.getLogger().log(Level.INFO, "Building profile from scratch.");
			new DrugProfile(e.getPlayer(), 
				(
						(ANOSF) ANOSF.get()
				).getDProfile(
					e.getPlayer()
						.getUniqueId()
						.toString()
				)
			);
		} else {
			DrugProfile.unfreezeThreads(e.getPlayer());
		}
	}
}
