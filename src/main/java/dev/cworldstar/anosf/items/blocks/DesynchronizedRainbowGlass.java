package dev.cworldstar.anosf.items.blocks;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.block.Block;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ColoredMaterial;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;

/**
 * 
 */
public class DesynchronizedRainbowGlass extends SlimefunItem {
	
	private static final SlimefunItemStack BASE_DESYNC_GLASS = new ItemStackBuilder(Material.GLASS).asSlimefunItemStack("DESYNC_RAINBOW_GLASS");
	public static final ArrayList<Material> COLORED_MATERIALS = new ArrayList<Material>();
	
	static {
		COLORED_MATERIALS.addAll(ColoredMaterial.STAINED_GLASS.asList());
	}
	
	public DesynchronizedRainbowGlass(ItemGroup itemGroup, SlimefunItemStack item) {
		super(itemGroup, BASE_DESYNC_GLASS);
		addItemHandler(new BlockTicker() {

			@Override
			public boolean isSynchronized() {
				return false;
			}
			
			private int current = 0;

			@Override
			public void tick(Block b, SlimefunItem item, Config data) {
				if(b.getType().isAir()) return;	
				Slimefun.runSync(() -> {
					b.setType(COLORED_MATERIALS.get(current));
					current += 1;
					if(COLORED_MATERIALS.size()-1 < current) {
						current = 0;
					}
				});
			}
		});
	
	}

	
	
}
