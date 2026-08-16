package dev.cworldstar.libs.cwlib.impl.breathing;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.bukkit.entity.LivingEntity;

import dev.cworldstar.libs.cwlib.impl.breathing.Lungs.LungAction;
import lombok.Getter;

public class BreathingAction extends CompletableFuture<Consumer<LivingEntity>> {
	
	private @Getter Lungs lungs;
	private @Getter LungAction action;
	private @Getter LivingEntity owner;
	
	public BreathingAction(Lungs lungs, LungAction action, LivingEntity owner) {
		this.lungs = lungs;
		this.action = action;
		this.owner = owner;
	}
}
