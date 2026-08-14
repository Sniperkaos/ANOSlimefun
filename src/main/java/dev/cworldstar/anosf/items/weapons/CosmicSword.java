package dev.cworldstar.anosf.items.weapons;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.recipes.MolecularWorkbenchRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.handlers.SwordSwingHandler;
import dev.cworldstar.libs.cwlib.impl.armor.ArmorSetPiece;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.papermc.paper.event.player.PlayerArmSwingEvent;

public class CosmicSword extends SlimefunItem {

	private static final SlimefunItemStack COSMIC_SWORD_ITEM = new SlimefunItemStack("COSMIC_SWORD_ITEM", 
		new ItemStackBuilder(Material.GOLDEN_SWORD)
			.glowing()
			.flag(ItemFlag.HIDE_ATTRIBUTES)
			.name(ItemTier.COSMIC.makeName("Sword"))
			.unbreakable(true)
			.attribute("EXTRA_RANGE", Attribute.ENTITY_INTERACTION_RANGE, 4, EquipmentSlotGroup.MAINHAND, Operation.ADD_NUMBER)
			.attribute("NO_REAL_DAMAGE", Attribute.ATTACK_DAMAGE, 0, EquipmentSlotGroup.MAINHAND, Operation.MULTIPLY_SCALAR_1)
			.flag(ItemFlag.HIDE_UNBREAKABLE)
			.lore(new String[] {
					"<gradient:white:purple:white>The power of the cosmos.",
					"<red>Instantly kills anything it hits.",
					"",
					ItemTier.COSMIC.makeItemString("Weapon")
			})
			.build()
	);
	
	public CosmicSword() {
		super(ItemRegistry.getItemGroup("WEAPON_CATEGORY"), COSMIC_SWORD_ITEM, Items.MOLECULAR_CRAFTER_RECIPE_TYPE, null);
		
		MolecularWorkbenchRecipe.registerRecipe(new ItemStack[] {
				
		}, getItem());
		
		addItemHandler(new SwordSwingHandler() {
			@Override
			public boolean onPlayerArmSwing(PlayerArmSwingEvent e, Player player, @Nullable LivingEntity hit) {
				if(hit == null) return false;
				
				DamageType dType = ANOSF.ANOSFDamageType.COSMIC_DAMAGE.asDamageType();
				hit.damage(1, DamageSource.builder(dType).withCausingEntity(player).withDirectEntity(player).build());
				hit.setKiller(player);
				
				short cancel = 0;
				EntityEquipment i = hit.getEquipment();
				for(ItemStack item : i.getArmorContents()) {
					SlimefunItem sfItem = SlimefunItem.getByItem(item);
					if(sfItem instanceof ArmorSetPiece armorPiece) {
						if(armorPiece.getArmorSetId().getKey().contains("COSMIC")) {
							cancel += 1;
						}
					}
				}
				
				if(cancel < 4) {
					hit.setHealth(0);
				} else {
					player.sendMessage(FormatUtils.mm("<red>You cannot kill a player wearing cosmic armor!"));
				}
				return false;
			}
		});
		ItemRegistry.registerItem(this);
	}

}
