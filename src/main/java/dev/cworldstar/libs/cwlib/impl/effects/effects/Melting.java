package dev.cworldstar.libs.cwlib.impl.effects.effects;

import org.bukkit.Particle;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;

import dev.cworldstar.libs.cwlib.impl.effects.EffectThread;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.math.RandomUtils;
import net.kyori.adventure.util.TriState;

public class Melting extends EffectThread {
	public Melting(Player owner, int duration, String name) {
		super(owner, duration, name);
	}

	@Override
	public void tick() {
		owner.getWorld().spawnParticle(
				Particle.SMOKE, 
				owner.getLocation(), 
				10, 
				1.0, 
				2.0, 
				1.0
		);
		owner.setVisualFire(TriState.TRUE);
		owner.sendMessage(FormatUtils.mm("<gradient:gold:red:dark_red>You are melting.</gradient>"));
		owner.damage((RandomUtils.nextDouble() + 0.1) * 2, DamageSource.builder(DamageType.ON_FIRE).withCausingEntity(source).build());
	}

	@Override
	public void expire() {
		owner.setVisualFire(TriState.NOT_SET);
		owner.sendMessage(FormatUtils.mm("<green>You are no longer melting.</green>"));
	}

	@Override
	public void tryIncreaseSeverity() {
		
	}

}
