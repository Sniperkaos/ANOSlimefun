package dev.cworldstar.anosf.items.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.anosf.items.Items.ItemTier;
import dev.cworldstar.libs.cwlib.CustomDurabilityHandler;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.handlers.DurabilityLossHandler;
import dev.cworldstar.libs.cwlib.handlers.ItemMendHandler;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;

public class Hammer extends SlimefunItem {
	
	private static final ArrayList<ItemStack> DROPPED_DUSTS = new ArrayList<ItemStack>();
	private static final List<Material> REQUIRED_BROKEN_BLOCKS = new ArrayList<Material>();

	public static List<ItemStack> getDusts() {
		return DROPPED_DUSTS.stream().collect(Collectors.toUnmodifiableList());
	}
	
	public static void addDust(ItemStack dust) {
		DROPPED_DUSTS.add(dust);
	}
	static {
		REQUIRED_BROKEN_BLOCKS.addAll(List.of(Material.STONE, Material.DEEPSLATE, Material.DIORITE, Material.ANDESITE, Material.GRANITE));
	}
	
	private static double handleFortune(int fortuneLevel) {
		return ((1 * fortuneLevel + 2 + fortuneLevel + 1) / 2);
	}
	
	public static void onBlockBreak(Player whoBroke, Block broken, ItemStack with) {
		if(!REQUIRED_BROKEN_BLOCKS.contains(broken.getType())) {
			return;
		}
		if(!Slimefun.getProtectionManager().hasPermission(whoBroke, broken, Interaction.BREAK_BLOCK)) return;
		broken.breakNaturally(with);
		int dustChanceIncrease = 1;
		Player p = whoBroke;
		@NotNull Collection<PotionEffect> effects = p.getActivePotionEffects();
		for(PotionEffect effect : effects) {
			if(effect.getType().equals(PotionEffectType.LUCK)) {
				dustChanceIncrease += effect.getAmplifier();
			} else if(effect.getType().equals(PotionEffectType.UNLUCK)) {
				dustChanceIncrease -= effect.getAmplifier();
			}
		}
		
		boolean shouldDropDust = (RandomUtils.nextInt(0 + (dustChanceIncrease), 75 + (dustChanceIncrease))+1) >= 75;
		@Nonnull ItemStack dustDrop = DROPPED_DUSTS.get(RandomUtils.nextInt(1, DROPPED_DUSTS.size()) - 1);
		if(!shouldDropDust) return;
		
		double drops = 1.0D;
		ItemStack i = whoBroke.getInventory().getItemInMainHand();
		if(i.containsEnchantment(Enchantment.FORTUNE)) {
			double multiplier = handleFortune(i.getEnchantmentLevel(Enchantment.FORTUNE));
			drops *= multiplier;
		}
		
		Location loc = broken.getLocation();
		World world = loc.getWorld();
		world.spawnParticle(Particle.SCULK_SOUL, 0, 0, 0, 8);
		world.dropItem(loc, dustDrop.asQuantity((int) Math.round(drops)));
	}
	
	public Hammer() {
		super(ItemRegistry.getItemGroup("TOOL_CATEGORY"), new SlimefunItemStack("SIEVING_HAMMER", CustomDurabilityHandler.setup(
				new ItemStackBuilder(Material.IRON_PICKAXE)
					.name("<gray>Sieving Hammer")
					.lore(new String[] {
							"<gray>Drops <aqua>dust<gray> while mining stone.",
							"<gray>AOE break will only affect stone.",
							"",
							ItemTier.BASIC.makeItemString("Tool")
					})
					.attribute("PICKAXE_MINING_EFFICIENCY_NERF", Attribute.MINING_EFFICIENCY, -20, EquipmentSlotGroup.MAINHAND, Operation.ADD_NUMBER)
					.attribute("PICKAXE_NERF", Attribute.BLOCK_BREAK_SPEED, -0.8, EquipmentSlotGroup.MAINHAND, Operation.ADD_NUMBER)
					.get()
		,90L)), RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
				SlimefunItems.REINFORCED_ALLOY_INGOT.asOne(), SlimefunItems.REINFORCED_ALLOY_INGOT.asOne(), SlimefunItems.REINFORCED_ALLOY_INGOT.asOne(),
				SlimefunItems.REINFORCED_ALLOY_INGOT.asOne(), ItemRegistry.getRegistryItem("IRON_ROD").getItem(), SlimefunItems.REINFORCED_ALLOY_INGOT.asOne(),
				null, ItemRegistry.getRegistryItem("IRON_ROD").getItem(), null
		});
		
		addDust(ItemRegistry.getRegistryItem("CHROME_DUST").getItem());
		
		enchantable = false;
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
		
		addItemHandler(Hammer.onToolUse());

		ItemRegistry.registerItem(this);
	}
	
	public static ToolUseHandler onToolUse() {
		return new ToolUseHandler() {
			
			@Override
			public void onToolUse(BlockBreakEvent e, ItemStack tool, int fortune, List<ItemStack> drops) {
				if(e.isCancelled()) {
					return;
				}
				
				//drops.add(DROPPED_DUSTS.get(RandomUtils.nextInt(1, DROPPED_DUSTS.size())-1));
				Player player = e.getPlayer();
				Block b = e.getBlock();
				Location blockLocation = b.getLocation();
				BlockFace face = player.getFacing();
				
				if(blockLocation.getBlockY() < player.getLocation().getBlockY() || blockLocation.getBlockY() >= player.getLocation().add(new Vector(0, 2, 0)).getBlockY()) {
					// breaking block above or below
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(0,0,1)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(0,0,-1)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(1,0,1)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(1,0,-1)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(-1,0,1)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(-1,0,-1)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(1,0,0)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(-1,0,0)).getBlock(), tool);
					return;
				}
				
				if(face.equals(BlockFace.EAST) || face.equals(BlockFace.WEST)) {
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(0,0,1)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(0,0,-1)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(0,1,0)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(0,-1,0)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(0,1,1)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(0,1,-1)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(0,-1,1)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(0,-1,-1)).getBlock(), tool);
				} else if(face.equals(BlockFace.NORTH) || face.equals(BlockFace.SOUTH)) {
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(0,1,0)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(0,-1,0)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(1,0,0)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(-1,0,0)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(1,1,0)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(-1,1,0)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(1,-1,0)).getBlock(), tool);
					onBlockBreak(e.getPlayer(), blockLocation.clone().add(new Vector(-1,-1,0)).getBlock(), tool);
				}
			}
		};
	}
}
