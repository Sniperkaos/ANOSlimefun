package dev.cworldstar.anosf.impl.recipetype;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.impl.groups.ANOSFRecipeGroup.ANOSFHistory;
import dev.cworldstar.anosf.items.recipes.AlloyForgeRecipe;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import dev.cworldstar.libs.cwlib.utils.SlimefunItemEntry;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import net.kyori.adventure.text.Component;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;

public class AlloyForgeRecipeType extends ExtendedRecipeType {

	public AlloyForgeRecipeType(NamespacedKey key, ItemStack item) {
		super(key, item);
	}
	
	private static final int[] BACKGROUND_SLOTS = new int[] {
			0,1,2,3,4,5,6,7,8,
			9,17,
			18,26,
			27,28,29,30,32,33,34,35
	};

	private ItemStack make(ItemStack item, long amount) {
		item.editMeta(meta -> {
			Component displayName = meta.displayName();
			if(displayName == null) {
				displayName = item.effectiveName();
			}
			meta.displayName(displayName.append(FormatUtils.mm(" <gray>x <aqua>" + String.valueOf(amount))));
		});
		return item;
	}
	
	
	@Override
	protected ChestMenu display(Player p, PlayerProfile profile, SlimefunItem item, SlimefunGuideImplementation guide,
			ANOSFHistory history) {
		AlloyForgeRecipe recipe = AlloyForgeRecipe.getAlloyForgeRecipe(item.getItem());
		
		ChestMenu menu = new ChestMenu(recipe.getId());
		draw(menu, new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).empty().item(), BACKGROUND_SLOTS);
		menu.addItem(31, item.getItem(), ChestMenuUtils.getEmptyClickHandler());
		menu.replaceExistingItem(0, ChestMenuUtils.getBackButton(p));
        menu.addMenuClickHandler(0, new MenuClickHandler() {
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				history.goBack();
				return false;
			}
        });
		menu.getItemInSlot(0);
		for(SlimefunItemEntry entries : recipe.inputs()) {
			int slot = menu.toInventory().firstEmpty();
			menu.replaceExistingItem(slot, make(entries.itemStack(), entries.getAmount()));
			menu.addMenuClickHandler(slot, new MenuClickHandler() {
				@Override
				public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
					tryOpen(p, profile, guide, item, history);
					return false;
				}
	        });
		}
		
		return menu;
	}

}
