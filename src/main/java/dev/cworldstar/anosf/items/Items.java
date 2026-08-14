package dev.cworldstar.anosf.items;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.entities.enemies.DeathKnight;
import dev.cworldstar.anosf.entities.enemies.EliteArcher;
import dev.cworldstar.anosf.entities.enemies.SpiderMother;
import dev.cworldstar.anosf.impl.drugs.drugs.Sativa;
import dev.cworldstar.anosf.impl.drugs.drugs.TestDrug;
import dev.cworldstar.anosf.impl.drugs.items.Narcan;
import dev.cworldstar.anosf.impl.recipetype.AcceleratorRecipeType;
import dev.cworldstar.anosf.impl.recipetype.AlloyForgeRecipeType;
import dev.cworldstar.anosf.impl.recipetype.FluidRecipeType;
import dev.cworldstar.anosf.impl.recipetype.MolecularWorkbenchRecipeType;
import dev.cworldstar.anosf.impl.runes.BouncyRune;
import dev.cworldstar.anosf.impl.runes.NightVisionRune;
import dev.cworldstar.anosf.impl.runes.SpeedRune;
import dev.cworldstar.anosf.items.armor.Armors;
import dev.cworldstar.anosf.items.explosives.Explosives;
import dev.cworldstar.anosf.items.machines.CreativeGenerator;
import dev.cworldstar.anosf.items.machines.generators.TieredDecayGenerators;
import dev.cworldstar.anosf.items.machines.multiblocks.AlloyForgeMultiblock;
import dev.cworldstar.anosf.items.machines.multiblocks.CosmicForgeMultiblock;
import dev.cworldstar.anosf.items.machines.multiblocks.DrugWorkbenchMultiblock;
import dev.cworldstar.anosf.items.machines.multiblocks.MolecularCrafterMultiblock;
import dev.cworldstar.anosf.items.machines.multiblocks.ParticleAcceleratorMultiblock;
import dev.cworldstar.anosf.items.machines.tiered.TieredDustExtractors;
import dev.cworldstar.anosf.items.machines.tiered.TieredIngotFormers;
import dev.cworldstar.anosf.items.materials.MaterialsClass;
import dev.cworldstar.anosf.items.recipes.ParticleAcceleratorRecipe;
import dev.cworldstar.anosf.items.recipes.DrugWorkbenchRecipe;
import dev.cworldstar.anosf.items.recipes.MolecularWorkbenchRecipe;
import dev.cworldstar.anosf.items.tools.ElectricHammer;
import dev.cworldstar.anosf.items.tools.Hammer;
import dev.cworldstar.anosf.items.weapons.ChromeSword;
import dev.cworldstar.anosf.items.weapons.CosmicSword;
import dev.cworldstar.anosf.items.weapons.LevitationBow;
import dev.cworldstar.anosf.items.weapons.PoisonBow;
import dev.cworldstar.anosf.items.weapons.Weapons;
import dev.cworldstar.anosf.items.weapons.WitherBow;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemGroupBuilder;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.groups.InvisibleGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;

public class Items {
	
	public static enum ItemTier {
			SIMPLE("<white>", "Simple", 1, true),
			BASIC("<blue>", "Basic", 1),
			ADVANCED("<yellow>", "Advanced", 2),
			ELITE("<aqua>", "Elite", 3),
			HIGH("<gradient:red:gold>", "High", 4),
			STRANGE("<gradient:dark_purple:gray>", "Strange", 5),
			COSMIC("<gradient:gold:#f7c662:#fadb9d>", "Cosmic", 6),
			CREATIVE("<gradient:light_purple:dark_purple>", "Creative", 7, true);

			@Getter
			private String identifier;
			@Getter
			private String color;
			@Getter
			private int tier;
			@Getter
			private boolean blacklisted = false;
			
			ItemTier(String gradient, String string, int tier) {
				color = gradient;
				this.tier = tier;
				this.identifier = string;
			}
			
			ItemTier(String gradient, String string, int tier, boolean blacklisted) {
				this(gradient, string, tier);
				this.blacklisted = blacklisted;
			}
			
			public String gradient(String text) {
				return color + text;
			}
			
			public String identifier() {
				return color + identifier;
			}
			
			public String makeName(String name) {
				return color + identifier + " " + name;
			}
			
