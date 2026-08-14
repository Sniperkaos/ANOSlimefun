package dev.cworldstar.anosf.items.materials;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.anosf.items.materials.chrome.ChromeDust;
import dev.cworldstar.anosf.items.materials.chrome.ChromeIngot;
import dev.cworldstar.anosf.items.recipes.AlloyForgeRecipe;
import dev.cworldstar.anosf.items.recipes.MolecularWorkbenchRecipe;
import dev.cworldstar.anosf.items.tools.Hammer;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.utils.SlimefunItemEntry;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
/**
 * Holds every material in this addon. It is loaded BEFORE everything else, and as such,
 * anything implementing these items should load AFTER. (e.g recipes)
 * @author cworldstar
 *
 */
public class MaterialsClass {
	
	public static void registerBossMaterials() {
		
	}
	
	
	@SuppressWarnings("unused")
	public static void registerAll() {
		
		// BASE RESOURCES
		
		ChromeDust CHROME_DUST = new ChromeDust();
		ChromeIngot CHROME_INGOT = new ChromeIngot();
		
		IngotMaterial BLACK_IRON = new IngotMaterial("BLACK_IRON", "<gradient:gray:dark_gray>Black Iron", ItemTier.SIMPLE, Items.HAMMER_RECIPE_TYPE, null)
			.generateDust(Material.GUNPOWDER, true);
		BLACK_IRON.generateIngot(RecipeType.SMELTERY, new ItemStack[] {
					BLACK_IRON.dust().asQuantity(8), SlimefunItems.MAGNESIUM_DUST.asQuantity(16), SlimefunItems.ZINC_DUST.asQuantity(16)	
			}, Material.BLACK_DYE, true)
			.generateBlock(Material.BLACK_CONCRETE, true)
		;
		
		Hammer.addDust(BLACK_IRON.dust());
		
		IngotMaterial LITHIUM = new IngotMaterial("LITHIUM", "<gradient:yellow:white>Lithium", ItemTier.SIMPLE, Items.HAMMER_RECIPE_TYPE, null)
				.generateDust(Material.SUGAR, true)
		;
		
		Particle BATTERY_CASING = new Particle("BATTERY_CASING", Material.RAW_IRON_BLOCK, ItemTier.SIMPLE,  "<yellow>Battery Casing", "Material", new String[0], RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null, SlimefunItems.DURALUMIN_INGOT.asOne(), SlimefunItems.ALUMINUM_INGOT.asOne(),
				null, SlimefunItems.ALUMINUM_INGOT.asOne(), SlimefunItems.DURALUMIN_INGOT.asOne(),
				null, SlimefunItems.DURALUMIN_INGOT.asOne(), SlimefunItems.ALUMINUM_INGOT.asOne()
		});
		
		// add recipe for batteries using lithium as an alternative to sulfur
		SlimefunItem.getByItem(SlimefunItems.BATTERY).setRecipe(new ItemStack[] {
				new ItemStack(Material.LIGHTNING_ROD), BATTERY_CASING.getItem(), new ItemStack(Material.LIGHTNING_ROD),
				BATTERY_CASING.getItem(), LITHIUM.dust(), BATTERY_CASING.getItem(),
				BATTERY_CASING.getItem(), LITHIUM.dust(), BATTERY_CASING.getItem()
		});
		
		Hammer.addDust(LITHIUM.dust());

		IngotMaterial PROMETHIUM = new IngotMaterial("PROMETHIUM", "<gradient:#BE5103:dark_gray>Promethium", ItemTier.ADVANCED, Items.HAMMER_RECIPE_TYPE, null)
				.generateDust(Material.SUGAR, true, Radioactivity.LOW)
				.generateIngot(RecipeType.SMELTERY, new ItemStack[] {
						ItemRegistry.getRegistryItem("PROMETHIUM_DUST").getItem(),
						LITHIUM.dust().asQuantity(8),
						BLACK_IRON.dust().asQuantity(16),
						SlimefunItems.SILVER_DUST.asQuantity(64)
				}, Material.NETHER_BRICK, Radioactivity.LOW, true)
		;
		
		Hammer.addDust(PROMETHIUM.dust());
		
		IngotMaterial NEODYNIUM = new IngotMaterial("NEODYNIUM", "<gradient:white:aqua>Neodynium", ItemTier.ADVANCED, Items.HAMMER_RECIPE_TYPE, null)
				.generateDust(Material.DISC_FRAGMENT_5, true)
		;
		
