package dev.cworldstar.anosf.impl.drugs.drugs.threads;

import java.util.List;
import javax.annotation.Nullable;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.impl.drugs.DrugThread;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

public class SativaThread extends DrugThread {

	public SativaThread(
			LivingEntity entity, 
			int duration, 
			String name, 
			int maxDuration, 
			SeverityLevel level, 
			int usages
	) {
		super(entity, name, duration, maxDuration, level, usages);
	}
	
	public SativaThread(LivingEntity owner, int duration, String name, @Nullable Integer maxDuration) {
		super(owner, duration, name);
		if(maxDuration != null) {
			this.updateMaxDuration(maxDuration);
		}
	}
	
	public SativaThread(LivingEntity owner, int duration, String name) {
		super(owner, duration, name);
		if(this.getTickDuration() >= this.getMaxDuration()) {
			owner.sendMessage(ANOSF.getLang().get("drugs.sativa.usage", null));
			owner.playSound(Sound.sound(Key.key("minecraft:block.campfire.crackle"), Sound.Source.NEUTRAL, 1F, 1F));
		}
	}

	@Override
	public void tick() {
		owner.addPotionEffects(List.of(
				new PotionEffect(PotionEffectType.SPEED, 80, 0 + getSeverity().getLevel()),
				new PotionEffect(PotionEffectType.HASTE, 80, 0 + getSeverity().getLevel())
		));
		if(getSeverity().greaterThanOrEqual(SeverityLevel.HARMFUL)) {
			if(getTicks() % 60 == 0) {
				owner.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA,RandomUtils.nextInt(60, 100), -3+getSeverity().getLevel()));
				owner.sendMessage(ANOSF.getLang().get("drugs.sativa.overdose.message", null));
			}
		} else {
			if(getTicks() % 60 == 0) {
				owner.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,RandomUtils.nextInt(60, 100),0));
				owner.sendMessage(ANOSF.getLang().get("drugs.sativa.messages", null));
			}
		}
	}

	@Override
	public void expire() {
		owner.sendMessage(FormatUtils.mm("<gradient:green:red>You feel the effects expire.</gradient>"));
	}

	@Override
	public void tryIncreaseSeverity() {
		if(getUsages() % 12 == 0) {
			raiseSeverity();
		}
	}

}
