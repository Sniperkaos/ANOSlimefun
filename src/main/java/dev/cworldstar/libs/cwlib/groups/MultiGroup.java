package dev.cworldstar.libs.cwlib.groups;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuOpeningHandler;

/**
 * 
 * Updated version of InfinityLib's MultiGroup.
 * A group that can hold other groups.
 * 
 * @author cworldstar
 *
 */
public class MultiGroup extends FlexItemGroup implements GroupLayoutHolder {

	private List<ItemGroup> groups = new ArrayList<ItemGroup>();
	private String name = "THIS GROUP HAS NO NAME!";
	private String permission;
	
	public MultiGroup(NamespacedKey key, ItemStack item, String displayName) {
		super(key, item);
		this.name = displayName;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	@Override
	public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideMode layout) {
		if(permission != null) {
			return p.hasPermission(this.permission);
		}
		return true;
	};

	public void add(ItemGroup group) {
		this.groups.add(group);
	}
	
	@Override
	public void open(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
		onOpen(p, profile, mode, 1);
	};
	
	protected void onOpen(Player p, PlayerProfile profile, SlimefunGuideMode mode, int page) {
		SlimefunGuideImplementation guide = Slimefun.getRegistry().getSlimefunGuide(mode);
        profile.getGuideHistory().add(this, page);
        
		ChestMenu menu = new ChestMenu(FormatUtils.formatString(name));
		menu.setEmptySlotsClickable(false);
		menu.addMenuOpeningHandler(new MenuOpeningHandler() {
			@Override
			public void onOpen(Player p) {
				p.playSound(p, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
			}
		});
		
        for (int i = 0; i < 9; i++) {
            menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
		
        String back = FormatUtils.formatString("&7") + Slimefun.getLocalization().getMessage(p, "guide.back.guide");
        menu.addItem(1, ChestMenuUtils.getBackButton(p, "", back));
        menu.addMenuClickHandler(1, (pl, s, is, action) -> {
            profile.getGuideHistory().goBack(guide);
            return false;
        });

        int index = 9;
        int target = (36 * (page - 1)) - 1;
        
        for(ItemGroup agroup : groups) {
        	menu.addItem(index, agroup.getItem(p));
            menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                SlimefunGuide.openItemGroup(profile, agroup, mode, 1);
                return false;
            });
        	index++;
        }
        
        for (int i = 45; i < 54; i++) {
            menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        int pages = target == groups.size() - 1 ? page : (groups.size() - 1) / 36 + 1;
        
        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;
            if (next > 0) {
                onOpen(p, profile, mode, next);
            }
            return false;
        });
        
        menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            int next = page + 1;
            if (next <= pages) {
            	onOpen(p, profile, mode, next);
            }
            return false;
        });
        
        menu.open(p);
	};
	
    @Override
    public void register(SlimefunAddon addon) {
        super.register(addon);
        for (ItemGroup category : groups) {
            if (!category.isRegistered()) {
                category.register(addon);
            }
        }
    }
    
    public void complete(SlimefunAddon addon) {
    	this.register(addon);
    }
}
