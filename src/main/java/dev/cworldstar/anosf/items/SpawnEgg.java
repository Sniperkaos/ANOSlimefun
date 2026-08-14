package dev.cworldstar.anosf.items;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import dev.cworldstar.anosf.entities.AbstractEnemy;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

public class SpawnEgg extends SlimefunItem {

	private static SlimefunItemStack createSFItem(
			String id, 
			String mob) {
		return new SlimefunItemStack(id, new ItemStackBuilder(Material.EGG)
				.name(mob + " Spawn Egg")
				.build()
		);
	}

	public SpawnEgg(String id, String mob) {
		super(ItemRegistry.getCreativeItemGroup(), createSFItem("SPAWN_EGG_"+id, mob));
		addItemHandler(new ItemUseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				e.cancel();
				Class<AbstractEnemy> clazz;
				Optional<Block> clickedBlock = e.getClickedBlock();
				if(!(clickedBlock.isPresent())) return;
				try {
					clazz = (Class<AbstractEnemy>) Class.forName(mob);
					clazz.getDeclaredConstructor(Location.class).newInstance(e.getClickedBlock().get().getLocation().add(new Vector(0, 1, 0)));
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
					e1.printStackTrace();
				}
			}			
		});
		ItemRegistry.registerItem(this);
	}
}
