package dev.cworldstar.anosf.items.weapons;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.CustomDurabilityHandler;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.handlers.DurabilityLossHandler;
import dev.cworldstar.libs.cwlib.handlers.ItemMendHandler;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class ChromeSword extends SlimefunItem {

	private static final SlimefunItemStack CHROME_SWORD_ITEM = new SlimefunItemStack("CHROME_SWORD_ITEM", 
		new ItemStackBuilder(Material.IRON_SWORD)
			.glowing()
			.name("<#C0C0C0>Chrome Sword")
			.setMaxDurability(32)
			.attribute("DAMAGE", Attribute.ATTACK_DAMAGE, 16, EquipmentSlotGroup.MAINHAND, Operation.ADD_NUMBER)
			.lore(new String[] {
					"<italic><gradient:white:gray:white:white>\"Surely, we can't make steel better?\"",
					"<italic><gradient:white:white:gray:white>I laughed, my face morphing into a disdaining smirk.",
					"<italic><#C0C0C0>\"Those who know...\"",
					"",
					"<gradient:white:gray:white:white><underlined>Chrome Sword Abilities:", 
					" <gray>[<#C0C0C0>Shiny<gray>]: This sword is shiny!",
					" <gray>[<#964B00>Brittle<gray>]: This sword's durability is equal",
					"<gray>to a <gold>Golden Sword<gray>.",
					"",
					ItemTier.BASIC.makeItemString("Weapon")
			}).build()
	);
	
	public ChromeSword() {
		super(ItemRegistry.getItemGroup("WEAPON_CATEGORY"), CHROME_SWORD_ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {null,ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(), null, null, ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(), null, null, new ItemStack(Material.STICK), null});
		ItemRegistry.registerItem(this);
		addItemHandler(new DurabilityLossHandler() {
			@Override
			public boolean onDurabilityLoss(PlayerItemDamageEvent e, Player p, ItemStack item) {
				return CustomDurabilityHandler.durabilityLossHandler(e, p, item);
			}
		});
		addItemHandler(new ItemMendHandler() {
			@Override
			public boolean onItemMend(PlayerItemMendEvent e, Player p, ItemStack item) {
				return CustomDurabilityHandler.durabilityGainHandler(e, p, item);
			}
		});
	}

}
