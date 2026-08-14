package dev.cworldstar.anosf.items.weapons;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.handlers.BowShootHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.weapons.SlimefunBow;

public class WitherBow extends SlimefunBow {

	public static final SlimefunItemStack WITHER_BOW_ITEM_STACK = new SlimefunItemStack("WITHER_BOW", new ItemStackBuilder(Material.BOW)
			.name("<gradient:dark_gray:gray>Wither Bow")
			.lore(new String[] {
					"<gray>Entities shot with this bow will be affected",
					"<gray>by <white>Wither IV</white> for 3 seconds."
			})
			.build()
	);
	
	private static final ItemStack POISON_2_LINGERING = new ItemStack(Material.LINGERING_POTION);
	
	static {
		POISON_2_LINGERING.editMeta(PotionMeta.class, meta -> {
			meta.setBasePotionType(PotionType.STRONG_POISON);
		});
	}
	
	public WitherBow() {
		super(ItemRegistry.getItemGroup("WEAPON_CATEGORY"), WITHER_BOW_ITEM_STACK, new ItemStack[] {
			null, SlimefunItems.CARBONADO.asOne(), SlimefunItems.NECROTIC_SKULL.asOne(),
			SlimefunItems.STAFF_FIRE.asOne(), null, SlimefunItems.REINFORCED_PLATE.asOne(),
			null, SlimefunItems.CARBONADO.asOne(), SlimefunItems.NECROTIC_SKULL.asOne()
		});
		ItemRegistry.registerItem(this);
	}

	@Override
	public BowShootHandler onShoot() {
		return new BowShootHandler() {
			@Override
			public void onHit(EntityDamageByEntityEvent e, LivingEntity n) {
				n.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 3));
			}
		};
	}

}
