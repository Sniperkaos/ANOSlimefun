package dev.cworldstar.libs.cwlib.impl.explosions;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;

public class ExplosionConfiguration {
	
	private int strength = 0;
	private double explosionRadius = 0;
	private double falloutRadius = 0;
	private boolean radioactive = false;
	private Location center;
	private Player source;
	
	private ExplosionConfiguration() { 
		
	}
	
	public ExplosionConfiguration radioactive(@Nonnull boolean radioactive) {
		this.radioactive = radioactive;
		return this;
	}
	
	public ExplosionConfiguration strength(@Nonnull int strength) {
		this.strength = strength;
		return this;
	}
	
	public ExplosionConfiguration explosionRadius(@Nonnull double radius) {
		this.explosionRadius = radius;
		return this;
	}
	
	public double explosionRadius() {
		return this.explosionRadius;
	}
	
	public ExplosionConfiguration falloutRadius(@Nonnull double radius) {
		this.falloutRadius = radius;
		return this;
	}
	
	public double falloutRadius() {
		return this.falloutRadius;
	}
	
	public boolean radioactive() {
		return this.radioactive;
	}
	
	public Location center() {
		return this.center;
	}
	
	public int strength() {
		return this.strength;
	}
	
	public static ExplosionConfiguration create() {
		return new ExplosionConfiguration();
	}

	public ExplosionConfiguration center(@Nonnull Location center) {
		Validate.notNull(center, "Center must not be null.");
		this.center = center;
		return this;
	}

	public ExplosionConfiguration source(@Nonnull Player source) {
		Validate.notNull(source, "Player must not be null.");
		this.source = source;
		return this;
	}
	
	public Player source() {
		return this.source;
	}
	
}
