package dev.cworldstar.anosf.items.armor.powered.impl;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.impl.radiation.ExtendedRadiationInfo.RadiationSeverity;
import dev.cworldstar.anosf.impl.radiation.ExtendedRadiationInfo.RadiationType;
import dev.cworldstar.anosf.impl.radiation.RadiationProtector;
import dev.cworldstar.anosf.items.armor.powered.PoweredArmor;
import dev.cworldstar.anosf.items.armor.powered.PoweredArmorSetPiece;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.LeatherArmorBuilder;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;

public class PoweredHazmat extends PoweredArmor implements RadiationProtector {

	private static final ItemStack POWERED_HAZMAT_HELMET = new LeatherArmorBuilder(Material.LEATHER_HELMET)
		.color(Color.BLACK)
		.armor(20)
		.name("<gradient:red:orange:yellow>Powered Hazmat Helmet")
		.desc(new String[] {
				"<italic><gradient:orange:yellow>Radiation... The subsonic vibrations that",
				"<italic><gradient:orange:yellow>make up what is good and evil... none shall reach me.",
				"",
				"<yellow><underlined>Powered Hazmat Set Abilities:", 
				" <gray>[<gray>Hazmat<gray>]: - <gray>While this set is active, you are immune",
				" <gray>to <lime>radiation<gray> damage.",
				" <gray>[<yellow>Powered<gray>]: - <gray>This set is powered, allowing you to apply",
				" <gray>special upgrades which cost <yellow>SV<gray> to use."
		})
		.unbreakable()
		.build();
	
	private static final ItemStack POWERED_HAZMAT_CHEST = new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE)
			.color(Color.YELLOW)
			.armor(20)
			.name("<gradient:red:orange:yellow>Powered Hazmat Suit")
			.desc(new String[] {
					"<italic><gradient:orange:yellow>Radiation... The subsonic vibrations that",
					"<italic><gradient:orange:yellow>make up what is good and evil... none shall reach me.",
					"",
					"<yellow><underlined>Powered Hazmat Set Abilities:", 
					" <gray>[<gray>Hazmat<gray>]: - <gray>While this set is active, you are immune",
					" <gray>to <lime>radiation<gray> damage.",
					" <gray>[<yellow>Powered<gray>]: - <gray>This set is powered, allowing you to apply",
					" <gray>special upgrades which cost <yellow>SV<gray> to use."
			})
			.unbreakable()
			.build();
	
	private static final ItemStack POWERED_HAZMAT_LEGS = new LeatherArmorBuilder(Material.LEATHER_LEGGINGS)
			.color(Color.YELLOW)
			.armor(20)
			.name("<gradient:red:orange:yellow>Powered Hazmat Legs")
			.desc(new String[] {
					"<italic><gradient:orange:yellow>Radiation... The subsonic vibrations that",
					"<italic><gradient:orange:yellow>make up what is good and evil... none shall reach me.",
					"",
					"<yellow><underlined>Powered Hazmat Set Abilities:", 
					" <gray>[<gray>Hazmat<gray>]: - <gray>While this set is active, you are immune",
					" <gray>to <lime>radiation<gray> damage.",
					" <gray>[<yellow>Powered<gray>]: - <gray>This set is powered, allowing you to apply",
					" <gray>special upgrades which cost <yellow>SV<gray> to use."
			})
			.unbreakable()
			.build();
	
	private static final ItemStack POWERED_HAZMAT_BOOTS = new LeatherArmorBuilder(Material.LEATHER_BOOTS)
			.color(Color.BLACK)
			.armor(20)
			.name("<gradient:red:orange:yellow>Powered Hazmat Boots")
			.desc(new String[] {
					"<italic><gradient:orange:yellow>Radiation... The subsonic vibrations that",
					"<italic><gradient:orange:yellow>make up what is good and evil... none shall reach me.",
					"",
					"<yellow><underlined>Powered Hazmat Set Abilities:", 
					" <gray>[<gray>Hazmat<gray>]: - <gray>While this set is active, you are immune",
					" <gray>to <lime>radiation<gray> damage.",
					" <gray>[<yellow>Powered<gray>]: - <gray>This set is powered, allowing you to apply",
					" <gray>special upgrades which cost <yellow>SV<gray> to use."
			})
			.unbreakable()
			.build();
	
	public static class PoweredHazmatSetPiece extends PoweredArmorSetPiece {
		public PoweredHazmatSetPiece(ItemStack item, ItemGroup itemGroup, String itemId) {
			super(ANOSF.key("POWERED_HAZMAT_ITEM_SET"), itemGroup, item, itemId);
		}
	}
	
	public PoweredHazmat() {
		super(new ArmorSetList<PoweredArmorSetPiece>(
			new PoweredHazmatSetPiece(POWERED_HAZMAT_HELMET, ItemRegistry.getItemGroup("ARMORS"), "POWERED_HAZMAT_HELMET"),
			new PoweredHazmatSetPiece(POWERED_HAZMAT_CHEST, ItemRegistry.getItemGroup("ARMORS"), "POWERED_HAZMAT_CHEST"),
			new PoweredHazmatSetPiece(POWERED_HAZMAT_LEGS, ItemRegistry.getItemGroup("ARMORS"), "POWERED_HAZMAT_LEGS"),
			new PoweredHazmatSetPiece(POWERED_HAZMAT_BOOTS, ItemRegistry.getItemGroup("ARMORS"), "POWERED_HAZMAT_BOOTS")
		));
	}

	@Override
	public RadiationType[] getProtections() {
		return new RadiationType[] {
			RadiationType.NEUTRON_RADIATION
		};
	}

	@Override
	public RadiationSeverity getMaxSeverity() {
		return RadiationSeverity.CERTAIN_DEATH;
	}

	@Override
	public int getProtectionValue() {
		return 8;
	}
}
