package dev.cworldstar.anosf.impl.runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.anosf.items.recipes.ParticleAcceleratorRecipe;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.events.SFTickEvent;
import dev.cworldstar.libs.cwlib.protocol.ItemEditorProtocol;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemDropHandler;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import net.kyori.adventure.text.Component;

public class SpeedRune extends SimpleSlimefunItem<ItemDropHandler> implements Listener {

    private static final double RANGE = 1.5;
	
    @EventHandler
    public void onTickerTick(SFTickEvent e) {
    	for(Player player : Bukkit.getOnlinePlayers()) {
    		ItemStack bootsItem = player.getInventory().getBoots();
    		if(bootsItem == null) continue;
    		if(!(bootsItem.getPersistentDataContainer().has(ANOSF.key("SPEED_RUNE"), PersistentDataType.BYTE))) continue;
    		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 1));
    	}
    }
    
    public static final SlimefunItemStack SPEED_RUNE_ITEM = new SlimefunItemStack("SPEED_RUNE", 
    		new ItemStackBuilder(Material.FIREWORK_STAR)
    			.name("<gray>Ancient Rune <bold><dark_gray>[<gradient:yellow:white>Speed<dark_gray>]")
    			.lore(new String[] {
    				"<yellow>Drop this rune onto a dropped pair of <white>boots<yellow>",
    				"<yellow>to make it <gradient:yellow:white>speedy<yellow>, giving it permanent",
    				"<gradient:yellow:white>Speed II<yellow> while worn.",
    			})
    		.build()
    );
    
	public SpeedRune() {
		super(ItemRegistry.getItemGroup("TOOL_CATEGORY"), SPEED_RUNE_ITEM, Items.ACCELERATOR_RECIPE_TYPE, null);
		new ParticleAcceleratorRecipe(
				SlimefunItems.SLIME_BOOTS_STEEL.asOne(),
				this.getItem(),
				SlimefunItems.LIGHTNING_RUNE.asOne(),
				1024,
				"SPEED_RUNE_SYNTHESIS"
		);
		ItemEditorProtocol.passthrough((item, player) -> {
    		if(!(item.getPersistentDataContainer().has(ANOSF.key("SPEED_RUNE"), PersistentDataType.BYTE))) return item;
			List<Component> lore = item.lore();
			if(lore == null) {
				lore = new ArrayList<Component>();
			}
			lore.add(
					FormatUtils.mm("<gray>Rune: <white>Speed")
			);
			item.lore(lore);
			return item;
		});
		ItemRegistry.registerItem(this);
		Bukkit.getPluginManager().registerEvents(this, ANOSF.get());
	}

	@Override
	public ItemDropHandler getItemHandler() {
		return new ItemDropHandler() {
			@Override
			public boolean onItemDrop(PlayerDropItemEvent e, Player p, Item item) {
	            if (isItem(item.getItemStack())) {
	                if (!canUse(p, true)) {
	                    return true;
	                }
	                Slimefun.runSync(() -> activate(p, item), 20L);
	                return true;
	            }
	            return false;
			}
		};
	}

    private void activate(@Nonnull Player p, @Nonnull Item rune) {
        if (!rune.isValid()) {
            return;
        }

        Location l = rune.getLocation();
        Collection<Entity> entites = l.getWorld().getNearbyEntities(l, RANGE, RANGE, RANGE, this::findCompatibleItem);
        Optional<Entity> optional = entites.stream().findFirst();

        if (optional.isPresent()) {
            Item item = (Item) optional.get();
            ItemStack itemStack = item.getItemStack();

            if (itemStack.getAmount() == 1) {
                // This lightning is just an effect, it deals no damage.
                l.getWorld().strikeLightningEffect(l);

                Slimefun.runSync(() -> {
                    // Being sure entities are still valid and not picked up or whatsoever.
                    if (rune.isValid() && item.isValid() && itemStack.getAmount() == 1) {

                        l.getWorld().createExplosion(l, 0);
                        SoundEffect.SOULBOUND_RUNE_RITUAL_SOUND.playAt(l, SoundCategory.PLAYERS);

                        item.remove();
                        rune.remove();

                        itemStack.editPersistentDataContainer(pdc -> {
                        	pdc.set(ANOSF.key("SPEED_RUNE"), PersistentDataType.BYTE, (byte) 0x00);
                        });
                        
                        l.getWorld().dropItemNaturally(l, itemStack);

                        p.sendMessage("<green>The speed rune was applied!");
                    } else {
                        p.sendMessage("<red>The speed rune failed to apply.");
                    }
                }, 10L);
            } else {
            	p.sendMessage("<red>The speed rune failed to apply.");
            }
        }
    }
    
    private boolean findCompatibleItem(@Nonnull Entity entity) {
        if (entity instanceof Item) {
        	Item item = (Item) entity;
            return !isItem(item.getItemStack()) &&
            		item.getItemStack().getType().toString().contains("BOOTS");
        }
        return false;
    }
	
}
