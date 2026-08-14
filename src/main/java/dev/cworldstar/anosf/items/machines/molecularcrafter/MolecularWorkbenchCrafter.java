package dev.cworldstar.anosf.items.machines.molecularcrafter;

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

public class MolecularWorkbenchCrafter extends ANOSFItem implements WitherProof {

	private static final ItemStack MOLECULAR_WORKBENCH_ITEM = new ItemStackBuilder(Material.CHISELED_POLISHED_BLACKSTONE)
			.setName("<gradient:white:gray:aqua:gray>Molecular Workbench Crafter")
			.setLore(new String[] {
					"",
					Items.ItemTier.makeItemString(ItemTier.HIGH, "Multiblock Component")
			})
			.get();
	
	public MolecularWorkbenchCrafter() {
		super(
				ItemRegistry.getItemGroup("MACHINE_CATEGORY"), 
				new SlimefunItemStack("MOLECULAR_WORKBENCH_CRAFTER", MOLECULAR_WORKBENCH_ITEM),
				RecipeType.ENHANCED_CRAFTING_TABLE,
				new ItemStack[] {
						
				}
		);
	}

	@Override
	public void onAttack(Block block, Wither wither) {
		
	}

}
