package dev.cworldstar.libs.cwlib.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.RayTraceResult;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.handlers.PlayerAttackHandler;
import dev.cworldstar.libs.cwlib.handlers.SwordSwingHandler;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
/**
 * This will handle the PlayerAttackHandlers.
 * @author cworldstar
 *
 */
public class PlayerAttackHandlerListener extends AbstractListener {

	public PlayerAttackHandlerListener(AbstractSFAddon addon) {
		super(addon);
	}
	
	@EventHandler
	public void onPlayerSwing(PlayerArmSwingEvent e) {
		Player player = e.getPlayer();
		Location eyeLocation = player.getEyeLocation();
		PlayerInventory playerInventory = player.getInventory();
		
		ItemStack heldItem = playerInventory.getItem(e.getHand());
		if(heldItem == null) {
			ANOSF.log(Level.INFO, "no held item");
			return;
		}
		
		SlimefunItem sfItem = SlimefunItem.getByItem(heldItem);
		
		if(sfItem == null) {
			return;
		}
		
		RayTraceResult result = player.getWorld().rayTrace(
			eyeLocation, 
			eyeLocation.getDirection(), 
			player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE).getValue(), 
			FluidCollisionMode.NEVER, 
			true, 
			0.1, 
			entity -> (entity instanceof LivingEntity && !(player.equals(entity)))
		);
		
		if(result == null || result.getHitEntity() == null) {
			ANOSF.log(Level.INFO, "result was nil or no hit entity");
			return;
		}
		
		boolean cancel = sfItem.callItemHandler(
			SwordSwingHandler.class, 
			handler -> handler.onPlayerArmSwing(
				e, 
				player, 
				(LivingEntity) result.getHitEntity()
			)
		);
		
		if(cancel) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent e) {
		Entity player = e.getDamager();
		if(player instanceof Player) {
			List<Class<? extends SlimefunItem>> triggered = new ArrayList<>();
			//-- this will prevent items from triggering twice from the same PlayerAttackHandler.
	        for(ItemStack item : ((Player) player).getInventory()) {
	        	if(SlimefunItem.getByItem(item) != null) {
	        		SlimefunItem sfitem = SlimefunItem.getByItem(item);
	        		boolean cancel = false;
	        		for(Class<? extends SlimefunItem> claz : triggered) {
	        			if(claz.isInstance(sfitem)) {
	        				cancel = true;
	        				break;
	        			}
	        		}
	        		
	        		if(cancel) {
	        			continue;
	        		}
	        		
	        		if(sfitem instanceof SlimefunItem) {
	        			SlimefunItem pet = (SlimefunItem) sfitem;
						Class<? extends SlimefunItem> clazz = pet.getClass();
						triggered.add(clazz);
	        		}
	        		sfitem.callItemHandler(PlayerAttackHandler.class, handler -> handler.onPlayerAttack(e, (Player) player, e.getEntity()));
	        	}
	        }
		}
	}
}
