package dev.cworldstar.anosf.impl.drugs.drugs.threads;

import java.util.List;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev.cworldstar.anosf.impl.drugs.DrugThread;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;

public class TestDrugThread extends DrugThread {
	public TestDrugThread(LivingEntity owner, int duration, String name, @Nullable Integer maxDuration) {
		super(owner, duration, name);
		if(maxDuration != null) {
			this.updateMaxDuration(maxDuration);
		}
	}
	
	public TestDrugThread(			
			LivingEntity entity, 
			int duration, 
			String name, 
			int maxDuration, 
			SeverityLevel level, 
			int usages
	) {
		super(entity, name, duration, maxDuration, level, usages);
	}
	
	public TestDrugThread(LivingEntity owner, int duration, String name) {
		super(owner, duration, name);
	}

	@Override
	public void tick() {
		// potion effects
		((LivingEntity) owner).addPotionEffects(List.of(
			new PotionEffect(PotionEffectType.STRENGTH, 60, 3),	
			new PotionEffect(PotionEffectType.MINING_FATIGUE, 60, 1),
			new PotionEffect(PotionEffectType.NAUSEA, 60, 0)	
		));
		
		World world = owner.getWorld();
		Location pos = owner.getLocation();
		world.spawnParticle(Particle.CHERRY_LEAVES,pos, 8, 0, 0, 0);
		owner.sendMessage(
				FormatUtils.createMiniMessageComponent("<gradient:red:gold>This is a test. Remaining duration: " + String.valueOf(this.getTickDuration()) + " </gradient>")
		);
	}

	@Override
	public void expire() {
		owner.removePotionEffect(PotionEffectType.STRENGTH);
		owner.removePotionEffect(PotionEffectType.MINING_FATIGUE);
		owner.removePotionEffect(PotionEffectType.NAUSEA);
		owner.sendMessage(
				FormatUtils.createMiniMessageComponent("<gradient:gold:green>The test is complete.</gradient>")
		);
	}
	
	@Override
	public void tryIncreaseSeverity() {
		raiseSeverity();
	}
}