			public String makeItemString(String typeOfItem) {
				return "<gray>«</gray> " + color + identifier + " Grade<gray> " + typeOfItem + " <gray>»";
			}
			
			public static String makeItemString(ItemTier tier, String typeOfItem) {
				return tier.makeItemString(typeOfItem);
			}
			
			public static List<ItemTier> iterator() {
				return Arrays.asList(values()).stream().filter(item -> !item.isBlacklisted()).sorted((t1, t2) -> t1.compareTo(t2)).collect(Collectors.toList());
			}
	}
	
	public static final InvisibleGroup DRUG_CATEGORY = new ItemGroupBuilder()
			.setID("DRUG_CATEGORY")
			.setItem(
					new ItemStackBuilder(Material.SUGAR)
						.setName("<gray>Drugs</gray>")
						.setLore(new String[] {
								"",
								"<gradient:gray:white>Contains the drugs in this addon.</gradient>",
								"",
								"<bold><yellow>Warning!</yellow></bold> <gradient:yellow:green:dark_green>May be too dank.</gradient>"
						})
						.get()
			).build();
	
	public static final InvisibleGroup ARMOR_CATEGORY = new ItemGroupBuilder()
			.setID("ARMOR_CATEGORY")
			.setItem(
					new ItemStackBuilder(Material.NETHERITE_CHESTPLATE)
						.setName("<gray>Armor</gray>")
						.setLore(new String[] {
								"",
								"<gradient:gray:#524e05>Contains the armor in this addon.</gradient>",
						})
						.get()
			).build();
	
	public static final InvisibleGroup MATERIAL_CATEGORY = new ItemGroupBuilder()
			.setID("MATERIAL_CATEGORY")
			.setItem(
					new ItemStackBuilder(Material.NETHERITE_INGOT)
						.setName("<gray>Material</gray>")
						.setLore(new String[] {
								"",
								"<gradient:gray:#524e05>Contains the materials in this addon.</gradient>",
						})
						.get()
			).build();
	
	public static final InvisibleGroup MACHINE_CATEGORY = new ItemGroupBuilder()
			.setID("MACHINE_CATEGORY")
			.setItem(
					new ItemStackBuilder(Material.REDSTONE_LAMP)
						.setName("<gray>Machines</gray>")
						.setLore(new String[] {
								"",
								"<gradient:gray:#524e05>Contains the machines in this addon.</gradient>",
						})
						.get()
			).build();
	
	public static final InvisibleGroup TOOL_CATEGORY = new ItemGroupBuilder()
			.setID("TOOL_CATEGORY")
			.setItem(
					new ItemStackBuilder(Material.NETHERITE_PICKAXE)
						.setName("<gray>Tools</gray>")
						.setLore(new String[] {
								"",
								"<gradient:gray:#524e05>Contains the tools in this addon.</gradient>",
						})
						.get()
			).build();
	
	public static final InvisibleGroup WEAPON_CATEGORY = new ItemGroupBuilder()
			.setID("WEAPON_CATEGORY")
			.setItem(
					new ItemStackBuilder(Material.NETHERITE_SWORD)
						.setName("<gray>Weapons</gray>")
						.setLore(new String[] {
								"",
								"<gradient:gray:#524e05>Contains the weapons in this addon.</gradient>",
						})
						.get()
			).build();
	
	public static final InvisibleGroup EXPLOSIVES_CATEGORY = new ItemGroupBuilder()
			.setID("EXPLOSIVES_CATEGORY")
			.setItem(
					new ItemStackBuilder(Material.TNT)
						.setName("<gray>Explosives</gray>")
						.setLore(new String[] {
								"",
								"<gradient:gray:#524e05>Contains the explosives in this addon.</gradient>",
						})
						.get()
			).build();
	
