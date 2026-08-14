package dev.cworldstar.anosf.impl.groups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.impl.recipetype.ExtendedRecipeType;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.core.guide.options.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import lombok.Getter;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import net.guizhanss.infinityexpansion2.api.InfinityExpansion2API;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

public class ANOSFRecipeGroup extends FlexItemGroup {
	
	public ANOSFRecipeGroup(NamespacedKey key, ItemStack item, int tier) {
		super(key, item, tier);
	}
	
	protected static final HashMap<UUID, ANOSFHistory> HISTORY = new HashMap<UUID, ANOSFHistory>();
	private boolean visible = false;
	
	public void visible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean visible() {
		return visible;
	}
	
	
	private List<SlimefunItem> items = new ArrayList<SlimefunItem>();
	
	@Override
	public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideMode layout) {
		return visible();
	}
	
	
	public @NotNull int firstEmpty(ChestMenu menu) {
		return menu.toInventory().firstEmpty();
	}
	
	@Override
	public void open(Player p, PlayerProfile profile, SlimefunGuideMode layout) {
		p.playSound(Sound.sound(sound -> sound.type(Key.key("minecraft:item.book.page_turn")).pitch(RandomUtils.nextFloat(1, 2))));
		
		ANOSFHistory history = HISTORY.get(p.getUniqueId());
		
		if(history == null) {
			HISTORY.put(p.getUniqueId(), new ANOSFHistory(p));
			history = HISTORY.get(p.getUniqueId());
		} else {
			if(history.isMainMenu()) {
				profile.getGuideHistory().add(this, 0);
			}
		}
		
		if(history.current() != null && !history.isMainMenu()) {
			history.current().open(p);
			return;
		}
		
		ArrayList<ChestMenu> pages = new ArrayList<ChestMenu>();
		int maxPages = (int) Math.ceil(((double) items.size() / 36));
		for(int i=0; i<maxPages; i++) {
			ChestMenu page = new ChestMenu(this.getDisplayName(p));
			final PageInfo info = new PageInfo(i);
			pages.add(page);
			// setup the menu
			
			page.addItem(1, ChestMenuUtils.getBackButton(p, ""), new MenuClickHandler() {
				@Override
				public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
					HISTORY.get(p.getUniqueId()).goBack();
					return false;
				}
			});
			
			ChestMenuUtils.drawBackground(page,0,2,3,4,5,6,7,8,45,47,48,49,50,51,53);
			page.getContents(); // chest menu is a piece of shit piggy class im so close to just making my own menu implementation
			page.replaceExistingItem(52, ChestMenuUtils.getNextButton(p, i+1, maxPages));
			page.addMenuClickHandler(52, (player, slot, item, action) -> {
				int nextPage = info.getPageIndex() + 1;
				if(nextPage > pages.size()-1) {
					nextPage = 0;
				}
				pages.get(nextPage).open(player);
				return false;
			});
			
			page.replaceExistingItem(46, ChestMenuUtils.getPreviousButton(p, i+1, maxPages));
			
			page.addMenuClickHandler(46, (player, slot, item, action) -> {
				int nextPage = info.getPageIndex() - 1;
				if(nextPage < 0) {
					nextPage = pages.size()-1;
				}
				pages.get(nextPage).open(player);
				return false;
			});
			
			for(int i2=0; i2<=35; i2++) {
				int where = (i*36) + i2;
				int slot = i2+9;
				if(where >= items.size()) {
					break;
				}
				SlimefunItem sfItem = items.get(where);
				page.replaceExistingItem(slot, sfItem.getItem().clone());
				page.addMenuClickHandler(slot, new MenuClickHandler() {
					@Override
					public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
						// handle creative giving
						if(layout.equals(SlimefunGuideMode.CHEAT_MODE)) {
							p.give(sfItem.getItem());
							return false;
						}
						
						RecipeType itemRecipeType = sfItem.getRecipeType();
						if(itemRecipeType instanceof ExtendedRecipeType) {
							((ExtendedRecipeType) itemRecipeType).triggerDisplay(p, profile, sfItem, Slimefun.getRegistry().getSlimefunGuide(layout), HISTORY.get(p.getUniqueId()));
						}
						else {
							// check if IE2 is enabled
							if(Bukkit.getServer().getPluginManager().isPluginEnabled("InfinityExpansion2")) {
								InfinityExpansion2API.openGuide(p, sfItem, item);
							}
							// display normally
							SlimefunGuide.displayItem(profile, sfItem, true);
						}
						return false;
					}
				});
			}
		}
		history.add(pages.get(0));
		pages.get(0).open(p);
	}

	public void addItem(SlimefunItem item) {
		items.add(item);
	}

	public void removeItem(SlimefunItem item) {
		items.remove(item);
	}
	
	public static final class ANOSFHistory {
		private ArrayList<ChestMenu> history = new ArrayList<ChestMenu>();
		private UUID owner;
		@Getter
		private int position = 0;
		
		public ANOSFHistory(Player owner) {
			this.owner = owner.getUniqueId();
		}
		
		public boolean isMainMenu() {
			return history.isEmpty();
		}

		public void add(ChestMenu menu) {
			history.addLast(menu);
			position = history.size();
		}
		
		public @Nullable ChestMenu current() {
			if(history.isEmpty()) {
				return null;
			}
			return history.getLast();
		}

		public void goBack() {
			history.removeLast();
			if(history.isEmpty()) {
				PlayerProfile profile = Slimefun.getRegistry().getPlayerProfiles().get(owner);
				profile.getGuideHistory().goBack(Slimefun.getRegistry().getSlimefunGuide(SlimefunGuideMode.SURVIVAL_MODE));
				return;
			}
			Player p = Bukkit.getPlayer(owner);
			p.playSound(Sound.sound(sound -> sound.type(Key.key("minecraft:item.book.page_turn")).pitch(RandomUtils.nextFloat(1, 2))));
			history.getLast().open(p);
		}
	}
}
