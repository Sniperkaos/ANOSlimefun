package dev.cworldstar.anosf.items.machines.tiered;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.IngotFormer;
import dev.cworldstar.anosf.items.recipes.DustExtractorRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

public class TieredIngotFormers {

	private static ArrayList<DustExtractorRecipe> recipes = new ArrayList<DustExtractorRecipe>();
	
    private static final SlimefunItemStack[] dusts = new SlimefunItemStack[] {
            SlimefunItems.IRON_DUST,
            SlimefunItems.GOLD_DUST,
            SlimefunItems.COPPER_DUST,
            SlimefunItems.TIN_DUST,
            SlimefunItems.ZINC_DUST,
            SlimefunItems.ALUMINUM_DUST,
            SlimefunItems.MAGNESIUM_DUST,
            SlimefunItems.LEAD_DUST,
            SlimefunItems.SILVER_DUST
    };
	
	public static void register() {
		
		recipes.add(new DustExtractorRecipe("IRON_DUST_TO_MINECRAFT_IRON", SlimefunItems.IRON_DUST.asOne(), new ItemStack[] {new ItemStack(Material.IRON_INGOT, 1)}, 40, 1200));
		recipes.add(new DustExtractorRecipe("COPPER_DUST_TO_MINECRAFT_COPPER", SlimefunItems.COPPER_DUST.asOne(), new ItemStack[] {new ItemStack(Material.COPPER_INGOT, 1)}, 20, 1200));
		recipes.add(new DustExtractorRecipe("GOLD_DUST_TO_MINECRAFT_GOLD", SlimefunItems.GOLD_DUST.asOne(), new ItemStack[] {new ItemStack(Material.GOLD_INGOT, 1)}, 40, 1200));

		
		for(SlimefunItemStack dust : dusts) {
			try {
				if(
					dust.getItemId().equalsIgnoreCase(SlimefunItems.COPPER_DUST.getItemId()) ||
					dust.getItemId().equalsIgnoreCase(SlimefunItems.IRON_DUST.getItemId()) ||
					dust.getItemId().equalsIgnoreCase(SlimefunItems.GOLD_DUST.getItemId())
				) { 
					continue;
				}
				SlimefunItem item = SlimefunItem.getById(dust.getItemId().replace("DUST", "INGOT"));
				if(item == null) {
					Bukkit.getLogger().log(Level.SEVERE, "Attempted to register recipe " + dust.getItemId() + "_TO_INGOT" + ", could not find associated ingot " + dust.getItemId().replace("DUST", "INGOT") + "!");
					continue;
				}
				recipes.add(new DustExtractorRecipe(dust.getItemId() + "_TO_INGOT", dust.asOne(), new ItemStack[] {item.getItem()}, 20, 1000));
			} catch(NullPointerException e) {
				Bukkit.getLogger().log(Level.SEVERE, "Attempted to register recipe " + dust.getItemId() + "_TO_INGOT" + ", could not find associated ingot " + dust.getItemId().replace("DUST", "INGOT") + "!");
			}
		}
		
		for(ItemTier tier : Items.ItemTier.iterator()) {
			if(tier.getTier() < ItemTier.ADVANCED.getTier()) {
				continue;
			}
			ItemRegistry.registerItem(new IngotFormer(
					ItemRegistry.getItemGroup("MACHINE_CATEGORY"),
					Material.COPPER_BLOCK,
					tier,
					"ANOSF_INGOT_FORMER_" + tier.toString(),
					"Machine",
					"<gradient:yellow:gray>Ingot Former</gradient>",
					new String[] {
							
					},
					RecipeType.NULL,
					(((tier.getTier() - 1) * 4) - 2 + ((tier.getTier() - 1) * 10)),
					new ItemStack[0],
					recipes
			));
		}
	}
}
