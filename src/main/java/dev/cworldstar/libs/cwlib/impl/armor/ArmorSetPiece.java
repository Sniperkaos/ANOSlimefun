package dev.cworldstar.libs.cwlib.impl.armor;

import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.CustomDurabilityHandler;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.handlers.DurabilityLossHandler;
import dev.cworldstar.libs.cwlib.handlers.ItemMendHandler;
import dev.cworldstar.libs.cwlib.impl.PreventDisenchant;
import dev.cworldstar.libs.cwlib.utils.PDCHelper;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectiveArmor;

public class ArmorSetPiece extends SlimefunItem implements ProtectiveArmor, PreventDisenchant {
	
	private ProtectionType[] protTypes;
	private NamespacedKey armorSetId;
	
	private void setup(ItemStack i) {
		ItemMeta meta = i.getItemMeta();
		if(meta instanceof Damageable) {
			Damageable dmeta = (Damageable) meta;
			Optional<PersistentDataContainer> option = PDCHelper.getPDC(dmeta);
			if(option.isPresent()) {
				PersistentDataContainer container = option.get();
				container.set(AbstractSFAddon.key("MAX_DURABILITY"), PersistentDataType.INTEGER, (int) i.getType().getMaxDurability());
				container.set(AbstractSFAddon.key("DURABILITY"), PersistentDataType.INTEGER, (int) i.getType().getMaxDurability());
				i.setItemMeta(dmeta);
			}
		}
	}
	
	public ArmorSetPiece(
			NamespacedKey armorSetId, 
			ItemGroup itemGroup, 
			ItemStack item, 
			String id, 
			RecipeType recipeType, 
			ItemStack[] recipe
	) {
		super(itemGroup, new SlimefunItemStack(id, item), recipeType, recipe);
		this.armorSetId = armorSetId;
		addItemHandler(new DurabilityLossHandler() {
			@Override
			public boolean onDurabilityLoss(PlayerItemDamageEvent e, Player p, ItemStack item) {
				CustomDurabilityHandler.durabilityLossHandler(e, p, item);
				return false;
			}
		});
		addItemHandler(new ItemMendHandler() {
			@Override
			public boolean onItemMend(PlayerItemMendEvent e, Player p, ItemStack item) {
				CustomDurabilityHandler.durabilityGainHandler(e, p, item);
				return false;
			}
		});
		setup(this.getItem());
		ItemRegistry.registerItem(this);
	}

	public ArmorSetPiece(NamespacedKey armorSetId, ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ProtectionType[] protTypes) {
		this(armorSetId, itemGroup, item, id, recipeType, recipe);
		this.protTypes = protTypes;
	}
	
	public ArmorSetPiece(NamespacedKey armorSetId, ItemGroup itemGroup, ItemStack item, String id) {
		this(armorSetId, itemGroup, item, id, RecipeType.NULL, null);
	}
	
	public ArmorSetPiece(NamespacedKey armorSetId, ItemStack item, String id) {
		this(armorSetId, ItemRegistry.getItemGroup("armor_group"), item, id, RecipeType.NULL, null);
	}
	
	public <T extends ArmorSetPiece> T addProtectionType(Class<T> clazz, ProtectionType...types) {
		for(ProtectionType type : types) {
			protTypes[protTypes.length+1] = type;
		}
		return clazz.cast(this);
	}

	@Override
	public ProtectionType[] getProtectionTypes() {
		return protTypes;
	}

	@Override
	public boolean isFullSetRequired() {
		return true;
	}

	@Override
	public NamespacedKey getArmorSetId() {
		return armorSetId;
	}
	
	public AbstractArmorSet getArmorSet() {
		return AbstractArmorSet.getSet(armorSetId);
	}

	public void setArmorSetId(NamespacedKey setId) {
		armorSetId = setId;
	}
}
