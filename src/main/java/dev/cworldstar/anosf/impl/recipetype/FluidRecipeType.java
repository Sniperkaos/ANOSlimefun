package dev.cworldstar.anosf.impl.recipetype;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.impl.groups.ANOSFRecipeGroup.ANOSFHistory;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

public class FluidRecipeType extends ExtendedRecipeType {
	public FluidRecipeType(NamespacedKey key, ItemStack item) {
		super(key, item);
	}

	@Override
	protected ChestMenu display(Player p, PlayerProfile profile, SlimefunItem item, SlimefunGuideImplementation guide,
			ANOSFHistory history) {
		return null;
	}
}
