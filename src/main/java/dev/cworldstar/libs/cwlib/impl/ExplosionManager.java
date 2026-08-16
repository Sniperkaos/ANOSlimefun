package dev.cworldstar.libs.cwlib.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Chunk;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.events.SFTickEvent;
import dev.cworldstar.libs.cwlib.impl.RadiationZone.RadiationZoneLevel;
import dev.cworldstar.libs.cwlib.impl.explosions.ExplosionConfiguration;
import dev.cworldstar.libs.cwlib.utils.BlockHelper;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.math.RandomUtils;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask.ExecutionState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * 
 * Handles explosions from the {@link Explosive} class.
 * Inefficient, however very cool. Will ignore blocks behind a block with a greater
 * blast resistance than its {@link Explosive#getStrength()) value.
 * 
 * @author cworldstar
 *
 */
public class ExplosionManager {
	
	
	
	private static final List<ExplosionOperation> QUEUED_OPERATIONS = new ArrayList<>();
	
	public static void onSFTick(SFTickEvent e) {
		long startingTick = e.tick();
		for(ExplosionOperation o : QUEUED_OPERATIONS) {
			if(ANOSF.get().getLastSlimefunTick() > (startingTick + 5)) {
				break;
			}
			o.execute();
		}
	}
	
	@AllArgsConstructor
	public static class ExplosionOperation {
		private @Getter ExplosionBlock block;
		private @Getter Material newMaterial;
		
		public void execute() {
			Location reconstructedLocation = new Location(block.getWorld(), block.getLocation().getX(), block.getLocation().getY(), block.getLocation().getZ());
			
		}
	}
	
	@AllArgsConstructor
	public static class ExplosionBlock {
	        private final @Getter Vector location;
	        private final @Getter World world;
	        private final @Getter Material type;
	        private final @Getter float blastResistance;
			public Chunk getChunk() {
				return toLocation().getChunk();
			}
			public Block toBlock() {
				return toLocation().getBlock();
			}
			public Location toLocation() {
				return new Location(world, location.getX(), location.getY(), location.getZ());
			}
			public static ExplosionBlock fromBlock(Block block) {
				return new ExplosionBlock(block.getLocation().toVector(), block.getWorld(), block.getType(), block.getType().getBlastResistance());
			}
	}
	
	private List<ExplosionBlock> captureBlocks(Location center, double radius) {
		ArrayList<ExplosionBlock> blockList = new ArrayList<>();
		
	    for (Block block : BlockHelper.getBlocksInSphere(center, radius / 2)) {
	        Material material = block.getType();

	        if (material.isAir() || material == Material.WATER) {
	            continue;
	        }
	        
	        blockList.add(new ExplosionBlock(
	                block.getLocation().toVector(),
	                block.getWorld(),
	                material,
	                material.getBlastResistance()
	        ));
	    }
		
		return blockList;
	}
	
	private final class Incremental {
		private int i = 0;
		
		public void increment() {
			i++;
		}
		
		public int count() {
			return i;
		}
	}
	
	private void setBlockSync(Block b, Material block) {
		ANOSF.sync(() -> {
			b.setType(block);
		});
	}
	
	private void handleBlock(@Nullable RayTraceResult ray, @NotNull Block b, @NotNull ExplosionConfiguration config) {
		
		if(b.getType() == Material.AIR) {
			return;
		}
		
		if(ray == null) {
			return;
		}
		
		int strength = config.strength();
		
		Block hitBlock = ray.getHitBlock();
		double currentStrength = strength; // getStrength(strength, ray.getHitBlock().getLocation().toVector().distance(center.toVector()));
		
		if (currentStrength < 0) {
			return;
		}
		
		if(hitBlock.getType().getBlastResistance() > currentStrength) {
			return;
		}
		
		if(Slimefun.getProtectionManager().hasPermission(config.source(), b, Interaction.BREAK_BLOCK)) {
			if(BlockStorage.hasBlockInfo(b) || b.getType() == Material.CHEST) {
				return;
			} else {
				setBlockSync(b, Material.AIR); //-- so we don't lag the server dropping 8 trillion cobblestone
				return;
			}
		}
	}
	
	private void handleExplosion(List<ExplosionBlock> blocks, ExplosionConfiguration config) {
		
		Location center = config.center();
		double radius = config.explosionRadius();
		int strength = config.strength();
		Vector spawnLoc = blocks.get(0).getLocation();
		
		Slimefun.runSync(() -> {
			center.getWorld().spawnParticle(Particle.WHITE_SMOKE, new Location(center.getWorld(), spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ()), 16);
		});
		
		for(ExplosionBlock b : blocks) {
			
			// we ignore water because fallout will get it later.
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.WATER)) {
				if(b.getType().equals(Material.WATER)) {
					schedule(new ExplosionOperation(b, Material.AIR));
				}
				continue;
			}
			
			if(b.getType().getBlastResistance() > strength) {
				continue;
			}
			
			Chunk chunk = b.getChunk();
			if(!chunk.isLoaded()) {
				chunk.load();
			}
			
			Vector direction = BlockHelper.direction(center.getBlock(), b.toBlock(), radius);
			
			/*if(Double.isNaN(direction.getX())) {
				direction = new Vector(0, direction.getY(), direction.getZ());
			}
			
			if(Double.isNaN(direction.getY())) {
				direction = new Vector(direction.getX(), 0, direction.getZ());
			}
			
			if(Double.isNaN(direction.getZ())) {
				direction = new Vector(direction.getX(), direction.getY(), 0);
			}*/
			
			if(direction.isZero()) {
				BlockStorage.clearBlockInfo(b.toBlock());
				setBlockSync(b.toBlock(), Material.AIR);
				continue;
			}
			
			direction = direction.normalize();
			
			RayTraceResult ray = center.getWorld().rayTraceBlocks(center, direction, radius / 2, FluidCollisionMode.NEVER);
			/*
			 * 		
			 */
			if(ray != null && ray.getHitBlock() != null) {
				if(!ray.getHitPosition().equals(b.getLocation())) {
					while(true) {
						ray = center.getWorld().rayTraceBlocks(center, direction, radius / 2, FluidCollisionMode.NEVER);
						if(ray == null || ray.getHitBlock().getType().getBlastResistance() > strength || ray.getHitBlock().equals(b.toBlock()) || b.getLocation().distance(ray.getHitPosition()) <= 0.5) {
							handleBlock(ray, b.toBlock(), config);
							break;
						}
						handleBlock(ray, ray.getHitBlock(), config);
					}
				}
				
			} else {
				setBlockSync(b.toBlock(), Material.AIR);
				continue;
			}
		}
	}
	
	private void schedule(ExplosionOperation explosionOperation) {
		
	}

	protected static int THREAD_LIMIT = 35;
	protected static int BLOCK_LIMIT = 500;
	
	public void explode(Player source, ExplosionConfiguration config) {
		AbstractSFAddon.log(Level.WARNING, "Warning! Explosion started. Unsafe methods have been used. If you see an error, report it.");
		
		Validate.notNull(config.center(), "The given ExplosionConfiguration must have a #location(Location)!");
		
		Location center = config.center();
		double radius = config.explosionRadius();

		BlockStorage.clearBlockInfo(config.center().getBlock());
		setBlockSync(config.center().getBlock(), Material.AIR);
		
		config.source(source);
		ArrayList<Block> blocks = BlockHelper.getBlocksInSphere(center, radius/2);
		
		int place = 0;
		boolean breakl = false;
		List<ScheduledTask> explosionTasks = new ArrayList<ScheduledTask>();
		
		while(true) {
			List<Block> subList;
			if(blocks.size() > (place + BLOCK_LIMIT)) {
				subList = blocks.subList(place, place + BLOCK_LIMIT);
			} else {
				subList = blocks.subList(place, Math.clamp(0, blocks.size()-1, (place + BLOCK_LIMIT - (place - blocks.size()))));
				breakl = true;
			}
			
			place += BLOCK_LIMIT;
			
			explosionTasks.add(AbstractSFAddon.async((ScheduledTask task) -> {
				handleExplosion(subList.stream().map(block -> ExplosionBlock.fromBlock(block)).toList(), config);
				task.cancel();
			}));
			
			if(breakl) {
				break;
			}
		}
		
		World world = center.getWorld();
		world.playSound(center, Sound.ENTITY_WARDEN_SONIC_BOOM, 5, (float) 0.2);
		world.playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 5, (float) 0.4);
		
		
		AbstractSFAddon.async((ScheduledTask task) -> {
			while(true) {
				if(!explosionTasks.get(explosionTasks.size()-1).getExecutionState().equals(ExecutionState.FINISHED) || explosionTasks.get(explosionTasks.size()-1).getExecutionState().equals(ExecutionState.CANCELLED)) {
					if(config.radioactive()) {
						handleRadiation(config);
					}
					task.cancel();
					break;
				}
			}
		});
	}
	
	private void handleRadiation(ExplosionConfiguration config) {
		
		Validate.notNull(config.falloutRadius(), "config#falloutRadius(int) must be set when config.radioactive() is true!");
		
		Location center = config.center();
		double falloutRadius = config.falloutRadius();
		BoundingBox fallout = new BoundingBox(center.getX() - falloutRadius, center.getY() - falloutRadius, center.getZ() - falloutRadius, center.getX() + falloutRadius, center.getY() + falloutRadius, center.getZ() + falloutRadius);
		
		RadiationZone zone = RadiationZone.of(center.getWorld(), fallout, RadiationZoneLevel.CHERNOBYL);
		RadiationExtender.markIrradiatedLocation(zone);
		RadiationExtender.spawnRadiationZoneCloud(zone, config.center().toVector());
		
		Incremental counter = new Incremental();
		ArrayList<Block> blocks = BlockHelper.getBlocksInBoundingBox(center.getWorld(), fallout);
		
		for(Block b : blocks) {
			if(BlockStorage.hasBlockInfo(b)) continue;
			counter.increment();
			if(counter.count() % 100 == 0) {
				AbstractSFAddon.async((ScheduledTask task) -> {
					for(Block block : blocks.subList(Math.max(0, counter.count() - 100), Math.min(counter.count(), blocks.size()))) {
						switch(block.getType()) {
							case GRASS_BLOCK:
								boolean pathOrCoarse = RandomUtils.nextBoolean();
								if(pathOrCoarse) {
									setBlockSync(block, Material.DIRT_PATH);
								} else {
									setBlockSync(block,Material.COARSE_DIRT);
								}
								break;
							case WATER:
								setBlockSync(block,Material.AIR);
								break;
							case SAND:
								setBlockSync(block,Material.GLASS);
								break;
							case SANDSTONE:
								setBlockSync(block, Material.BLACKSTONE);
							case DEEPSLATE:
								setBlockSync(block,Material.COBBLED_DEEPSLATE);
								break;
							case DIRT:
								boolean coarseOrRooted = RandomUtils.nextBoolean();
								if(coarseOrRooted) {
									setBlockSync(block,Material.COARSE_DIRT);
								} else {
									setBlockSync(block,Material.ROOTED_DIRT);
								}
								break;
							case STONE:
								boolean stoneOrCobblestone = RandomUtils.nextBoolean();
								if(stoneOrCobblestone) {
									setBlockSync(block,Material.COBBLESTONE);
								} else {
									setBlockSync(block,Material.BLACKSTONE);
								}
								break;
							case ACACIA_LEAVES:
							case BIRCH_LEAVES:
							case CHERRY_LEAVES:
							case DARK_OAK_LEAVES:
							case JUNGLE_LEAVES:
							case SPRUCE_LEAVES:
							case MANGROVE_LEAVES:
							case OAK_LEAVES:
								setBlockSync(block,Material.AIR);
								break;
							default:
								break;
						}	
					}
				});
			}
		}
	}

	private ExplosionManager() {
		
	}
	
	public static ExplosionConfiguration createExplosionConfig() {
		return ExplosionConfiguration.create();
	}

	public static ExplosionManager start() {
		return new ExplosionManager();
	}

	public static String radius(int radius) {
		return FormatUtils.formatString("&e&l⚠ &r&7Explosion Radius: &c" + String.valueOf(radius));
	}
	
	public static String power(int power) {
		return FormatUtils.formatString("&r&7Explosion Strength: &c" + String.valueOf(power));
	}
	
	public static String radioactive(RadiationZoneLevel radiationLevel) {
		return FormatUtils.formatString("&e&l☢️ &r&4Radiation Level: &c" + String.valueOf(radiationLevel.toString()));
	}

	public static String falloutRadius(int i) {
		return FormatUtils.formatString("&e&l☢️ &r&4Fallout Radius: &c" + String.valueOf(i));
	}
}
