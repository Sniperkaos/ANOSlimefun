package dev.cworldstar.libs.cwlib.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.reactions.ReactionConsumer;
import dev.cworldstar.libs.cwlib.reactions.SlimefunReaction;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
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
	
	public static class ReactionEnvironment {
		private float temperature;
		private @Getter Location block;
		
		public ReactionEnvironment(Location block) {
			temperature = (float) block.getBlock().getTemperature();
			this.block = block;
		}
		
		public void editEnvironmentTemperature(float newTemp) {
			this.temperature = newTemp;
		}
		
		public float getEnvironmentTemperature() {
			return temperature;
		}
	}
	
	@AllArgsConstructor
	public static class ReactionResult {
		
		public static enum ReactionResultType {
			COMPLETED,
			FAILED;
			
			public static ReactionResultType asReactionResult(boolean b) {
				if(b) {
					return COMPLETED;
				} else {
					return FAILED;
				}
			}
		}
		
		@Getter
		private ReactionResultType completed;
		@Getter
		private ReactionEnvironment environment;
	}
	
	public CompletableFuture<ReactionResult> react(float moles, SlimefunReaction reaction, ReactionEnvironment environment) {
		Validate.isTrue(reaction.getOrigin().equals(this), "The given reaction does not include this reagent as an origin!");
		
		CompletableFuture<ReactionResult> snapshot = new CompletableFuture<ReactionResult>();
	
		reactionEquation.run(moles, environment, reaction).whenComplete((result, error) -> {
			boolean success = reaction.test(environment);
			snapshot.complete(new ReactionResult(ReactionResult.ReactionResultType.asReactionResult(success), environment));
		});
		
		return snapshot;
	}
	
	@Setter
	@Getter
	/**
	 *  Many of these consumers will run at the same time, we must not cache the environment temperature, don't yell at me
	 *  for not caching the {@link ReactionEnvironment#getEnvironmentTemperature()}.
	 *  
	 */
	private @NonNull ReactionConsumer reactionEquation = (moles, enviroment, reaction) -> {
		
		float heatDissipation = reaction.getHeatGeneration();
		
		CompletableFuture<Float> reactionResult = new CompletableFuture<Float>();
		
		new BukkitRunnable() {
			private double endTick = Bukkit.getCurrentTick() + reaction.getTicks();
			private	ReactionEnvironment reactionEnvironment = enviroment;
			
			@Override
			public void run() {
				if(endTick < Bukkit.getCurrentTick()) {
					reactionResult.complete(reactionEnvironment.getEnvironmentTemperature());
					cancel();
				}
							
				ReactionType rt = reaction.getReactionType();
				switch(rt) {
					case ENDOTHERMIC:
						// lowers the temp of the environment drastically
						reactionEnvironment.editEnvironmentTemperature(enviroment.getEnvironmentTemperature() - (heatDissipation*(1-(enviroment.getEnvironmentTemperature()/heatDissipation))));
					case EXOTHERMIC:
						// raises the temp of the environment drastically
						reactionEnvironment.editEnvironmentTemperature(enviroment.getEnvironmentTemperature() + (heatDissipation*(1+(enviroment.getEnvironmentTemperature()/heatDissipation))));
					default:
						
				}
			}
		}.runTaskTimer(AbstractSFAddon.get(), 0, Slimefun.getTickerTask().getTickRate());
		
		return reactionResult;
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
	
	public SlimefunLiquidStack(ItemGroup itemGroup, SlimefunItemStack item, ItemStack solid, SlimefunGas gas, float meltingTemperature) {
		this(itemGroup, item, solid);
		setMeltingTemperature(meltingTemperature);
	}
	
	@Override
	public void register(SlimefunAddon addon) {
		super.register(addon);
		ItemRegistry.registerItem(this);
	}
}
