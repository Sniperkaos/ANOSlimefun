package dev.cworldstar.anosf.items.weapons;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.handlers.BowShootHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.weapons.SlimefunBow;

public class LevitationBow extends SlimefunBow {

	public static final SlimefunItemStack LEVIATION_BOW_ITEM_STACK = new SlimefunItemStack("LEVITATION_BOW", new ItemStackBuilder(Material.BOW)
			.name("<gradient:dark_purple:light_purple:gray>Gravity Bow")
			.lore(new String[] {
					"<gray>Entities shot with this bow will be affected",
					"<gray>by <white>Levitation II</white> for 30 seconds."
			})
			.build()
	);
	
	public LevitationBow() {
		super(ItemRegistry.getItemGroup("WEAPON_CATEGORY"), LEVIATION_BOW_ITEM_STACK, new ItemStack[] {
			null, SlimefunItems.STAFF_STORM.asOne(), SlimefunItems.SYNTHETIC_SHULKER_SHELL.asOne(),
			SlimefunItems.STAFF_STORM.asOne(), null, SlimefunItems.ELYTRA_SCALE.asOne(),
			null, SlimefunItems.STAFF_STORM.asOne(), SlimefunItems.SYNTHETIC_SHULKER_SHELL.asOne()
		});
		ItemRegistry.registerItem(this);
	}

	@Override
	public BowShootHandler onShoot() {
		return new BowShootHandler() {
			@Override
			public void onHit(EntityDamageByEntityEvent e, LivingEntity n) {
				n.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 600, 1));
			}
		};
	}

}
