package dev.cworldstar.anosf.items.armor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.items.armor.powered.impl.PoweredHazmat;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.impl.armor.AbstractArmorSet;

public class Armors {
	public static void registerAll() {
		new BerserkerArmorSet();
		new NaturalArmorSet();
		new NyxcelliumArmorSet();
		new CosmicArmorSet();
		new StrangeArmorSet();
		new PoweredHazmat();
		// begin mundane material armor set
		new ChromeArmorSet();
		
		// black iron
		new MaterialArmorSet(
			"BLACK_IRON_ARMOR_SET", // Set ID
			new String[] {"BLACK_IRON_HELMET", "BLACK_IRON_CHESTPLATE", "BLACK_IRON_LEGGINGS", "BLACK_IRON_BOOTS"}, // ids
			new Material[] {
					Material.IRON_HELMET,
					Material.IRON_CHESTPLATE,
					Material.IRON_LEGGINGS,
					Material.IRON_BOOTS
			},
			new int[] {
					128,
					145,
					136,
					120
			},
			Map.of(Enchantment.UNBREAKING, 10, Enchantment.BLAST_PROTECTION, 20),
			new ArrayList<Map<Attribute, AttributeModifier[]>>(Arrays.asList(
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BIHELM"), 3, Operation.ADD_NUMBER)	
							}
					),
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BICHEST"), 6, Operation.ADD_NUMBER)	
							}
					),
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BILEG"), 5, Operation.ADD_NUMBER)	
							}
					),
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BIBOOT"), 2, Operation.ADD_NUMBER)	
							}
					)
			)),
			new String[] {
					"lore: todo",
			},
			new String[] {
					"<gradient:gray:dark_gray>Black Iron Helmet",
					"<gradient:gray:dark_gray>Black Iron Chestplate",
					"<gradient:gray:dark_gray>Black Iron Leggings",
					"<gradient:gray:dark_gray>Black Iron Boots",
			},
			new ItemStack[][] {
				new ItemStack[] {
						ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"),ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"),ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"),
						ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"), null, ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"),
						null, null, null
				},
				new ItemStack[] {
						ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"), null,ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"),
						ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"), ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"), ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"),
						ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"), ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"), ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT")
				},
				new ItemStack[] {
						ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"),ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"),ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"),
						ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"), null, ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"),
						ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"), null, ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT")
				},
				new ItemStack[] {
						null,null,null,
						ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"), null, ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"),
						ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT"), null, ItemRegistry.getRegistryItemAsItemStack("BLACK_IRON_INGOT")
				}
			}
		) 
		{
			@Override
			public Consumer<AbstractArmorSet> armorTick() {
				return DEFAULT_ARMOR_TICK;
			}			
		};
		
		// lead-chrome alloy
		new MaterialArmorSet(
			"LEAD_CHROME_ARMOR_SET", // Set ID
			new String[] {"LEAD_CHROME_HELMET", "LEAD_CHROME_CHESTPLATE", "LEAD_CHROME_LEGGINGS", "LEAD_CHROME_BOOTS"}, // ids
			new Material[] {
					Material.IRON_HELMET,
					Material.IRON_CHESTPLATE,
					Material.IRON_LEGGINGS,
					Material.IRON_BOOTS
			},
			new int[] {
					274,
					315,
					305,
					248
			},
			Map.of(Enchantment.UNBREAKING, 10, Enchantment.THORNS, 10),
			new ArrayList<Map<Attribute, AttributeModifier[]>>(Arrays.asList(
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("LCHELM"), 6, Operation.ADD_NUMBER)	
							}
					),
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BICHEST"), 10, Operation.ADD_NUMBER)	
							}
					),
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BILEG"), 7, Operation.ADD_NUMBER)	
							}
					),
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BIBOOT"), 5, Operation.ADD_NUMBER)	
							}
					)
			)),
			new String[] {
					"lore: todo",
			},
			new String[] {
					"<gradient:gray:dark_gray>Lead-Chrome Alloy Helmet",
					"<gradient:gray:dark_gray>Lead-Chrome Alloy Chestplate",
					"<gradient:gray:dark_gray>Lead-Chrome Alloy Leggings",
					"<gradient:gray:dark_gray>Lead-Chrome Alloy Boots",
			},
			new ItemStack[][] {
				new ItemStack[] {
						ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"),ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"),ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"),
						null, null, null
				},
				new ItemStack[] {
						ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"), null,ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"), ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"), ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"), ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"), ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE")
				},
				new ItemStack[] {
						ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"),ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"),ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE")
				},
				new ItemStack[] {
						null,null,null,
						ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("LEAD_CHROME_ALLOY_PLATE")
				}
			}
		) 
		{
			@Override
			public Consumer<AbstractArmorSet> armorTick() {
				return DEFAULT_ARMOR_TICK;
			}			
		};
	
		// heavy chrome alloy
		new MaterialArmorSet(
			"HEAVY_CHROME_ARMOR_SET", // Set ID
			new String[] {"HEAVY_CHROME_HELMET", "HEAVY_CHROME_CHESTPLATE", "HEAVY_CHROME_LEGGINGS", "HEAVY_CHROME_BOOTS"}, // ids
			new Material[] {
					Material.IRON_HELMET,
					Material.IRON_CHESTPLATE,
					Material.IRON_LEGGINGS,
					Material.IRON_BOOTS
			},
			new int[] {
					474,
					580,
					525,
					460
			},
			Map.of(Enchantment.PROJECTILE_PROTECTION, 15, Enchantment.UNBREAKING, 10, Enchantment.THORNS, 10),
			new ArrayList<Map<Attribute, AttributeModifier[]>>(Arrays.asList(
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("HCHELM"), 8, Operation.ADD_NUMBER)	
							}
					),
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BICHEST"), 12, Operation.ADD_NUMBER)	
							}
					),
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BILEG"), 10, Operation.ADD_NUMBER)	
							}
					),
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BIBOOT"), 7, Operation.ADD_NUMBER)	
							}
					)
			)),
			new String[] {
					"lore: todo",
			},
			new String[] {
					"<gradient:gray:dark_gray>Heavy Chrome Alloy Helmet",
					"<gradient:gray:dark_gray>Heavy Chrome Alloy Chestplate",
					"<gradient:gray:dark_gray>Heavy Chrome Alloy Leggings",
					"<gradient:gray:dark_gray>Heavy Chrome Alloy Boots",
			},
			new ItemStack[][] {
				new ItemStack[] {
						ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"),ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"),ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"),
						null, null, null
				},
				new ItemStack[] {
						ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"), null,ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"), ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"), ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"), ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"), ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE")
				},
				new ItemStack[] {
						ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"),ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"),ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE")
				},
				new ItemStack[] {
						null,null,null,
						ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("HEAVY_CHROME_ALLOY_PLATE")
				}
			}	
		) 
		{
			@Override
			public Consumer<AbstractArmorSet> armorTick() {
				return DEFAULT_ARMOR_TICK;
			}			
		};
		
		// blackened chrome alloy
		new MaterialArmorSet(
			"BLACKENED_CHROME_ARMOR_SET", // Set ID
			new String[] {"BLACKENED_CHROME_HELMET", "BLACKENED_CHROME_CHESTPLATE", "BLACKENED_CHROME_LEGGINGS", "BLACKENED_CHROME_BOOTS"}, // ids
			new Material[] {
					Material.NETHERITE_HELMET,
					Material.NETHERITE_CHESTPLATE,
					Material.NETHERITE_LEGGINGS,
					Material.NETHERITE_BOOTS
			},
			new int[] {
					764,
					826,
					795,
					750
			},
			Map.of(
					Enchantment.PROJECTILE_PROTECTION, 20, 
					Enchantment.BLAST_PROTECTION, 20, 
					Enchantment.FIRE_PROTECTION, 20,
					Enchantment.UNBREAKING, 10, 
					Enchantment.THORNS, 10
			),
			new ArrayList<Map<Attribute, AttributeModifier[]>>(Arrays.asList(
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BCHELM"), 9, Operation.ADD_NUMBER)	
							}
					),
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BCCHEST"), 13, Operation.ADD_NUMBER)	
							}
					),
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BCLEG"), 12, Operation.ADD_NUMBER)	
							}
					),
					Map.of(
							Attribute.ARMOR,
							new AttributeModifier[] {
									new AttributeModifier(ANOSF.key("BCBOOT"), 8, Operation.ADD_NUMBER)	
							}
					)
			)),
			new String[] {
					"<italic><gradient:gold:#694e06>\"This is... a little absurd, no?\"",
					"<italic><gradient:#694e06:gold>\"A non-radioactive alloy made of radioactive",
					"<italic><gradient:gold:#694e06>materials? How strange...\"",
					"",
					"<gradient:gold:#694e06><underlined>Blackened Chrome Set Abilities:", 
					" <gray>[<white>Shiny<gray>]: This armor is shiny!",
					" <gray>[<gold>Extremely Resistant<gray>]: This armor is resistant to all damage types, and",
					" <gray> the durability is greatly increased.",
					" <gray>[<red>Self-Healing<gray>]: This armor grants the wearer <red>Regeneration I<gray> and",
					" <green> Health Boost II<gray>."
			},
			new String[] {
					"<gradient:gold:#694e06>Blackened Chrome Alloy Helmet",
					"<gradient:gold:#694e06>Blackened Chrome Alloy Chestplate",
					"<gradient:gold:#694e06>Blackened Chrome Alloy Leggings",
					"<gradient:gold:#694e06>Blackened Chrome Alloy Boots",
			},
			new ItemStack[][] {
				new ItemStack[] {
						ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"),ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"),ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"),
						null, null, null
				},
				new ItemStack[] {
						ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"), null,ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"), ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"), ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"), ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"), ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE")
				},
				new ItemStack[] {
						ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"),ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"),ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE")
				},
				new ItemStack[] {
						null,null,null,
						ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"),
						ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE"), null, ItemRegistry.getRegistryItemAsItemStack("BLACKENED_CHROME_ALLOY_PLATE")
				}
			}
		) 
		{
			@Override
			public Consumer<AbstractArmorSet> armorTick() {
				return potionArmorTick(new PotionEffect(PotionEffectType.REGENERATION, PotionEffect.INFINITE_DURATION, 0), new PotionEffect(PotionEffectType.HEALTH_BOOST, PotionEffect.INFINITE_DURATION, 1));
			}
		};
		
	}
}
