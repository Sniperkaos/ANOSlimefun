package dev.cworldstar.anosf.items.machines.drugworkbench;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.ANOSFItem;
import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.WitherProof;

public class DrugWorkbenchVent extends ANOSFItem implements WitherProof {

	private static final ItemStack DRUG_WORKBENCH_VENT_ITEM = new ItemStackBuilder(Material.OXIDIZED_COPPER_GRATE)
			.setName("<gradient:white:gray:aqua:gray>Drug Workbench Vent")
			.setLore(new String[] {
					"",
					Items.ItemTier.makeItemString(ItemTier.SIMPLE, "Multiblock Component")
			})
			.get();
	
	public DrugWorkbenchVent() {
		super(
				ItemRegistry.getItemGroup("MACHINE_CATEGORY"), 
				new SlimefunItemStack("DRUG_WORKBENCH_VENT", DRUG_WORKBENCH_VENT_ITEM),
				RecipeType.ENHANCED_CRAFTING_TABLE,
				new ItemStack[] {
						
				}
		);
	}

	@Override
	public void onAttack(Block block, Wither wither) {
		
	}

}
