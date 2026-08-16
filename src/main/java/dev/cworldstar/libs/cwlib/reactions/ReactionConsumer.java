package dev.cworldstar.libs.cwlib.reactions;

import java.util.concurrent.CompletableFuture;

import dev.cworldstar.libs.cwlib.impl.SlimefunLiquidStack.ReactionEnvironment;

@FunctionalInterface
public interface ReactionConsumer {
		
	CompletableFuture<Float> run(Float moles, ReactionEnvironment environment, SlimefunReaction reaction);
}
