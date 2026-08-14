package dev.cworldstar.anosf.items.armor.powered;

import java.util.function.Consumer;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.svoltz.SVCapacitor;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet;
import dev.cworldstar.libs.cwlib.impl.armor.ArmorSetPiece;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.NamespacedKey;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * PoweredArmor uses a different power system than
 * the original Slimefun version. This uses SV, which is
 * fundamentally different than J. SV is stored as a BigNumber,
 * meaning ludicrous power requirements are possible. 
 * 
 * @author cworldstar
 */
public class PoweredArmor extends AbstractArmorSet {
	
	@Override	
	public ProtectionType[] getProtectionTypes() {
		return new ProtectionType[] {
			ProtectionType.RADIATION
		};
	}
	
	protected static long getPower() {
		return 120000;
	}
	
	@AllArgsConstructor
	public static class ArmorSetList<T extends ArmorSetPiece> {
		private @Getter @NotNull T helmet;
		private @Getter @NotNull T chest;
		private @Getter @NotNull T legs;
		private @Getter @NotNull T boots;	
	}

	protected boolean hasPower(LivingEntity entity, NamespacedKey armorSet, long hasPower) {
		if(!(AbstractArmorSet.isWearing(entity, armorSet))) return false;
		long totalPower = 0;
		for(ItemStack piece : entity.getEquipment().getArmorContents()) {
			SlimefunItem item = SlimefunItem.getByItem(piece);
			if(item instanceof SVCapacitor setPiece) {
				totalPower += setPiece.getVoltz(piece).longValue();
				continue;
			}
		}
		return hasPower >= totalPower;
	}
	
	public PoweredArmor(ArmorSetList<PoweredArmorSetPiece> pieces) {
		super(ANOSF.key("POWERED_ARMOR_SET"));
		// helmet
		
		addPiece(pieces.getHelmet(), ArmorContext.HELMET);
		addPiece(pieces.getChest(), ArmorContext.CHEST);
		addPiece(pieces.getLegs(), ArmorContext.LEGS);
		addPiece(pieces.getBoots(), ArmorContext.BOOTS);
		
	}

	private void addPiece(PoweredArmorSetPiece armor, ArmorContext context) {
		if(armor.getContext() == null) {
			armor.setContext(context);
		}
		addPiece(armor);
	}

	@Override
	public Consumer<AbstractArmorSet> armorTick() {
		return null;
	}

}
