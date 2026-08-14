package dev.cworldstar.anosf.items.machines.accelerator;

import org.bukkit.Material;

import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.machines.Casing;
import dev.cworldstar.libs.cwlib.ItemRegistry;

public class AcceleratorCasing extends Casing {
	public AcceleratorCasing() {
		super(
				ItemRegistry.getItemGroup("MACHINE_CATEGORY"), 
				"ACCELERATOR_CASING", 
				Material.NETHERITE_BLOCK, 
				ItemTier.HIGH, 
				"<#cfcabc>Accelerator Casing</#cfcabc>", 
				"Multiblock Component"
		);
	}
}
