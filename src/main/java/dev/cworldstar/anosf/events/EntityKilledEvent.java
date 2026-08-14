package dev.cworldstar.anosf.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.impl.drugs.DrugProfile;

/**
 * Fires when an entity is killed. Interrupts all threads on entity death so you don't
 * persist threads over lives.
 * @author cworldstar
 *
 */
public class EntityKilledEvent implements Listener {
	public EntityKilledEvent() {
		Bukkit.getPluginManager().registerEvents(this, ANOSF.get());
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if(DrugProfile.profileExists(e.getEntity())) {
			DrugProfile.interruptThreads(e.getEntity());
		}
	}
}
