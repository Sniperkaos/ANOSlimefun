package dev.cworldstar.anosf.impl.recipetype;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.impl.groups.ANOSFRecipeGroup.ANOSFHistory;
import dev.cworldstar.anosf.items.recipes.ParticleAcceleratorRecipe;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.builders.PlayerHeadBuilder;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import net.kyori.adventure.text.Component;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;

public class AcceleratorRecipeType extends ExtendedRecipeType {

	private static int[] BACKGROUND_SLOTS = new int[] {
			1,2,4,6,7,8,
			9,10,11,13,15,16,17,
			18,19,20,24,25,26,
			27,28,29,30,32,33,34,35
	};
	
	private static int[] LEFT_INPUT_SLOTS = new int[] {
		3, 11	
	};
	
	private static int[] RIGHT_INPUT_SLOTS = new int[] {
			5, 15
	};
	
	private static int[] OUTPUT_ITEM_SLOTS = new int[] {
			21, 23, 31
	};
	
	private static final ItemStack LEFT_INPUT_ITEM = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setName("<gray>Input").item();
	private static final ItemStack RIGHT_INPUT_ITEM = new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("<gold>Collision Input").item();
	private static final ItemStack OUTPUT_ITEM = new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE).setName("<green>Output").item();
	private static final ItemStack RECIPE_TYPE_ITEM = new PlayerHeadBuilder().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiYWYxYmZiMWZlNDdjNTczZjM0ODIyZmVjNDUwZDNhY2RmOGNlYTAzZGIxYzU1ZmYxNmM1YjFiN2I0ZDU1YyJ9fX0=")
			.name("<red>Particle Accelerator Recipe")
			.lore(new String[] {
					"<gray>← <red>Input",
					"<gold>Collider <gray>→",
					"<green>Output <gray>↓"
			})
			.item();
	
	public AcceleratorRecipeType(NamespacedKey key, ItemStack item) {
		super(key, item);
	}

	@Override
	public ChestMenu display(Player player, PlayerProfile profile, SlimefunItem item, SlimefunGuideImplementation guide, ANOSFHistory history) {
		
		ArrayList<ParticleAcceleratorRecipe> recipes = ParticleAcceleratorRecipe.lookupOutput(item.getItem());
		// TODO: support multiple recipes of the same output
		ParticleAcceleratorRecipe recipe = recipes.get(0);
		
		ChestMenu menu = new ChestMenu(recipe.getRecipeId());
		draw(menu, new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE).empty().get(), BACKGROUND_SLOTS);
		
		ItemStack recipeTypeItem = RECIPE_TYPE_ITEM.clone();
		recipeTypeItem.editMeta(meta -> {
			List<Component> lore = meta.lore();
			lore.add(0, FormatUtils.mm(" "));
			lore.add(1, FormatUtils.mm("<gray>Required Work: <aqua>" + String.valueOf(recipe.getRequiredWork())));
			lore.add(2, FormatUtils.mm("<gray>Approximate Consumption: <yellow>" + String.valueOf((long) (200000000L * recipe.getRequiredWork())) + "J"));
			meta.lore(lore);
		});
		
		draw(menu, LEFT_INPUT_ITEM, LEFT_INPUT_SLOTS);
		draw(menu, RIGHT_INPUT_ITEM, RIGHT_INPUT_SLOTS);
		draw(menu, OUTPUT_ITEM, OUTPUT_ITEM_SLOTS);
		draw(menu, recipeTypeItem, 13);
		
		menu.setEmptySlotsClickable(false);
		menu.setPlayerInventoryClickable(false);
		
		ItemStack recipeInput = recipe.getInputItem();
		ItemStack collisionInput = recipe.getCollisionItem();
		ItemStack outputItem = recipe.getOutputItem();
		
		if(collisionInput == null) {
			collisionInput = new ItemStackBuilder(Material.BARRIER).name("<red>No Collision Input").item();
		}
		
        menu.addItem(12, recipeInput, new MenuClickHandler() {
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				tryOpen(player, profile, guide, item, history);
				return false;
			}
        });
        
        menu.addItem(14, collisionInput, new MenuClickHandler() {
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				tryOpen(player, profile, guide, item, history);
				return false;
			}
        });
        
        menu.addItem(22, outputItem, ChestMenuUtils.getEmptyClickHandler());
		
        menu.addItem(0, ChestMenuUtils.getBackButton(player, ""), new MenuClickHandler() {
			@Override
			public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
				history.goBack();
				return false;
			}
        });
        
        return menu;
	}
}
