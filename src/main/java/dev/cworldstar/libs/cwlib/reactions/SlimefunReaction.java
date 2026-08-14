package dev.cworldstar.libs.cwlib.reactions;

import org.jetbrains.annotations.NotNull;

import dev.cworldstar.libs.cwlib.impl.SlimefunLiquidStack;
import dev.cworldstar.libs.cwlib.impl.SlimefunLiquidStack.ReactionType;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SlimefunReaction {
	@Getter
	private @NotNull SlimefunLiquidStack origin;
	@Getter
	private @NotNull SlimefunItemStack reactant;
	@Getter
	private @NotNull ReactionType reactionType;
	@Getter
	private @NotNull float heatGeneration;
	@Getter
	private @NotNull float failingTemperature;
}
