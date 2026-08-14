package dev.cworldstar.libs.cwlib.utils;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class VectorUtils {
	public static Location getRelative(LivingEntity entity, Vector relative) {
		double yaw = entity.getLocation().getYaw();
		double pitch = entity.getLocation().getPitch();
		return entity.getLocation().add(new Vector(
				Math.sin(pitch + relative.getY()) * Math.cos(yaw + relative.getX()) * relative.getZ(),
				Math.cos(pitch + relative.getY()) * relative.getZ(),
				Math.sin(pitch + relative.getY()) * Math.sin(yaw + relative.getX()) * relative.getZ()
		));
    }
}
