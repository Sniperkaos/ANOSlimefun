package dev.cworldstar.libs.cwlib.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.reactions.ReactionConsumer;
import dev.cworldstar.libs.cwlib.reactions.SlimefunReaction;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * TODO: gaseous item stacks
 */
public class SlimefunLiquidStack extends SlimefunItem {

	public static enum ReactionType {
		ENDOTHERMIC,
		EXOTHERMIC,
		NEUTRAL
	}
	
	@AllArgsConstructor
	public static class ReactionResult {
		@Getter
		private boolean completed;
		@Getter
		private float startTemperature;
		@Getter
		private float endTemperature;
	}
	
	public ReactionResult react(float moles, SlimefunReaction reaction, float environmentTemperature) {
		Validate.isTrue(reaction.getOrigin().equals(this), "The given reaction does not include this reagent as an origin!");
		float endTemperature = reactionEquation.run(moles, environmentTemperature, reaction);
		boolean failed = reaction.getFailingTemperature() > endTemperature;
		return new ReactionResult(failed, environmentTemperature, endTemperature);
	}
	
	@Setter
	private @NonNull ReactionConsumer reactionEquation = (moles, enviroTemp, reaction) -> {
		float heatDissipation = reaction.getHeatGeneration();
		ReactionType rt = reaction.getReactionType();
		switch(rt) {
			case ENDOTHERMIC:
				// lowers the temp of the environment drastically
				return enviroTemp - (heatDissipation*(1-(enviroTemp/heatDissipation)));
			case EXOTHERMIC:
				// raises the temp of the environment drastically
				return enviroTemp + (heatDissipation*(1+(enviroTemp/heatDissipation)));
			default:
				break;
		}
		return enviroTemp;
	};
	
	@Setter
	@Getter
	private @NonNull ReactionType liquidReactionType = ReactionType.ENDOTHERMIC;
	
	private List<SlimefunReaction> reactions = new ArrayList<>();
	
	public void addReaction(SlimefunReaction reaction) {
		Validate.isTrue(reaction.getOrigin().equals(this), "The reaction must have an origin of this liquid stack!");
		reactions.add(reaction);
	}
	
	@Setter
	@Getter
	/**
	 * This must not be null! When creating a {@link SlimefunLiquidStack}, remember
	 * to call {@link #setIngotStack(ItemStack)} before using {{@link #register(io.github.thebusybiscuit.slimefun4.api.SlimefunAddon)}!
	 */
	private @Nullable ItemStack ingotStack;
	
	@Getter
	@Setter
	private float meltingTemperature = 212.2F; // melting point of water
	
	public SlimefunLiquidStack(ItemGroup itemGroup, SlimefunItemStack item) {
		super(itemGroup, item, Items.FLUID_RECIPE_TYPE, new ItemStack[0]);
	}
	
	public SlimefunLiquidStack(ItemGroup itemGroup, SlimefunItemStack item, ItemStack solid) {
		this(itemGroup, item);
		setIngotStack(solid);
	}
	
	public SlimefunLiquidStack(ItemGroup itemGroup, SlimefunItemStack item, ItemStack solid, float meltingTemperature) {
		this(itemGroup, item, solid);
		setMeltingTemperature(meltingTemperature);
	}
	
	@Override
	public void register(SlimefunAddon addon) {
		super.register(addon);
		ItemRegistry.registerItem(this);
	}
}
