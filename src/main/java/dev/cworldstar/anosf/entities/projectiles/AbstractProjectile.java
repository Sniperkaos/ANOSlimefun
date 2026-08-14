package dev.cworldstar.anosf.entities.projectiles;

import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.entities.AbstractEnemy;
import dev.cworldstar.anosf.entities.tasks.BossBarTask;

public abstract class AbstractProjectile extends AbstractEnemy {

	public <T extends LivingEntity> AbstractProjectile(Location loc, Class<T> toSpawn, String name) {
		super(loc, toSpawn, name);
		
		if(!toSpawn.isInstance(Projectile.class)) {
			ANOSF.log(Level.WARNING, "Error: AbstractProjectile initiated with a non-projectile living entity!");
			return;
		}
	}

	public abstract void entityHitHandler(LivingEntity hit, ProjectileHitEvent event);
	
	protected void onEntityHit(ProjectileHitEvent e) {
		Projectile projectile = e.getEntity();
		if(!projectile.hasMetadata("ANOSF_PROJECTILE")) return;
		Entity hitEntity = e.getHitEntity();
		if(hitEntity == null || !(hitEntity instanceof LivingEntity livingEntity)) return;
		entityHitHandler(livingEntity, e);
	}
	
	@Override
	protected void dropItem(EntityDropItemEvent e) {
		e.setCancelled(true);
	}

	@Override
	protected void death(EntityDeathEvent e) {
		
	}

	@Override
	protected void onEntityDamagedModifier(EntityDamageEvent e) {
		e.setCancelled(true);
	}

	@Override
	protected void onEntityDamagingModifier(EntityDamageEvent e) {
		
	}

	@Override
	protected BossBarTask createBossBar() {
		return null;
	}

}
