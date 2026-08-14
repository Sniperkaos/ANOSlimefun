package dev.cworldstar.anosf.entities.enemies;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.entities.AbstractEnemy;
import dev.cworldstar.anosf.entities.Overhealth;
import dev.cworldstar.anosf.entities.Skill;
import dev.cworldstar.anosf.entities.Skill.SkillType;
import dev.cworldstar.anosf.entities.TrueDamage;
import dev.cworldstar.anosf.entities.TrueDamage.TrueDamageType;
import dev.cworldstar.anosf.entities.tasks.BossBarTask;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;

public class DeathKnight extends AbstractEnemy implements Listener {

	private SkeletonHorse horse;
	
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if(e.getHitEntity() == null) return;
		Entity hitEntity = e.getHitEntity();
		Entity spitEntity = e.getEntity();
		if(!(spitEntity instanceof Arrow)) return;
		if(!(spitEntity.hasMetadata("ELITE_ARCHER_ARROW"))) return;
		if(!(hitEntity instanceof LivingEntity)) return;
		LivingEntity livingEntity = (LivingEntity) hitEntity;
		livingEntity.damage(54.0, DamageSource.builder(DamageType.MOB_PROJECTILE).withDirectEntity(this.getEntity()).withCausingEntity(this.getEntity()).build());
		livingEntity.sendMessage(FormatUtils.mm("<gray>You were hit by a withering arrow!"));
		livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 600, 3));
	}
	
	public DeathKnight(Location loc) {
		super(loc, Skeleton.class, "<gradient:gray:gold:yellow:gray>Death Knight");
	}

	@Override
	protected void dropItem(EntityDropItemEvent e) {
		
	}

	@Override
	protected void death(EntityDeathEvent e) {
		
		e.setDroppedExp(69421337);
		ProjectileHitEvent.getHandlerList().unregister(this);
		e.getDrops().add(ItemRegistry.getRegistryItem("FALLEN_SPARK_PARTICLE").getItem().clone());
		horse.remove();
	}

	@Override
	protected void onEntityDamagedModifier(EntityDamageEvent e) {
		
	}

	@Override
	protected void onEntityDamagingModifier(EntityDamageEvent e) {
		TrueDamage.dealTrueDamage(target, e.getFinalDamage(), TrueDamageType.LITERAL);
	}
	
	private Vector getDirection(Vector vec1, Vector vec2) {
		return vec2.subtract(vec1);
	}
	
	@Override
	protected void onRegister() {
		addSkill(new Skill("ARROW", SkillType.ACTIVE, () -> new BukkitRunnable() {
			@Override
			public void run() {
				Entity target = DeathKnight.this.getTarget();
				if(target != null && (target instanceof LivingEntity)) {
					Bukkit.getScheduler().runTask(ANOSF.get(), () -> {
						for(Entity e2 : DeathKnight.this.getEntity().getNearbyEntities(24, 24, 24)) {
							if(e2 instanceof Player) {
								((Player) e2).sendMessage(FormatUtils.mm("<gradient:gray:gold:yellow:gray>Elite Archer<gray>: Nothing escapes my eyes. I see you, <red>" + e2.getName() + "<gray>."));
							}
						}
						for(int i=-32; i<=32; i++) {
							Location startLocation = DeathKnight.this.getEntity().getEyeLocation().add(i, 2, i);
							Vector direction = getDirection(startLocation.toVector(), target.getLocation().toVector());
							Arrow arrow = target.getWorld().spawnArrow(startLocation,direction, 25, 0);
							arrow.setMetadata("ELITE_ARCHER_ARROW", new FixedMetadataValue(ANOSF.get(), 0x01));
						}
					});
				}
			}
		}));
		addSkill(new Skill("BACKSHOT", SkillType.ACTIVE, () -> new BukkitRunnable() {
			@Override
			public void run() {
				Entity target = DeathKnight.this.getTarget();
				if(target != null && (target instanceof LivingEntity)) {
					Bukkit.getScheduler().runTask(ANOSF.get(), () -> {
						Location startLocation = DeathKnight.this.getEntity().getEyeLocation().add(0, 2, 0);
						Vector direction = getDirection(startLocation.toVector(), target.getLocation().toVector());
						Arrow arrow = target.getWorld().spawnArrow(startLocation,direction, 100, 0);
						arrow.setMetadata("ELITE_ARCHER_ARROW", new FixedMetadataValue(ANOSF.get(), 0x01));
					});
				}
			}
		}));
	}

	@Override
	protected void applyEntityEdits(LivingEntity e) {
		overhealth = new Overhealth(12000);
		
		Skeleton skeleton = (Skeleton) e;
		EntityEquipment inventory = skeleton.getEquipment();
		inventory.setItemInMainHand(new ItemStackBuilder(Material.NETHERITE_SWORD).build());
		
		e.customName(FormatUtils.mm("<gradient:gray:gold:yellow:gray>Death Knight"));
		e.getAttribute(Attribute.KNOCKBACK_RESISTANCE).addModifier(new AttributeModifier(ANOSF.key("kb"), 1, Operation.ADD_NUMBER));
		e.getAttribute(Attribute.ATTACK_DAMAGE).addModifier(new AttributeModifier(ANOSF.key("atk"), 75, Operation.ADD_NUMBER));

		e.getAttribute(Attribute.SCALE).addModifier(new AttributeModifier(ANOSF.key("scale"), 2, Operation.ADD_NUMBER));
		e.getAttribute(Attribute.MAX_HEALTH).addModifier(new AttributeModifier(ANOSF.key("health"), 1000, Operation.ADD_NUMBER));
		e.setHealth(e.getAttribute(Attribute.MAX_HEALTH).getValue());
		
		SkeletonHorse horse = (SkeletonHorse) e.getWorld().spawnEntity(e.getLocation(), EntityType.SKELETON_HORSE);
		horse.getAttribute(Attribute.MAX_HEALTH).addModifier(new AttributeModifier(ANOSF.key("horseHP"), 1500, Operation.ADD_NUMBER));
		horse.getAttribute(Attribute.SCALE).addModifier(new AttributeModifier(ANOSF.key("scale"), 2, Operation.ADD_NUMBER));
		horse.setInvulnerable(true);
		this.horse = horse;
		
		horse.addPassenger(e);
	}

	@Override
	protected BossBarTask createBossBar() {
		return new BossBarTask(BarColor.PURPLE, "Death Knight", BarStyle.SOLID, getEntity(), overhealth);
	}

}
