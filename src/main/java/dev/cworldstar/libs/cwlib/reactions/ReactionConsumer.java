package dev.cworldstar.libs.cwlib.reactions;

@FunctionalInterface
public interface ReactionConsumer {
	float run(Float moles, Float enviroTemp, SlimefunReaction reaction);
}
