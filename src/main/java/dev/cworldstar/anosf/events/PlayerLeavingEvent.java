package dev.cworldstar.anosf.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.impl.drugs.DrugProfile;

public class PlayerLeavingEvent implements Listener {
	public PlayerLeavingEvent() {
		Bukkit.getPluginManager().registerEvents(this, ANOSF.get());
	}
	
	@EventHandler
	public void onPlayerRemovingEvent(PlayerQuitEvent e) {
		DrugProfile.freezeThreads(e.getPlayer());
	}
}
