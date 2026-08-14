package dev.cworldstar.anosf.entities;

import org.bukkit.entity.LivingEntity;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;
import lombok.Getter;
import lombok.Setter;

public class Overhealth {
	@Getter
	@Setter
	private double overhealth = 0;
	@Getter
	private double maxOverhealth = 0;
	
	public Overhealth(double overhealth) {
		this.overhealth = overhealth;
		this.maxOverhealth = overhealth;
	}

	private void remove(double amount) {
		this.overhealth -= amount;
	}

	public void add(double amount) {
		this.overhealth = Math.min(this.maxOverhealth, this.overhealth+amount);
	}
	
	public void damage(LivingEntity damaging, double amount) {
		if(overhealth > 0) {
			if(overhealth < amount) {
				double damage = amount - overhealth;
				overhealth = 0;
				if((damaging.getHealth() - amount) <= 0) {
					damaging.setHealth(0);
				} else {
					damaging.setHealth(damaging.getHealth() - damage);
				}
			} else {
				remove(amount);
				return;
			}
		} else {
			if((damaging.getHealth() - amount) <= 0) {
				damaging.setHealth(0);
				return;
			}
			damaging.setHealth(damaging.getHealth() - amount);
		}
	}


}
