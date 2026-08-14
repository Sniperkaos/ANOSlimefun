package dev.cworldstar.anosf.items.materials;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;

public class WhiteParticle extends Particle {
	public WhiteParticle() {
		super("WHITE_PARTICLE", Material.GLASS_BOTTLE, ItemTier.HIGH, "<gradient:red:gold:white>White Particle", "Material", new String[] {
				"",
				"<gradient:white:gray:white><italic>\"The building block of the universe- at this time of day? In this part of your palm?",
				"<gradient:white:gray:white><italic>Localized entirely within this bottle??\" [static] \"<green>Yes!</green>\"</gradient>",
				"<gradient:white:gray:white><italic>\"May I see it?\" [pause] \"<green>No.</green>\"</gradient>",
				"<gray> Recorded Conversation, 21XX",
			}, Items.ACCELERATOR_RECIPE_TYPE, new ItemStack[] {
				ItemRegistry.getRegistryItem("ANOIUM_ALLOY_INGOT").getItem(),
				ItemRegistry.getRegistryItem("HBPARTICLE").getItem(),
		});

	}

}
