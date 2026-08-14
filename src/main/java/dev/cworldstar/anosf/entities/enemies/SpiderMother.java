package dev.cworldstar.anosf.entities.enemies;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
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
import dev.cworldstar.libs.cwlib.utils.BlockHelper;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import dev.cworldstar.libs.cwlib.utils.ParticleUtils;

public class SpiderMother extends AbstractEnemy implements Listener {
	
	public SpiderMother(Location loc) {
		super(loc, Spider.class, "<gradient:dark_purple:gray>Spider Mother");
		Bukkit.getPluginManager().registerEvents(this, ANOSF.get());
	}
	
	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent e) {
		if(e.getHitEntity() == null) return;
		if(e.getHitEntity().hasMetadata("ABSTRACT_ENEMY")) {
			e.setCancelled(true);
			return;
		}
		Entity hitEntity = e.getHitEntity();
		Entity spitEntity = e.getEntity();
		if(!(spitEntity instanceof LlamaSpit)) return;
		if(!(spitEntity.hasMetadata("SPIDER_MOTHER_SPIT"))) return;
		if(!(hitEntity instanceof LivingEntity)) return;
		LivingEntity livingEntity = (LivingEntity) hitEntity;
		livingEntity.damage(28.0, DamageSource.builder(DamageType.MAGIC).withDirectEntity(this.getEntity()).withCausingEntity(this.getEntity()).build());
		livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 600, 5));
		World world = livingEntity.getWorld();
		Location location = livingEntity.getLocation();
		world.getBlockAt(location).setType(Material.COBWEB);
	}

	@Override
	protected void dropItem(EntityDropItemEvent e) {
		
	}

	@Override
	protected void death(EntityDeathEvent e) {
		e.setDroppedExp(1000);
		ProjectileHitEvent.getHandlerList().unregister(this);
		e.getDrops().add(ItemRegistry.getRegistryItem("FALLEN_SPARK_PARTICLE").getItem().clone());
	}

	@Override
	protected void onEntityDamagedModifier(EntityDamageEvent e) {

		
	}

	@Override
	protected void onEntityDamagingModifier(EntityDamageEvent e) {
		TrueDamage.dealTrueDamage((LivingEntity) e.getEntity(), 5, TrueDamageType.PERCENTAGE);
	}

	@Override
	protected void onRegister() {
		addSkill("LEAP", new Skill("LEAP", SkillType.ACTIVE, () -> 
			new BukkitRunnable() {
				@Override
				public void run() {
					Entity target = SpiderMother.this.getTarget();
					if(target != null) {
						Bukkit.getScheduler().runTask(ANOSF.get(), () -> {
							for(Entity e2 : SpiderMother.this.getEntity().getNearbyEntities(24, 24, 24)) {
								if(e2 instanceof Player) {
									((Player) e2).sendMessage(FormatUtils.mm("<gradient:dark_purple:gray>Spider Mother<gray>: <purple>Die, die, die!"));
								}
							}
							ParticleUtils.displayParticlesRandomly(getEntity().getLocation(), Particle.CAMPFIRE_COSY_SMOKE, 2D, 1);
							SpiderMother.this.getEntity().teleport(target);
							for(int i=0; i<= 8; i++) {
								if(target instanceof LivingEntity) {
									target.getLocation().getWorld().strikeLightningEffect(target.getLocation());
									target.getLocation().getWorld().playSound(target.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 0.1F);
									LivingEntity letarget = (LivingEntity) target;
									// each lightning effect will do 15% max HP true irreducable damage
									TrueDamage.dealTrueDamage(letarget, 2, TrueDamageType.PERCENTAGE);
								}
							}
						});
					}
				}
			})
		);
		addSkill("WEB", new Skill("WEB", SkillType.ACTIVE, () -> new BukkitRunnable() {
			@Override
			public void run() {
				Entity target = SpiderMother.this.getTarget();
				if(target != null && (target instanceof LivingEntity)) {
					Bukkit.getScheduler().runTask(ANOSF.get(), () -> {
						for(Entity e2 : SpiderMother.this.getEntity().getNearbyEntities(24, 24, 24)) {
							if(e2 instanceof Player) {
								((Player) e2).sendMessage(FormatUtils.mm("<gradient:dark_purple:gray>Spider Mother<gray>: <purple>You won't go anywhere!"));
							}
						}
						ParticleUtils.displayParticlesRandomly(getEntity().getLocation(), Particle.ITEM_COBWEB, 3D, 15);
						Location relative = target.getLocation();
						World world = relative.getWorld();
						target.getLocation().getWorld().playSound(target.getLocation(), Sound.BLOCK_SLIME_BLOCK_BREAK, 1, 0.2F);
						for(int y=0; y<=1; y++) {
							Block block = world.getBlockAt(relative.clone().add(new Vector(0,y,0)));
							if(block.getType() == Material.AIR) {
								block.setType(Material.COBWEB);
							}
						}
						((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 0));
					});
				}
			}
		}));
		addSkill("SUMMON", new Skill("SUMMON", SkillType.ACTIVE, () -> new BukkitRunnable() {
			@Override
			public void run() {
				for(int i=0; i<=RandomUtils.nextInt(1, 3); i++) {
					Bukkit.getScheduler().runTask(ANOSF.get(), () -> {
						for(Entity e2 : SpiderMother.this.getEntity().getNearbyEntities(24, 24, 24)) {
							if(e2 instanceof Player) {
								((Player) e2).sendMessage(FormatUtils.mm("<gradient:dark_purple:gray>Spider Mother<gray>: <purple>Go, my child!"));
							}
						}
						SpiderMinion minion = new SpiderMinion(SpiderMother.this.getEntity().getLocation().add(BlockHelper.randomVector(3,0,3)));
						CaveSpider spider = (CaveSpider) minion.getEntity();
						spider.setTarget(SpiderMother.this.target);
					});
				}
			}
		}));
		addSkill("ACID_SPIT", new Skill("ACID_SPIT", SkillType.ACTIVE, () -> new BukkitRunnable() {
			@Override
			public void run() {
				Entity target = SpiderMother.this.getTarget();
				Bukkit.getScheduler().runTask(ANOSF.get(), () -> {
					target.getLocation().getWorld().playSound(target.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 0.2F);
					for(int i=-4; i>=4; i++) {
						LlamaSpit spit = (LlamaSpit) target.getWorld().spawnEntity(SpiderMother.this.getEntity().getLocation(), EntityType.LLAMA_SPIT);
						spit.setMetadata("SPIDER_MOTHER_SPIT", new FixedMetadataValue(ANOSF.get(), 0x01));
						spit.setVelocity(target.getLocation().subtract(getEntity().getLocation()).toVector().normalize().multiply(25));
					}

				});
			}	
		}));
	}

	@Override
	protected void applyEntityEdits(LivingEntity e) {
		
		for(Entity e2 : e.getNearbyEntities(24, 24, 24)) {
			if(e2 instanceof Player) {
				((Player) e2).sendMessage(FormatUtils.mm("<gradient:dark_purple:gray>Spider Mother<gray>: <purple>You dare wake me from my slumber?"));
			}
		}
		
		overhealth = new Overhealth(10000);
		e.customName(FormatUtils.mm("<gradient:dark_purple:gray>Spider Mother"));
		e.getAttribute(Attribute.KNOCKBACK_RESISTANCE).addModifier(new AttributeModifier(ANOSF.key("kb"), 1, Operation.ADD_NUMBER));
		e.getAttribute(Attribute.SCALE).addModifier(new AttributeModifier(ANOSF.key("scale"), 5, Operation.ADD_NUMBER));
		e.getAttribute(Attribute.MAX_HEALTH).addModifier(new AttributeModifier(ANOSF.key("health"), 1000, Operation.ADD_NUMBER));
		e.getAttribute(Attribute.ATTACK_DAMAGE).addModifier(new AttributeModifier(ANOSF.key("DMG"), 25, Operation.ADD_NUMBER));
		e.getAttribute(Attribute.MOVEMENT_SPEED).addModifier(new AttributeModifier(ANOSF.key("movespeed"), 1, Operation.ADD_NUMBER));
		e.setHealth(e.getAttribute(Attribute.MAX_HEALTH).getValue());
	}

	@Override
	protected BossBarTask createBossBar() {
		return new BossBarTask(BarColor.PURPLE, "Spider Mother", BarStyle.SOLID, getEntity(), overhealth);
	}

}
