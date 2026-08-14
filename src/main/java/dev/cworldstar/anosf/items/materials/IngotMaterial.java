package dev.cworldstar.anosf.items.materials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.implementation.items.RadioactiveItem;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import lombok.Getter;

public class IngotMaterial {
	
	@Getter
	private SlimefunItem ingot;
	@Getter
	private SlimefunItem dust;
	@Getter
	private SlimefunItem rod;
	@Getter
	private SlimefunItem plate;
	@Getter
	private SlimefunItem block;
	@Getter
	private SlimefunItem wire;
	
	private String[] lore = new String[0];
	private String id;
	private ItemTier tier;
	private RecipeType type = RecipeType.NULL;
	private ItemStack[] recipe;
	private String name;
	
	public IngotMaterial(
			String id,
			String name,
			ItemTier tier,
			RecipeType type,
			ItemStack[] recipe
	) {
		this.id = id;
		this.tier = tier;
		this.type = type;
		this.recipe = recipe;
		this.name = name;
	}
	
	public IngotMaterial(
			String id,
			String name,
			String[] lore,
			ItemTier tier,
			RecipeType type,
			ItemStack[] recipe
	) {
		this.id = id;
		this.tier = tier;
		this.type = type;
		this.recipe = recipe;
		this.name = name;
		this.lore = lore;
	}
	
	private static ItemStack makeItem(Material material, ItemTier tier, String prefix, String type, String[] lore, boolean glowing) {
		List<String> loreList = new ArrayList<String>();
		loreList.addAll(Arrays.asList(lore));
		loreList.add("");
		loreList.add(tier.makeItemString(type));
		return new ItemStackBuilder(material)
			.setName(prefix)
			.setLore(loreList.stream().toArray(String[]::new))
			.condition(glowing, builder -> builder.glowing())
			.get();
	}
	
	public IngotMaterial generateDust(Material dustMaterial, boolean b) {
		dust = new Particle(id+"_DUST", dustMaterial, b, tier, name + " Dust", "Material", lore, Items.HAMMER_RECIPE_TYPE, null);
		return this;
	}
	
	public IngotMaterial generateDust(Material dustMaterial, boolean b, String[] lore) {
		dust = new Particle(id+"_DUST", dustMaterial, b, tier, name + " Dust", "Material", lore, RecipeType.NULL, null);
		return this;
	}
	
	public IngotMaterial generateDust(Material dustMaterial, boolean glowing, Radioactivity radioactive) {
		if(radioactive != null) {
			dust = new RadioactiveItem(ItemRegistry.getItemGroup("MATERIAL_CATEGORY"), radioactive, new SlimefunItemStack(id + "_DUST", makeItem(dustMaterial, tier, name + " Dust", "Material", new String[] {
				"",
				LoreBuilder.radioactive(radioactive),
				LoreBuilder.HAZMAT_SUIT_REQUIRED
			}, glowing)), type, recipe);
			ItemRegistry.registerItem(dust);
		} else {
			generateDust(dustMaterial, glowing);
		}
		return this;
	}
	
