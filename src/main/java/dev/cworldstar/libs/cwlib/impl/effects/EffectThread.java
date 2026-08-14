package dev.cworldstar.libs.cwlib.impl.effects;

import javax.annotation.Nonnull;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import dev.cworldstar.anosf.impl.drugs.DrugProfile;
import dev.cworldstar.anosf.impl.drugs.DrugThread;
import lombok.Getter;

public abstract class EffectThread extends DrugThread {
	
	@Getter
	protected LivingEntity source;
	@Getter
	protected LivingEntity owner;
	
	protected boolean isActive(Entity query, @Nonnull String effectCanonicalName) {
		if(!DrugProfile.profileExists(query)) {
			return false;
		}
		DrugProfile profile = DrugProfile.getProfile(query);
		return profile.isActive(effectCanonicalName);
	}
	
	public EffectThread(Player owner, int duration, String name) {
		super(owner, duration, name);
	}
}
