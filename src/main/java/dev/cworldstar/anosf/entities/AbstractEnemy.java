package dev.cworldstar.anosf.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Consumer;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.entities.Skill.SkillType;
import dev.cworldstar.anosf.entities.tasks.BossBarTask;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;
import lombok.Getter;

public abstract class AbstractEnemy implements Listener, ConfigurationSerializable {
	
	public static List<AbstractEnemy> enemies = new ArrayList<AbstractEnemy>();
	
	public static <T extends AbstractEnemy> void removeEnemy(T enemy) {
		enemies.removeIf(enemy2 -> enemy2.getIdentifier().equals(enemy.getIdentifier()));
	}
	
	public static <T extends AbstractEnemy> void addEnemy(T enemy) {
		enemies.add(enemy);
	}
	
	static {
		ConfigurationSerialization.registerClass(AbstractEnemy.class);
	}
	
	private Consumer<LivingEntity> targetListener = null;
	@Getter
	private @NotNull LivingEntity entity;
	@Getter
	protected UUID identifier;
	@Getter
	private Map<String, Skill> skills = new HashMap<String, Skill>();
	protected LivingEntity target;
	
	public LivingEntity getTarget(int range) {
		if(
				(target.getLocation().distance(entity.getLocation()) > range)
		) {
			target = null;
		}
		return target;
	}
	
	public LivingEntity getTarget() {
		return getTarget(20);
	}
	
	@Getter
	protected Overhealth overhealth;
	@Getter
	private BossBarTask bossBarTask;
	
	public void addSkill(String id, Skill skill) {
		skill.setId(id);
		skill.lock();
		skills.put(id, skill);
	}
	
	public void addSkill(Skill skill) {
		Validate.notNull(skill.getId(), "SkillId must not be null!");
		skill.lock();
		skills.put(skill.getId(), skill);
	}
	
	public <T extends LivingEntity> AbstractEnemy(Location loc, Class<T> toSpawn, String name) {
		LivingEntity bossEntity = loc.getWorld().spawn(loc, toSpawn, SpawnReason.CUSTOM, false, null);
		entity = bossEntity;
		entity.setPersistent(true);
		entity.setRemoveWhenFarAway(false);
		entity.setMetadata("ABSTRACT_ENEMY", new FixedMetadataValue(ANOSF.get(), toSpawn.getCanonicalName()));
		identifier = entity.getUniqueId();
		Bukkit.getServer().getPluginManager().registerEvents(this, ANOSF.get());
		register();
		addEnemy(this);
	}
	
	public <T extends LivingEntity, I extends AbstractEnemy> AbstractEnemy(String uuid, Class<?> clazz, World world) {
		LivingEntity existingEntity = (LivingEntity) world.getEntity(UUID.fromString(uuid));
		if(existingEntity != null) {
			entity = existingEntity;
			identifier = entity.getUniqueId();
		}
	}

	@Override
	public @NotNull Map<String, Object> serialize() {
		return Map.of("identifier", identifier.toString(), "enemyClass", getClass().getCanonicalName(), "world", entity.getWorld());
	}
	
	private List<DamageCause> blacklistedDamageTypes = List.of(
			DamageCause.DROWNING,
			DamageCause.CRAMMING,
			DamageCause.FALL,
			DamageCause.THORNS
    );
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity() == null) return;
		double damage = e.getFinalDamage();
		if(e.getEntity().getUniqueId().equals(entity.getUniqueId())) {
			if(blacklistedDamageTypes.contains(e.getCause())) {
				e.setCancelled(true);
				return;
			}
			
			if(overhealth != null) {
				e.setDamage(0);
				overhealth.damage(entity, damage);		
			}
			
			if(getBossBarTask() != null) {
				getBossBarTask().forceUpdate();
			}
			onEntityDamagedModifier(e);
			
			if(e.getDamageSource().getCausingEntity() == null) {
				return;
			}
			
			target = (@Nullable LivingEntity) e.getDamageSource().getCausingEntity();
			if(targetListener != null) {
				targetListener.accept(target);
			}
		}  else if (e.getDamageSource() != null && e.getDamageSource().getCausingEntity() != null && e.getDamageSource().getCausingEntity().getUniqueId().equals(entity.getUniqueId())) {
			onEntityDamagingModifier(e);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDeath(EntityDeathEvent e) {
		if(e.getEntity().getUniqueId().equals(entity.getUniqueId())) {
			onDeath(e);
			removeEnemy(this);
		}
	}
	
	
	//-- internal do not override
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDropItem(EntityDropItemEvent e) {
		if(e.getEntity().getUniqueId().equals(entity.getUniqueId())) {
			dropItem(e);
		}
	}
	
	//-- internal do not override
	protected void onDeath(EntityDeathEvent e) {
		HandlerList.unregisterAll(this);
		death(e);
	}
	
	protected abstract void dropItem(EntityDropItemEvent e);
	
	protected abstract void death(EntityDeathEvent e);
	
	protected abstract void onEntityDamagedModifier(EntityDamageEvent e);
	
	protected abstract void onEntityDamagingModifier(EntityDamageEvent e);
	
	protected abstract void applyEntityEdits(LivingEntity e);
	
	protected void onRegister() {
		
	}
	
	public void useRandomSkill() {
		ArrayList<Skill> activeSkills = new ArrayList<Skill>();
		for(Entry<String, Skill> skill : this.skills.entrySet()) {
			if(skill.getValue().getType() == SkillType.ACTIVE) {
				activeSkills.add(skill.getValue());
			}
		}
		Skill chosenSkill = activeSkills.get(RandomUtils.nextInt(1, activeSkills.size())-1);
		chosenSkill.use();
	}
	
	protected abstract BossBarTask createBossBar();
	
	public void register() {
		applyEntityEdits(entity);
		onRegister();
		
		bossBarTask = createBossBar();
		if(bossBarTask != null) {
			bossBarTask.runTaskTimerAsynchronously(ANOSF.get(), 0, 20);
		}
		
		for(Entry<String, Skill> skill : skills.entrySet()) {
			if(skill.getValue().getType() == SkillType.PASSIVE) {
				skill.getValue().use();
			}
		}
		
		new BukkitRunnable() {
			@Override
			public void run() {
				AbstractEnemy self = AbstractEnemy.this;
				if(self.getEntity().isDead()) {
					this.cancel();
					return;
				}
				if(target == null) {
					return;
				}
				if(RandomUtils.nextInt(0, 5) >= 4) {
					self.useRandomSkill();
				}
			}
		}.runTaskTimer(ANOSF.get(), 0, 20);	
		return;
	}
}