	public IngotMaterial generateRod(Material rodMaterial) {
		rod = new Particle(id+"_ROD", rodMaterial, tier, name + " Rod", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null,ingot.getItem(), null,
				null, ingot.getItem(), null,
				null, ingot.getItem(), null
		});		
		return this;
	}
	
	public IngotMaterial generateRod(Material rodMaterial, boolean glowing, Radioactivity radioactive) {
		if(radioactive != null) {
			rod = new RadioactiveItem(ItemRegistry.getItemGroup("MATERIAL_CATEGORY"), radioactive, new SlimefunItemStack(id + "_ROD", makeItem(rodMaterial, tier, name + " Rod", "Material", lore, glowing)), RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
					null,ingot.getItem(), null,
					null, ingot.getItem(), null,
					null, ingot.getItem(), null
			});
			ItemRegistry.registerItem(rod);
		} else {
			generateRod(rodMaterial);
		}
		return this;
	}
	
	public IngotMaterial generatePlate(Material plateMaterial) {
		if(rod == null) {
			generateRod(Material.BLAZE_ROD);
		}
		plate = new Particle(id+"_PLATE", plateMaterial, tier, name + " Plate", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null, null, null,
				ingot.getItem(), rod.getItem(), ingot.getItem(),
				ingot.getItem(), rod.getItem(), ingot.getItem()
		});
		return this;
	}
	
	
	public IngotMaterial generateIngot(Material ingotMaterial, boolean b) {
		ingot = new Particle(id+"_INGOT", ingotMaterial, b, tier, name, "Material", lore, type, recipe);
		return this;
	}
	
	public IngotMaterial generateIngot(Material ingotMaterial, RecipeType recipeType, boolean b) {
		ingot = new Particle(id+"_INGOT", ingotMaterial, b, tier, name, "Material", lore, recipeType, recipe);
		return this;
	}
	
	public IngotMaterial generateIngot(RecipeType recipeType, ItemStack[] recipe, Material ingotMaterial, boolean b) {
		ingot = new Particle(id+"_INGOT", ingotMaterial, b, tier, name, "Material", lore, recipeType, recipe);
		return this;
	}

	public IngotMaterial generateIngot(RecipeType type, ItemStack[] recipe, Material ingotMaterial, Radioactivity radioactive, boolean glowing) {
		if(radioactive != null) {
			List<String> lore = new ArrayList<>(Arrays.asList(this.lore));
			lore.add("");
			lore.add(LoreBuilder.radioactive(radioactive));
			lore.add(LoreBuilder.HAZMAT_SUIT_REQUIRED);
			ingot = new RadioactiveItem(ItemRegistry.getItemGroup("MATERIAL_CATEGORY"), radioactive, new SlimefunItemStack(id + "_INGOT", makeItem(ingotMaterial, tier, name + " Ingot", "Material", lore.toArray(new String[0]), glowing)), type, recipe);
			ItemRegistry.registerItem(ingot);
		} else {
			generateIngot(type, recipe, ingotMaterial, glowing);
		}
		return this;
	}
	
	public IngotMaterial generateBlock(Material blockMaterial, boolean b) {
		block = new Particle(id+"_BLOCK", blockMaterial, tier, name + " Block", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				ingot.getItem(), ingot.getItem(), ingot.getItem(),
				ingot.getItem(), ingot.getItem(), ingot.getItem(),
				ingot.getItem(), ingot.getItem(), ingot.getItem()
		});		return this;
	}

	public IngotMaterial generateWire(Material wireMaterial, boolean b) {
		wire = new Particle(id+"_WIRE", wireMaterial, b, tier, name + " Wire", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null, null, null,
				ingot.getItem(), ingot.getItem(), ingot.getItem(),
				null, null, null
		});
		return this;
	}
	
	public IngotMaterial(
			String id,
			ItemTier tier,
			Material ingotMaterial,
			Material dustMaterial,
			Material rodMaterial,
			Material plateMaterial,
			String materialName, 
			RecipeType recipeType,
			ItemStack[] recipe,
			boolean generateDust
	) {
		//ingot
		ingot = new Particle(id+"_INGOT", ingotMaterial, tier, materialName, "Material", lore, recipeType, recipe);
		if(generateDust) {
			dust = new Particle(id+"_DUST", dustMaterial, tier, materialName + " Dust", "Material", lore, RecipeType.NULL, null);
		}
		rod = new Particle(id+"_ROD", rodMaterial, tier, materialName + " Rod", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null,ingot.getItem(), null,
				null, ingot.getItem(), null,
				null, ingot.getItem(), null
		});
		plate = new Particle(id+"_PLATE", plateMaterial, tier, materialName + " Plate", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null, null, null,
				ingot.getItem(), rod.getItem(), ingot.getItem(),
				ingot.getItem(), rod.getItem(), ingot.getItem()
		});

	}
	
	public IngotMaterial(
			String id,
			ItemTier tier,
			Material ingotMaterial,
			Material dustMaterial,
			Material rodMaterial,
			Material plateMaterial,
			String materialName, 
			RecipeType recipeType,
			ItemStack[] recipe,
			String[] lore,
			boolean generateDust
	) {
		//ingot
		ingot = new Particle(id+"_INGOT", ingotMaterial, tier, materialName, "Material", lore, recipeType, recipe);
		if(generateDust) {
			dust = new Particle(id+"_DUST", dustMaterial, tier, materialName + " Dust", "Material", lore, RecipeType.NULL, null);
		}
		rod = new Particle(id+"_ROD", rodMaterial, tier, materialName + " Rod", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null,ingot.getItem(), null,
				null, ingot.getItem(), null,
				null, ingot.getItem(), null
		});
		plate = new Particle(id+"_PLATE", plateMaterial, tier, materialName + " Plate", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null, null, null,
				ingot.getItem(), rod.getItem(), ingot.getItem(),
				ingot.getItem(), rod.getItem(), ingot.getItem()
		});
	}
	
	public IngotMaterial(
			String id,
			ItemTier tier,
			Material ingotMaterial,
			Material dustMaterial,
			Material rodMaterial,
			Material plateMaterial,
			Material blockMaterial,
			String materialName, 
			RecipeType recipeType,
			ItemStack[] recipe,
			String[] lore,
			boolean generateDust
	) {
		//ingot
		ingot = new Particle(id+"_INGOT", ingotMaterial, tier, materialName, "Material", lore, recipeType, recipe);
		if(generateDust) {
			dust = new Particle(id+"_DUST", dustMaterial, tier, materialName + " Dust", "Material", lore, RecipeType.NULL, null);
		}
		rod = new Particle(id+"_ROD", rodMaterial, tier, materialName + " Rod", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null,ingot.getItem(), null,
				null, ingot.getItem(), null,
				null, ingot.getItem(), null
		});
		plate = new Particle(id+"_PLATE", plateMaterial, tier, materialName + " Plate", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				null, null, null,
				ingot.getItem(), rod.getItem(), ingot.getItem(),
				ingot.getItem(), rod.getItem(), ingot.getItem()
		});
		block = new Particle(id+"_BLOCK", blockMaterial, tier, materialName + " Block", "Material", lore, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				ingot.getItem(), ingot.getItem(), ingot.getItem(),
				ingot.getItem(), ingot.getItem(), ingot.getItem(),
				ingot.getItem(), ingot.getItem(), ingot.getItem()
		});
	}

	public ItemStack plate() {
		return plate.getItem();
	}
	
	public ItemStack wire() {
		return wire.getItem();
	}
	
	public @Nullable ItemStack block() {
		return block.getItem();
	}
	
	public ItemStack rod() {
		return rod.getItem();
	}
	
	public ItemStack ingot() {
		return ingot.getItem();
	}
	
	public @Nullable ItemStack dust() {
		return dust.getItem();
	}
	
	public ItemStack getItem() {
		return ingot.getItem();
	}
}
