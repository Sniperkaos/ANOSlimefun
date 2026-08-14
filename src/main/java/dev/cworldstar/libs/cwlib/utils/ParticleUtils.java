package dev.cworldstar.libs.cwlib.utils;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class ParticleUtils {
	public static void spawnCircle(Location location, DustOptions color, double circleSize, int particleSize, int particleAmount) {
	    for (int d = 0; d <= particleAmount; d += 1) {
	        Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
	        particleLoc.setX(location.getX() + Math.cos(d) * circleSize + 0.5);
	        particleLoc.setZ(location.getZ() + Math.sin(d) * circleSize + 0.5);
	        location.getWorld().spawnParticle(Particle.DUST, particleLoc, particleSize, color);
	    }
	}
	
	public static void spawnCircle(Location location, Particle particle, double circleSize, int particleSize, int particleAmount, int distance) {
	    for (int d = 0; d <= particleAmount; d += 1) {
	        Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
	        particleLoc.setX(location.getX() + Math.cos(d) * circleSize);
	        particleLoc.setZ(location.getZ() + Math.sin(d) * circleSize);
	        location.getWorld().spawnParticle(particle, particleLoc, particleSize);
	    }
	}
	public static void makeWings(Player player, DustOptions color) {
		@NotNull Vector behind = player.getLocation().add(new Vector(0, 1, 0)).getDirection().normalize().multiply(-1.5);
		for(int span=0; span<5; span++) {
			for (double i=0; i<Math.PI*2; i+=(Math.PI/10)) {
				player.getWorld().spawnParticle(Particle.DUST, player.getLocation().add(behind.crossProduct(behind.clone().multiply(span * i))), 5, color);
			}
		}
	}
	public static void dripFromHead(LivingEntity drip, Particle particle) {
		Location loc = drip.getEyeLocation();
		for(int i = 0; i<=10; i++) {
			loc.getWorld().spawnParticle(particle, loc, 4,0.2,0.2,0.2);
		}
	}
	public static void displayParticlesRandomly(Location loc, Particle particle, double range, int quantity) {
		for(int i=0; i<quantity; i++) {
			double x = RandomUtils.nextDouble(0, range) - (range/2);
			double y = RandomUtils.nextDouble(0, range) - (range/2);
			double z = RandomUtils.nextDouble(0, range) - (range/2);
			loc.getWorld().spawnParticle(particle, x, y, z, i);
		}
	}
}
