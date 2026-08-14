package dev.cworldstar.anosf.items.weapons;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import dev.cworldstar.anosf.items.recipes.AlloyForgeRecipe;
import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.utils.RecipeUtils;
import dev.cworldstar.libs.cwlib.utils.SlimefunItemEntry;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.WeaponUseHandler;

public class Weapons {
	public static void registerAll() {
		
		//-- Blackened Chrome Alloy Sword
		AbstractSword blackenedChromeAlloySword = new AbstractSword("BLACKENED_CHROME_ALLOY_SWORD", "<gradient:gold:#694e06>", new String[] {}, Material.NETHERITE_SWORD, 18, 3200, false, null) {
			@Override
			public WeaponUseHandler onAttack() {
				return null;
			}
		};
		
		AbstractSword meteorSwordT0 = new AbstractSword("METEOR_SWORD_T0", "<gray>Meteor Sword", new String[] {"lore: todo"}, Material.STONE_SWORD, 8, 1080, false, null) {
			@Override
			public WeaponUseHandler onAttack() {
				return null;
			}
		};
		
		AbstractSword meteorSwordT1 = new AbstractSword("METEOR_SWORD_T1", "<gray>Meteor Sword (<gradient:red:gold><bold>Seared</bold><gray>)", new String[] {"lore: todo"}, Material.GOLDEN_SWORD, 9, 1260, true, null) {
			@Override
			public WeaponUseHandler onAttack() {
				return null;
			}
		};
		
		
		NamespacedKey furnaceRecipeKey = AbstractSFAddon.key("METEOR_SWORD_T1_RECIPE");
		// t1 furnace recipe
		if(Bukkit.getRecipe(furnaceRecipeKey) == null) {
			Bukkit.addRecipe(
				new FurnaceRecipe(
					furnaceRecipeKey,
					meteorSwordT1.getItem(), 
					RecipeUtils.exact(meteorSwordT0.getItem()),
					160,
					1080
				)
			);
		}

		
		AbstractSword meteorSwordT2 = new AbstractSword("METEOR_SWORD_T2", "<gray>Meteor Sword (<gradient:gray:white><bold>Reforged</bold><gray>)", new String[] {"lore: todo"}, Material.IRON_SWORD, 11, 1460, false, null) {
			@Override
			public WeaponUseHandler onAttack() {
				return null;
			}
		};
		
		meteorSwordT2.setRecipeType(RecipeType.HEATED_PRESSURE_CHAMBER);
		meteorSwordT2.setRecipe(new ItemStack[] {
			null,null,null,null,meteorSwordT1.getItem(),null,null,null,null,
		});
		
		AbstractSword meteorSwordT3 = new AbstractSword("METEOR_SWORD_T3", "<gray>Meteor Sword (<white>Hardened<gray>)", new String[] {"lore: todo"}, Material.STONE_SWORD, 14, 1680, true, Map.of(Enchantment.UNBREAKING, 20)) {
			@Override
			public WeaponUseHandler onAttack() {
				return null;
			}
		};
		
		AlloyForgeRecipe.makeRecipe("METEOR_SWORD_T3_RECIPE", 10080, meteorSwordT3.getItem(), new SlimefunItemEntry[] {
				SlimefunItemEntry.of(meteorSwordT2.getItem(), 1),
				SlimefunItemEntry.of(ItemRegistry.get("HEAVY_CHROME_ALLOY_INGOT"), 16)
		});
		
		AbstractSword meteorSwordT4 = new AbstractSword("METEOR_SWORD_T4", "<gray>Meteor Sword (<gradient:dark_green:red:gray>Alloyed<gray>)", new String[] {"lore: todo"}, Material.IRON_SWORD, 18, 1920, true, Map.of(Enchantment.UNBREAKING, 20, Enchantment.FIRE_ASPECT, 20)) {
			@Override
			public WeaponUseHandler onAttack() {
				return null;
			}
		};
		
		AlloyForgeRecipe.makeRecipe("METEOR_SWORD_T4_RECIPE", 40080, meteorSwordT4.getItem(), new SlimefunItemEntry[] {
				SlimefunItemEntry.of(meteorSwordT3.getItem(), 1),
				SlimefunItemEntry.of(ItemRegistry.get("BLACKENED_CHROME_ALLOY_INGOT"), 160)
		});
		
		/*AbstractSword meteorSwordT5 = new AbstractSword("METEOR_SWORD_T5", "<gray>Meteor Sword (<green>Machined<gray>)", new String[] {"lore: todo"}, Material.IRON_SWORD, 18, 1920) {
			@Override
			public WeaponUseHandler onAttack() {
				return null;
			}
		};*/
	}
}
