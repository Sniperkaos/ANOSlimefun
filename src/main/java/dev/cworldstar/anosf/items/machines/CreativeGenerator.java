package dev.cworldstar.anosf.items.machines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;

public class CreativeGenerator extends SlimefunItem implements EnergyNetProvider {

	private static final ItemStack CREATIVE_GENERATOR_STACK = new ItemStackBuilder(Material.CHISELED_COPPER)
			.name(ItemTier.CREATIVE.makeName("Decay Generator"))
			.lore(new String[] {
					"<gray>Passively makes power using particles.",
					"<gray>This one uses none.",
					"",
					ItemTier.CREATIVE.makeItemString("Generator")
			})
			.get();
	
	public CreativeGenerator() {
		super(ItemRegistry.getCreativeItemGroup(), new SlimefunItemStack("ANOSF_CREATIVE_GENERATOR", CREATIVE_GENERATOR_STACK));
		ItemRegistry.registerItem(this);
	}

	@Override
	public EnergyNetComponentType getEnergyComponentType() {
		return EnergyNetComponentType.GENERATOR;
	}

	@Override
	public int getCapacity() {
		return (Integer.MAX_VALUE - 1);
	}

	@Override
	public int getGeneratedOutput(Location l, Config data) {
		return (Integer.MAX_VALUE - 1);
	}

}
