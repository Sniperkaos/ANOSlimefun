package dev.cworldstar.anosf.impl.recipetype;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.impl.groups.ANOSFRecipeGroup.ANOSFHistory;
import dev.cworldstar.anosf.items.recipes.MolecularWorkbenchRecipe;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;

public class MolecularWorkbenchRecipeType extends ExtendedRecipeType {

	private static int[] BACKGROUND_SLOTS = new int[] {
			8,
			9,17,
			18,26,
			27,35,
			36,44,
			45,46,47,48,49,50,51,52,53
	};
	
	private static final ItemStack NO_ITEM = new ItemStackBuilder(Material.BARRIER).name("<red>No Item").build();
	
	public MolecularWorkbenchRecipeType(NamespacedKey key, ItemStack item) {
		super(key, item);
	}
	
	@Override
	public ChestMenu display(Player player, PlayerProfile profile, SlimefunItem item, SlimefunGuideImplementation guide, ANOSFHistory history) {
		MolecularWorkbenchRecipe recipe = MolecularWorkbenchRecipe.lookupOutput(item.getItem());
		if(recipe == null) {
			recipe = MolecularWorkbenchRecipe.emptyRecipe();
		}
		
		ChestMenu menu = new ChestMenu(recipe.getID());
		draw(menu, new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).empty().get(), BACKGROUND_SLOTS);
		menu.getContents();
		menu.replaceExistingItem(0, ChestMenuUtils.getBackButton(player, ""));
        menu.addMenuClickHandler(0, new MenuClickHandler() {
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				history.goBack();
				return false;
			}
        });
		
		menu.setEmptySlotsClickable(false);
		menu.setPlayerInventoryClickable(false);
		
		ItemStack rItem = this.getItem(player);
		rItem.lore(FormatUtils.lore(new String[] {
				"<gray>Molecular Deviation Chance: <red>" + String.valueOf(recipe.getDeviationChance()),
				"<gray>Molecular Deviation Threshold: <gold>" + String.valueOf(recipe.getDeviationThreshhold()),
				"<gray>Required Work: <aqua>" + String.valueOf(recipe.getWork())
		}));
		
		menu.replaceExistingItem(49, rItem);
		menu.addMenuClickHandler(49, ChestMenuUtils.getEmptyClickHandler());

		ArrayList<ItemStack> recipeItems = recipe.contents();
		for(ItemStack recipeItem : recipeItems) {
			int slot = menu.toInventory().firstEmpty();
			if(slot == -1) {
				Bukkit.getLogger().warning("Recipe " + recipe.getID() + " is malformed! If you see this, inform a developer!");
				break;
			}
			if(recipeItem == null) {
				recipeItem = NO_ITEM;
			}
			menu.replaceExistingItem(slot, recipeItem.clone());
			menu.addMenuClickHandler(slot, new MenuClickHandler() {
				@Override
				public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
					tryOpen(player, profile, guide, item, history);
					return false;
				}
	        });
		}
        
        return menu;
	}
}
