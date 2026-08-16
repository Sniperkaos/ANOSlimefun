package dev.cworldstar.anosf.items.weapons;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.items.ANOSFItem;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.impl.PreventDisenchant;
import dev.cworldstar.libs.cwlib.impl.PreventEnchant;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.WeaponUseHandler;

/**
 * Creates a sword without needing to spam new SlimefunItem.
 * Prevents disenchant by default.
 * @author cworldstar
 */
public abstract class AbstractSword extends ANOSFItem implements PreventDisenchant, PreventEnchant {
	public AbstractSword(
			String id,
			String displayName,
			String[] lore,
			Material material,
			int damage,
			long durability,
			boolean glowing,
			Map<Enchantment, Integer> enchants
	) {
		super(
			ItemRegistry.getItemGroup("WEAPON_CATEGORY"), 
			new SlimefunItemStack(id, new ItemStackBuilder(material)
				.attribute(
					Attribute.ATTACK_DAMAGE, 
					new AttributeModifier(
						ANOSF.key("ASWORD_DMG"), 
						damage, 
						Operation.ADD_NUMBER, 
						EquipmentSlotGroup.MAINHAND
					)
				)
				.name(displayName)
				.lore(lore)
				.durability(durability)
				.glowing(glowing)
				.enchants(enchants)
				.build()
			), 
			RecipeType.NULL, 
			null
		);
		
		WeaponUseHandler handler = onAttack();
		if(handler != null) {
			addItemHandler(handler);
		}
	}
	
	public abstract WeaponUseHandler onAttack();
}
