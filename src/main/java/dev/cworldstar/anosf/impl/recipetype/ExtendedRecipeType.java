package dev.cworldstar.anosf.impl.recipetype;

import java.util.function.BiConsumer;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.impl.groups.ANOSFRecipeGroup.ANOSFHistory;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

public abstract class ExtendedRecipeType extends RecipeType {
	
	protected abstract ChestMenu display(Player p, PlayerProfile profile, SlimefunItem item, SlimefunGuideImplementation guide, ANOSFHistory history);
	
	public void triggerDisplay(Player p, PlayerProfile profile, SlimefunItem item, SlimefunGuideImplementation guide, ANOSFHistory history) {
		ChestMenu display = display(p, profile, item, guide, history);
		if(display == null) {
			guide.displayItem(profile, item, true);
			return;
		}
		history.add(display);
		p.playSound(Sound.sound(sound -> sound.type(Key.key("minecraft:item.book.page_turn")).pitch(RandomUtils.nextFloat(1, 2))));
		display.open(p);
	}
	
	protected void draw(ChestMenu menu, ItemStack item, int... slots) {
		for(int slot : slots) {
			menu.addItem(slot, item, ChestMenuUtils.getEmptyClickHandler());
		}
	}
	
	public ExtendedRecipeType(ItemStack item, String machine) {
		super(item, machine);
	}

	public ExtendedRecipeType(NamespacedKey key, ItemStack item) {
		super(key, item);
	}
	
    public ExtendedRecipeType(NamespacedKey key, ItemStack item, BiConsumer<ItemStack[], ItemStack> callback, String... lore) {
        super(key, item, callback, lore);
    }
    
	public void tryOpen(Player player, PlayerProfile profile, SlimefunGuideImplementation guide, ItemStack item, ANOSFHistory history) {
		if(item.getType().equals(Material.BARRIER)) {
			return;
		}
		SlimefunItem sfItem = SlimefunItem.getByItem(item);
		if(sfItem != null) {
			RecipeType type = sfItem.getRecipeType();
			if(type instanceof ExtendedRecipeType) {
				((ExtendedRecipeType) type).triggerDisplay(player, profile, sfItem, guide, history);
			} else {
				SlimefunGuide.displayItem(profile, sfItem, true);
			}
			return;
		} 
		SlimefunGuide.displayItem(profile, item, true);
	}
}
