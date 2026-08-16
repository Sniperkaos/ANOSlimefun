package dev.cworldstar.anosf.items.liquids;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.gases.ANOSFGases;
import dev.cworldstar.libs.cwlib.impl.SlimefunLiquidStack;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;

public class Water extends SlimefunLiquidStack {
	public Water(ItemGroup itemGroup, SlimefunItemStack item) {
		super(itemGroup, item, new ItemStack(Material.ICE), ANOSFGases.STEAM, 212.1F);
	}
}
