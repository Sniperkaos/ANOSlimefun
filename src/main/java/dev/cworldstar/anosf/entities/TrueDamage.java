package dev.cworldstar.anosf.entities;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import dev.cworldstar.anosf.entities.events.TrueDamageDealtEvent;

public class TrueDamage {
	
	public static enum TrueDamageType {
		PERCENTAGE, LITERAL
	}
	
	public static void dealTrueDamage(LivingEntity to, double amount, TrueDamageType type) {
		TrueDamageDealtEvent e = new TrueDamageDealtEvent(to, amount, type);
		Bukkit.getServer().getPluginManager().callEvent(e);
		if(!e.cancelled()) {
			double health = to.getHealth();
			switch(e.getType()) {
				case LITERAL:
					if(health - amount <= 0) {
						to.setHealth(0);
					} else {
						to.setHealth(health - amount);
					}
					break;
				case PERCENTAGE:
					double damage = to.getAttribute(Attribute.MAX_HEALTH).getValue() * (amount / 100);
					if(health - damage <= 0) {
						to.setHealth(0);
					} else {
						to.setHealth(health - damage);
					}
					break;
			}
			to.knockback(1, 0, -1);
		}
	}
}
