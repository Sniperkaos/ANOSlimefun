package dev.cworldstar.anosf.entities.enemies;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.entities.AbstractEnemy;
import dev.cworldstar.anosf.entities.Skill;
import dev.cworldstar.anosf.entities.Skill.SkillType;
import dev.cworldstar.anosf.entities.tasks.BossBarTask;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;

public class SpiderMinion extends AbstractEnemy {

	public SpiderMinion(Location loc) {
		super(loc, CaveSpider.class, "Spider Minion");
		
	}

	@Override
	protected void dropItem(EntityDropItemEvent e) {
		
	}

	@Override
	protected void death(EntityDeathEvent e) {

	}

	@Override
	protected void onEntityDamagedModifier(EntityDamageEvent e) {
		
	}

	@Override
	protected void onEntityDamagingModifier(EntityDamageEvent e) {
		
	}

	@Override
	protected void onRegister() {
		addSkill("ACID_SPIT", new Skill(SkillType.ACTIVE, () -> new BukkitRunnable() {
			@Override
			public void run() {
				Entity target = SpiderMinion.this.getEntity().getTargetEntity(45);
				if(target == null) {
					return;
				}
				Bukkit.getScheduler().runTask(ANOSF.get(), () -> {
					target.getLocation().getWorld().playSound(target.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 0.2F);
					for(int i=-4; i>=4; i++) {
						LlamaSpit spit = (LlamaSpit) target.getWorld().spawnEntity(SpiderMinion.this.getEntity().getLocation(), EntityType.LLAMA_SPIT);
						spit.setMetadata("SPIDER_MOTHER_SPIT", new FixedMetadataValue(ANOSF.get(), 0x01));
						spit.setVelocity(target.getLocation().subtract(getEntity().getLocation()).toVector().normalize().multiply(25));
					}
				});
			}	
		}));
	}

	@Override
	protected void applyEntityEdits(LivingEntity e) {
		e.customName(FormatUtils.mm("<dark_purple>Spider Minion"));
		e.getAttribute(Attribute.MAX_HEALTH).setBaseValue(150);
		e.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(10);
		AttributeInstance ms = e.getAttribute(Attribute.MOVEMENT_SPEED);
		ms.setBaseValue(ms.getBaseValue() * 1.1);
		
	}

	@Override
	protected BossBarTask createBossBar() {
		return null;
	}

}
