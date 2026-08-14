package dev.cworldstar.libs.cwlib.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
import dev.cworldstar.anosf.impl.radiation.ExtendedRadiationInfo.RadiationType;
import dev.cworldstar.libs.cwlib.AbstractSFAddon;
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
	
	private void handleExplosion(List<Block> blocks, ExplosionConfiguration config) {
		
		Location center = config.center();
		double radius = config.explosionRadius();
		int strength = config.strength();
		World world = center.getWorld();
		
		Location spawnLoc = blocks.get(0).getLocation();
		
		Slimefun.runSync(() -> {
			world.spawnParticle(Particle.WHITE_SMOKE, spawnLoc, 16);
		});
		
		for(Block b : blocks) {
			
			// we ignore water because fallout will get it later.
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.WATER)) {
				if(b.getType().equals(Material.WATER)) {
					setBlockSync(b, Material.AIR);
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
			
			Vector direction = BlockHelper.direction(center.getBlock(), b, radius);
			
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
				BlockStorage.clearBlockInfo(b);
				setBlockSync(b, Material.AIR);
				continue;
			}
			
			direction = direction.normalize();
			
			RayTraceResult ray = world.rayTraceBlocks(center, direction, radius / 2, FluidCollisionMode.NEVER);
			/*
			 * 		
			 */
			if(ray != null && ray.getHitBlock() != null) {
				if(!ray.getHitPosition().equals(b.getLocation().toVector())) {
					while(true) {
						ray = world.rayTraceBlocks(center, direction, radius / 2, FluidCollisionMode.NEVER);
						if(ray == null || ray.getHitBlock().getType().getBlastResistance() > strength || ray.getHitBlock().equals(b) || b.getLocation().toVector().distance(ray.getHitPosition()) <= 0.5) {
							handleBlock(ray, b, config);
							break;
						}
						handleBlock(ray, ray.getHitBlock(), config);
					}
				}
				
			} else {
				setBlockSync(b, Material.AIR);
				continue;
			}
		}
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
				handleExplosion(subList, config);
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
