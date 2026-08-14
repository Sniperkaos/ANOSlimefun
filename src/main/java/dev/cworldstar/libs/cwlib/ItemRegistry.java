package dev.cworldstar.libs.cwlib;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.events.RegistryEvent;
import dev.cworldstar.libs.cwlib.events.RegistryPreFinalizeEvent;
import dev.cworldstar.libs.cwlib.groups.InvisibleGroup;
import dev.cworldstar.libs.cwlib.groups.MultiGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import net.md_5.bungee.api.ChatColor;

public class ItemRegistry {
	
	private static final ItemStack DEFAULT_GROUP_ITEM = new ItemStackBuilder(Material.OAK_SAPLING)
			.setName("<gradient:red:green:dark_green>ANO Slimefun")
			.setLore(new String[] {
					"",
			}).get();
	
	private static final ItemStack CREATIVE_GROUP_ITEM = new ItemStackBuilder(Material.PINK_BUNDLE)
			.name("<gradient:light_purple:dark_purple>ANOSF Creative Items")
			.lore(new String[] {
					
			}).get();
	
	private static boolean finalized = false;
	private static boolean closed = false;
	private static LinkedHashMap<String, SlimefunItem> items = new LinkedHashMap<String, SlimefunItem>();
	private static Map<String, ItemGroup> groups = new HashMap<String, ItemGroup>();
	private static final MultiGroup DEFAULT_ITEM_GROUP = new MultiGroup(ANOSF.key("DEFAULT_ITEM_GROUP"),DEFAULT_GROUP_ITEM,ChatColor.DARK_RED + "ANO SF");
	private static final InvisibleGroup CREATIVE_ITEM_GROUP = new InvisibleGroup(ANOSF.key("ANOSF_CREATIVE_ITEMS"), CREATIVE_GROUP_ITEM, 1);
	
	public ItemRegistry() {
		throw new UnsupportedOperationException("This is a static class!");
	}
	
	public static @Nonnull InvisibleGroup getCreativeItemGroup() {
		return CREATIVE_ITEM_GROUP;
	}
	
	public static @Nonnull MultiGroup getDefaultItemGroup() {
		if(!DEFAULT_ITEM_GROUP.isRegistered()) {
			DEFAULT_ITEM_GROUP.register(ANOSF.get());
		}
		return DEFAULT_ITEM_GROUP;
	}
	
	/**
	 * This method will never return null,
	 * as item groups are dynamically registered ( with this method ) 
	 * if the given group key doesn't exist.
	 * @param String key
	 * @return {@link ItemGroup}
	 */
	public static @Nonnull ItemGroup getItemGroup(String key) {
		if(!groups.containsKey(key)) {
			registerItemGroup(new InvisibleGroup(ANOSF.key(key), new ItemStackBuilder(Material.BARRIER).name(key).build(), 99));
		}
		return groups.get(key.toLowerCase());
	}
	
	public static ItemGroup registerItemGroup(InvisibleGroup group) {
		ANOSF.get().getLogger().log(Level.INFO, "item group added " + group.getKey().getKey());
		groups.put(group.getKey().getKey(), group);
		DEFAULT_ITEM_GROUP.add(group.proxy());
		return group;
	}
	
	public static void registerItem(SlimefunItem item) {
		if(finalized) {
			throw new Error("The registry has already been finalized! No new items may be added.");
		}
		items.put(item.getId(), item);
	}
	
	public static @Nullable SlimefunItem getRegistryItem(String s) {
		return items.get(s);
	}
	
	public static @Nullable ItemStack getRegistryItemAsItemStack(String s) {
		return getRegistryItem(s).getItem();
	}
	
	public static void finalizeRegistry() {
		if(closed == true) {
			return;
		}
		closed = true; //-- Prevent race condition errors
		items.forEach((String id, SlimefunItem item) -> {
			RegistryEvent event = new RegistryEvent(item);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if(!event.cancelled()) {
				Bukkit.getLogger().log(Level.INFO, item.getId());
				item.register(ANOSF.get());
			}
		});
		//-- register the default item group if not already registered
		getDefaultItemGroup(	);
		Bukkit.getServer().getPluginManager().callEvent(new RegistryPreFinalizeEvent());
		finalized = true;
	}

	public static ItemStack get(String string) {
		return getRegistryItemAsItemStack(string);
	}
}