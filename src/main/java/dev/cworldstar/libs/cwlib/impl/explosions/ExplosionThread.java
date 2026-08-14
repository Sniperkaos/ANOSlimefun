package dev.cworldstar.libs.cwlib.impl.explosions;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

public abstract class ExplosionThread extends BukkitRunnable {

	private RayTraceResult ray;
	
	public ExplosionThread(RayTraceResult result) {
		this.ray = result;
	}
	
	@Override
	public void run() {
		
	}
	
}