		Hammer.addDust(NEODYNIUM.dust());
		
		IngotMaterial HALYNIX = new IngotMaterial("HALYNIX", "<gradient:aqua:yellow:white>Halynix", ItemTier.ADVANCED, Items.ALLOY_FORGE_RECIPE_TYPE, null)
				.generateIngot(Items.ALLOY_FORGE_RECIPE_TYPE, null, Material.ARMADILLO_SCUTE, null, true)
				.generateWire(Material.STRING, true)
		;
		
		Particle HALYNIX_SOLENOID = new Particle("HALYNIX_SOLENOID", Material.BLAZE_ROD, ItemTier.ADVANCED,  "<gradient:aqua:yellow:white>Halynix Solenoid", "Material", new String[0], RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				HALYNIX.wire(), SlimefunItems.GOLD_24K.asOne(), HALYNIX.wire(),
				HALYNIX.wire(), SlimefunItems.GOLD_24K.asOne(), HALYNIX.wire(),
				HALYNIX.wire(), SlimefunItems.GOLD_24K.asOne(), HALYNIX.wire()
		});
		
		Particle NEODYNIUM_MAGNET = new Particle("NEODYNIUM_MAGNET", Material.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTE4YWExMzFjZmI1NWE5MjY4YzhhNWI0MGJiOTc3MjU5MWQ5YzYyYjA4ZjA0Y2QxNDFiYjJlZDY2MjUxNTZkZiJ9fX0=", ItemTier.ADVANCED,  "<gradient:white:aqua>Neodynium Magnet", "Material", new String[0], RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null, NEODYNIUM.dust(), null,
				NEODYNIUM.dust(),SlimefunItems.MAGNET.asOne(),NEODYNIUM.dust(),
				null,NEODYNIUM.dust(),null
		});
		
		Particle HALYNIX_CIRCUIT_BOARD = new Particle("HALYNIX_CIRCUIT_BOARD", Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTier.ADVANCED,  "<gradient:aqua:yellow:white>Halynix Circuit Board", "Material", new String[0], RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				HALYNIX.wire(), NEODYNIUM_MAGNET.getItem(), HALYNIX.wire(),
				HALYNIX.wire(), SlimefunItems.ADVANCED_CIRCUIT_BOARD.asOne(), HALYNIX.wire(),
				HALYNIX.wire(), HALYNIX_SOLENOID.getItem(), HALYNIX.wire()
		});
		

		
		//-- forge recipe
		AlloyForgeRecipe.makeRecipe("HALYNIX_ALLOY_FORGING", 240, HALYNIX.ingot(), new SlimefunItemEntry[] {
			SlimefunItemEntry.of(NEODYNIUM.getDust(), 2),
			SlimefunItemEntry.of(LITHIUM.getDust(), 4),
			SlimefunItemEntry.of(SlimefunItems.LEAD_DUST, 8),
			SlimefunItemEntry.of(SlimefunItems.ZINC_DUST, 12)
		});
		
		
		SlimefunItem IRON_ROD = new Particle("IRON_ROD", Material.BREEZE_ROD, ItemTier.SIMPLE, "<white>Steel Rod", "Material", new String[] {}, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
			null,SlimefunItems.STEEL_INGOT.asOne(),null,	
			null,SlimefunItems.STEEL_INGOT.asOne(),null,	
			null,SlimefunItems.STEEL_INGOT.asOne(),null,	
		});
		
		Particle CHROME_ROD = new Particle("CHROME_ROD", Material.BREEZE_ROD, ItemTier.SIMPLE, "<gradient:gray:white>Chrome Rod", "Material", new String[] {}, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null,ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),null,	
				null,ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),null,
				null,ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),null,
		});
		
		Particle CHROME_PLATE = new Particle("CHROME_PLATE", Material.PAPER, ItemTier.SIMPLE, "<gradient:gray:white>Chrome Plate", "Material", new String[] {}, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null,null,null,	
				ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),
				ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),ItemRegistry.getRegistryItem("CHROME_INGOT").getItem(),
		});
		
		// machine cores
		
		Particle BASIC_MACHINE_CORE = new Particle("MACHINE_CORE_BASIC", Material.GOLD_BLOCK, ItemTier.BASIC, ItemTier.BASIC.makeName("Machine Core"), "Material", new String[] {}, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				IRON_ROD.getItem(),SlimefunItems.STEEL_PLATE.asOne(),IRON_ROD.getItem(),	
				IRON_ROD.getItem(),CHROME_DUST.getItem(),IRON_ROD.getItem(),	
				IRON_ROD.getItem(),SlimefunItems.REINFORCED_PLATE.asOne(),IRON_ROD.getItem(),	
		});
		
		Particle ADVANCED_MACHINE_CORE = new Particle("MACHINE_CORE_ADVANCED", Material.STRIPPED_MANGROVE_WOOD, ItemTier.ADVANCED, ItemTier.ADVANCED.makeName("Machine Core"), "Material", new String[] {}, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				CHROME_PLATE.getItem(),HALYNIX_CIRCUIT_BOARD.getItem(),CHROME_PLATE.getItem(),	
				HALYNIX_SOLENOID.getItem(),BASIC_MACHINE_CORE.getItem(),HALYNIX_SOLENOID.getItem(),	
				CHROME_PLATE.getItem(),HALYNIX_CIRCUIT_BOARD.getItem(),CHROME_PLATE.getItem(),	
		});
		
		IngotMaterial LEAD_ALLOY = new IngotMaterial(
				"LEAD_CHROME_ALLOY", 
				ItemTier.ADVANCED, 
				Material.NETHERITE_INGOT,
				null,
				Material.BREEZE_ROD,
				Material.PAPER,
				"Lead-Chrome Alloy", 
				Items.ALLOY_FORGE_RECIPE_TYPE, 
				null,
				false
		);
		
		AlloyForgeRecipe.makeRecipe("LEAD_ALLOY_FORGING", 300, LEAD_ALLOY.ingot(), new SlimefunItemEntry[] {
				SlimefunItemEntry.of(CHROME_DUST, 4),
				SlimefunItemEntry.of(SlimefunItems.LEAD_DUST, 32),
		});
		
		IngotMaterial HEAVY_CHROME_ALLOY = new IngotMaterial(
				"HEAVY_CHROME_ALLOY", 
				ItemTier.ELITE, 
				Material.BLACK_DYE,
				null,
				Material.BREEZE_ROD,
				Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE,
				"Heavy Chrome Alloy", 
				Items.ALLOY_FORGE_RECIPE_TYPE, 
				new ItemStack[] {
						ItemRegistry.getRegistryItem("CHROME_INGOT").getItem().asQuantity(4), 
						LEAD_ALLOY.getIngot().getItem().asQuantity(16),
						SlimefunItems.PLUTONIUM.asQuantity(48)
				},
				false
		);
		
		AlloyForgeRecipe.makeRecipe("HEAVY_CHROME_ALLOY_FORGING", 500, HEAVY_CHROME_ALLOY.ingot(), new SlimefunItemEntry[] {
				SlimefunItemEntry.of(CHROME_INGOT, 4),
				SlimefunItemEntry.of(LEAD_ALLOY.getIngot(), 16),
				SlimefunItemEntry.of(NEODYNIUM_MAGNET, 8)
		});
		
		Particle ELITE_MACHINE_CORE = new Particle("MACHINE_CORE_ELITE", Material.STRIPPED_WARPED_HYPHAE, ItemTier.ELITE, ItemTier.ELITE.makeName("Machine Core"), "Material", new String[] {}, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				HEAVY_CHROME_ALLOY.getPlate().getItem(),LEAD_ALLOY.getItem(),HEAVY_CHROME_ALLOY.getPlate().getItem(),	
				LEAD_ALLOY.getItem(),ADVANCED_MACHINE_CORE.getItem(),LEAD_ALLOY.getItem(),	
				HEAVY_CHROME_ALLOY.getPlate().getItem(),LEAD_ALLOY.getItem(),HEAVY_CHROME_ALLOY.getPlate().getItem(),	
		});
		
		IngotMaterial BLACKENED_CHROME_ALLOY = new IngotMaterial(
				"BLACKENED_CHROME_ALLOY", 
				ItemTier.ELITE, 
				Material.NETHERITE_INGOT,
				null,
				Material.BLAZE_ROD,
				Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE,
				Material.BROWN_CONCRETE,
				"<gradient:gold:#694e06>Blackened Chrome Alloy", 
				Items.ALLOY_FORGE_RECIPE_TYPE, 
				null,
				new String[0],
				false
		);
		
		AlloyForgeRecipe.makeRecipe("BLACKENED_CHROME_ALLOY_SYNTHESIS", 1200, BLACKENED_CHROME_ALLOY.ingot(), new SlimefunItemEntry[] {
				SlimefunItemEntry.of(CHROME_INGOT.getItem(), 32),
				SlimefunItemEntry.of(PROMETHIUM.dust(), 16),
				SlimefunItemEntry.of(BLACK_IRON.dust(), 200),
				SlimefunItemEntry.of(SlimefunItem.getById("IE_COMPRESSED_COBBLESTONE_5"), 200),
				SlimefunItemEntry.of(SlimefunItems.PLUTONIUM, 128),
				SlimefunItemEntry.of(SlimefunItem.getById("IE_MAGSTEEL_PLATE"), 100),
				SlimefunItemEntry.of(HALYNIX.getIngot(), 8)
		});
		
		IngotMaterial NYXCELLIUM = new IngotMaterial(
				"NYXCELLIUM", 
				"<gradient:dark_green:gray:#964B00>Nyxcellium",
				new String[] {
						"placeholder lore"
				},
				ItemTier.ELITE,
				Items.ALLOY_FORGE_RECIPE_TYPE,
				null
			).generateIngot(Material.RESIN_BRICK, true);
			
		AlloyForgeRecipe.makeRecipe("NYXCELLIUM_SYNTHESIS", 6400, NYXCELLIUM.ingot().asQuantity(8), new SlimefunItemEntry[] {
				SlimefunItemEntry.of(BLACKENED_CHROME_ALLOY.getIngot(), 1),
				SlimefunItemEntry.of(Material.MYCELIUM, 9001),
				SlimefunItemEntry.of(HALYNIX.getIngot(), 8)
		});
		
		Particle PROMETHIUM_BATTERY = new Particle("PROMETHIUM_BATTERY", Material.PLAYER_HEAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I4YTk3ZmUyMjRhNjNlY2Y1ZWM3NWNmYzBjNzhlYTkwNWRhMzE4OGFjMjY2YTc1MGJlMGE2NWYyMmQzOGRmZCJ9fX0=",  ItemTier.ELITE, "<gradient:#BE5103:dark_gray>Promethium Battery", "Material", new String[0], RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null, PROMETHIUM.ingot(), null,
				PROMETHIUM.ingot(), SlimefunItems.BATTERY.asOne(), PROMETHIUM.ingot(),
				null, PROMETHIUM.ingot(), null
		});
		
		Particle HIGH_CIRCUIT = new Particle("HIGH_CIRCUIT", Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTier.HIGH, "High-Voltage Circuit", "Material", new String[] {}, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				BLACKENED_CHROME_ALLOY.getPlate().getItem(),HALYNIX_CIRCUIT_BOARD.getItem(),BLACKENED_CHROME_ALLOY.getPlate().getItem(),	
				HALYNIX_CIRCUIT_BOARD.getItem(), PROMETHIUM_BATTERY.getItem(), HALYNIX_CIRCUIT_BOARD.getItem(),	
				BLACKENED_CHROME_ALLOY.getPlate().getItem(),HALYNIX_CIRCUIT_BOARD.getItem(),BLACKENED_CHROME_ALLOY.getPlate().getItem(),	
		});
		
		Particle HIGH_MACHINE_CORE = new Particle("MACHINE_CORE_HIGH", Material.CRACKED_NETHER_BRICKS, ItemTier.HIGH, ItemTier.HIGH.makeName("Machine Core"), "Material", new String[] {}, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				BLACKENED_CHROME_ALLOY.getPlate().getItem(),BLACKENED_CHROME_ALLOY.getRod().getItem(),BLACKENED_CHROME_ALLOY.getPlate().getItem(),	
				HIGH_CIRCUIT.getItem(),ELITE_MACHINE_CORE.getItem(),HIGH_CIRCUIT.getItem(),	
				BLACKENED_CHROME_ALLOY.getPlate().getItem(),BLACKENED_CHROME_ALLOY.getRod().getItem(),BLACKENED_CHROME_ALLOY.getPlate().getItem(),	
		});
		
		Particle FABRIC_OF_REALITY = new Particle("FABRIC_OF_REALITY", Material.DISC_FRAGMENT_5, ItemTier.STRANGE, ItemTier.STRANGE.makeName("Reality Fracture"), "Material", new String[] {}, Items.BOSS_DROP_RECIPE_TYPE, null);

		IngotMaterial ANOIUM_ALLOY = new IngotMaterial(
				"ANOIUM_ALLOY", 
				ItemTier.HIGH, 
				Material.NETHERITE_INGOT,
				null,
				Material.BLAZE_ROD,
				Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE,
				Material.BLACK_CONCRETE,
				"<gradient:green:white>Anoium Alloy", 
				Items.ALLOY_FORGE_RECIPE_TYPE, 
				null,
				new String[] {
						"",
						"<gradient:dark_green:green:gray><italic>\"Singularities? How could a mere singularity hold my",
						"<gradient:gray:green:dark_green><italic>vast ambition?\""
				},
				false
		);
		
		AlloyForgeRecipe.makeRecipe("ANOIUM_ALLOY_SYNTHESIS", 12000, ANOIUM_ALLOY.ingot().asQuantity(8), new SlimefunItemEntry[] {
				SlimefunItemEntry.of(BLACKENED_CHROME_ALLOY.getBlock(), 128),
				SlimefunItemEntry.of(SlimefunItem.getById("IE_INFINITY_SINGULARITY"), 128),
				SlimefunItemEntry.of(FABRIC_OF_REALITY, 8)
		});
		
		HBParticle hbParticle = new HBParticle();
		WhiteParticle whiteParticle = new WhiteParticle();
		FallenSpark fallenSpark = new FallenSpark();
		CosmicParticle cosmicParticle = new CosmicParticle();
		
		IngotMaterial STRANGE_ALLOY = new IngotMaterial(
				"STRANGE_ALLOY", 
				ItemTier.STRANGE, 
				Material.PURPLE_DYE,
				null,
				Material.PURPLE_CANDLE,
				Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,
				Material.PURPLE_CONCRETE,
				ItemTier.STRANGE.makeName("Alloy"), 
				Items.ALLOY_FORGE_RECIPE_TYPE, 
				null,
				new String[] {
						"",
						"<gradient:dark_purple:gray><italic>\"Now we're forging blood?",
						"<gradient:gray:dark_purple><italic>Some freaky shit, man.\""
				},
				false
		);
		
		new AlloyForgeRecipe("STRANGE_ALLOY_RECIPE", 128000, STRANGE_ALLOY.ingot(), new SlimefunItemEntry[] {
				SlimefunItemEntry.of(fallenSpark, 1)
		});
		
		Particle STRANGE_MACHINE_CORE = new Particle("MACHINE_CORE_STRANGE", Material.PURPLE_GLAZED_TERRACOTTA, ItemTier.STRANGE, ItemTier.STRANGE.makeName("Machine Core"), "Material", new String[] {}, Items.MOLECULAR_CRAFTER_RECIPE_TYPE, null);
		MolecularWorkbenchRecipe.registerRecipe(new ItemStack[] {
				null,null,null,null,null,null,null,
				STRANGE_ALLOY.plate(),STRANGE_ALLOY.plate(),STRANGE_ALLOY.plate(),STRANGE_ALLOY.plate(),STRANGE_ALLOY.plate(),STRANGE_ALLOY.plate(),STRANGE_ALLOY.plate(),
				ANOIUM_ALLOY.rod(),HIGH_MACHINE_CORE.getItem(),ANOIUM_ALLOY.block(),HIGH_MACHINE_CORE.getItem(),ANOIUM_ALLOY.block(),HIGH_MACHINE_CORE.getItem(),ANOIUM_ALLOY.rod(),
				STRANGE_ALLOY.plate(),STRANGE_ALLOY.plate(),STRANGE_ALLOY.plate(),STRANGE_ALLOY.plate(),STRANGE_ALLOY.plate(),STRANGE_ALLOY.plate(),STRANGE_ALLOY.plate(),
		}, STRANGE_MACHINE_CORE.getItem());
		
		IngotMaterial COSMIC_ALLOY = new IngotMaterial(
				"COSMIC_ALLOY", 
				ItemTier.COSMIC, 
				Material.WHITE_DYE,
				null,
				Material.WHITE_CANDLE,
				Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE,
				Material.WHITE_CONCRETE,
				ItemTier.COSMIC.makeName("Alloy"), 
				Items.ALLOY_FORGE_RECIPE_TYPE,
				null,
				new String[] {
						"",
						ItemTier.COSMIC.gradient("<italic>\"placeholder\"")
				},
				false
		);

		
	}
}
