package dev.cworldstar.anosf.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkPopulateEvent;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.listeners.AbstractListener;

public class ChunkPopulateListener extends AbstractListener {
	public ChunkPopulateListener() {
		super(ANOSF.get());
	}

	@EventHandler
	public void onChunkGenerate(ChunkPopulateEvent e) {
		World world = e.getWorld();
	}
}