	public static final RecipeType ACCELERATOR_RECIPE_TYPE = new AcceleratorRecipeType(ANOSF.key("ACCELERATOR"), new ItemStackBuilder(Material.AMETHYST_BLOCK).name("<light_purple>Particle Accelerator").item());
	public static final RecipeType ALLOY_FORGE_RECIPE_TYPE = new AlloyForgeRecipeType(ANOSF.key("ALLOY_FORGE"), new ItemStackBuilder(Material.FURNACE).name("<gold>Alloy Furnace").item());
	public static final RecipeType MOLECULAR_CRAFTER_RECIPE_TYPE = new MolecularWorkbenchRecipeType(ANOSF.key("MOLECULAR_CRAFTER"), new ItemStackBuilder(Material.CRAFTER).name("<red>Molecular Crafter").item());
	public static final RecipeType FLUID_RECIPE_TYPE = new FluidRecipeType(ANOSF.key("FLUID"), new ItemStack(Material.BUCKET));
	public static final RecipeType BOSS_DROP_RECIPE_TYPE = new RecipeType(ANOSF.key("BOSS_DROP"), new ItemStackBuilder(Material.ZOMBIE_HEAD).name("<red>Boss Drop").item());
	public static final RecipeType HAMMER_RECIPE_TYPE = new RecipeType(ANOSF.key("HAMMER"), new ItemStackBuilder(Material.IRON_PICKAXE).name("<red>Hammer Drop").item());

	public static void toRegistry() {
		ItemRegistry.registerItemGroup(DRUG_CATEGORY);
		ItemRegistry.registerItemGroup(ARMOR_CATEGORY);
		ItemRegistry.registerItemGroup(MATERIAL_CATEGORY);
		ItemRegistry.registerItemGroup(MACHINE_CATEGORY);
		ItemRegistry.registerItemGroup(TOOL_CATEGORY);
		ItemRegistry.registerItemGroup(WEAPON_CATEGORY);
		ItemRegistry.registerItemGroup(EXPLOSIVES_CATEGORY);

		
		// drugs
		ItemRegistry.registerItem(new TestDrug());	
		ItemRegistry.registerItem(new Sativa());
		ItemRegistry.registerItem(new Narcan());
		
		MolecularWorkbenchRecipe.registerDeviatedItem();
		
		//creative items
		new CreativeGenerator();
		
		MaterialsClass.registerAll();
		
		//tools
		new Hammer();
		new ElectricHammer();
		
		SlimefunItem hbParticle = ItemRegistry.getRegistryItem("HBPARTICLE");
		SlimefunItem whiteParticle = ItemRegistry.getRegistryItem("WHITE_PARTICLE");
		SlimefunItem fallenSpark = ItemRegistry.getRegistryItem("FALLEN_SPARK_PARTICLE");
		SlimefunItem cosmicParticle = ItemRegistry.getRegistryItem("COSMIC_PARTICLE");

		// machines
		TieredDustExtractors.register();
		TieredIngotFormers.register();
		
		// magical items
		new BouncyRune();
		new NightVisionRune();
		new SpeedRune();
		
		// multiblocks
		new ParticleAcceleratorMultiblock();
		
		new DrugWorkbenchMultiblock();
		 
		new MolecularCrafterMultiblock();

		new CosmicForgeMultiblock();
		
		new AlloyForgeMultiblock();
		
		// recipes
		new ParticleAcceleratorRecipe(
				ItemRegistry.getRegistryItem("ANOIUM_ALLOY_INGOT").getItem(), 
				hbParticle.getItem(), 
				1080,
				"HB_PARTICLE_SYNTHESIS"
		);
		
		new ParticleAcceleratorRecipe(
			ItemRegistry.getRegistryItem("ANOIUM_ALLOY_INGOT").getItem(),
			whiteParticle.getItem(),
			hbParticle.getItem(),
			10800,
			"WHITE_PARTICLE_SYNTHESIS"
		);
		
		new ParticleAcceleratorRecipe(
				fallenSpark.getItem(),
				cosmicParticle.getItem(),
				whiteParticle.getItem(),
				54000,
				"COSMIC_PARTICLE_SYNTHESIS"
		);
		
		new DrugWorkbenchRecipe(
				"SATIVA_CRAFTING",
				250,
				ItemRegistry.getRegistryItem("SATIVA").getItem(),
				new ItemStack[0]
		);
		
		//generators
		TieredDecayGenerators.register();
		
		
		
		new SpawnEgg("SPIDER_MOTHER", SpiderMother.class.getCanonicalName());
		new SpawnEgg("ELITE_ARCHER", EliteArcher.class.getCanonicalName());
		new SpawnEgg("DEATH_KNIGHT", DeathKnight.class.getCanonicalName());

		
		// weapons
		new CosmicSword();
		new ChromeSword();
		new LevitationBow();
		new PoisonBow();
		new WitherBow();
		
		Weapons.registerAll();
		Explosives.registerAll();
		Armors.registerAll();
	}
}
