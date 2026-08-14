package dev.cworldstar.anosf.items.tools;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.handlers.DurabilityLossHandler;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;

public class ElectricHammer extends SlimefunItem implements Rechargeable {

	public ElectricHammer() {
		super(ItemRegistry.getItemGroup("TOOL_CATEGORY"), new SlimefunItemStack("ELECTRIC_SIEVING_HAMMER", 
				new ItemStackBuilder(Material.IRON_PICKAXE)
				.glowing()
				.name("<yellow>Electric <gray>Sieving Hammer")
				.lore(new String[] {
					"<gray>Drops <aqua>dust<gray> while mining stone.",
					"<gray>AOE break will only affect stone.",
					"",
					LoreBuilder.powerCharged(0, 128000),
					ItemTier.ADVANCED.makeItemString("Tool")
				})
				.attribute("PICKAXE_MINING_EFFICIENCY_NERF", Attribute.MINING_EFFICIENCY, -12, EquipmentSlotGroup.MAINHAND, Operation.ADD_NUMBER)
				.attribute("PICKAXE_NERF", Attribute.BLOCK_BREAK_SPEED, -0.5, EquipmentSlotGroup.MAINHAND, Operation.ADD_NUMBER)
				.get()
		));
		
		addItemHandler(new DurabilityLossHandler() {
			@Override
			public boolean onDurabilityLoss(PlayerItemDamageEvent e, Player p, ItemStack item) {
				if(getItemCharge(item) > 0) {
					e.setCancelled(true);
				}
				return false;
			}			
		});
		addItemHandler(new ToolUseHandler() {

			@Override
			public void onToolUse(BlockBreakEvent e, ItemStack tool, int fortune, List<ItemStack> drops) {
				if(removeItemCharge(tool, 1280F)) {
					Hammer.onToolUse().onToolUse(e, tool, fortune, drops);
				}
			}
		});
		
		ItemRegistry.registerItem(this);
	}
	
	@Override
	public float getMaxItemCharge(ItemStack item) {
		return 128000;
	}

}
