package dev.cworldstar.libs.cwlib.impl.breathing;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Zoglin;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.events.SFTickEvent;
import dev.cworldstar.libs.cwlib.impl.SlimefunGas;
import dev.cworldstar.libs.cwlib.impl.SlimefunGas.GasProperties;
import dev.cworldstar.libs.cwlib.impl.gas.ChunkGasStorage;
import dev.cworldstar.libs.cwlib.impl.gas.GasStorage;
import dev.cworldstar.libs.cwlib.impl.gas.GasStorage.StoredGas;
import dev.cworldstar.libs.cwlib.impl.hazards.Hazards;
import dev.cworldstar.libs.cwlib.impl.hazards.Hazards.HazardKeys;
import dev.cworldstar.libs.cwlib.impl.hazards.Hazards.HazardType;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import net.kyori.adventure.util.TriState;

public class Breathing implements Listener {
		
	private static final NamespacedKey LUNGS_KEY = AbstractSFAddon.key("CWLIB_LUNGS");
	
	public Breathing() {
		AbstractSFAddon.registerListener(this);
	}
	
	@EventHandler
	public void onChunkGenerate(ChunkPopulateEvent e) {
		Environment environment = e.getWorld().getEnvironment();
		PersistentDataContainer pdc = e.getChunk().getPersistentDataContainer();
		
		if(pdc.has(ChunkGasStorage.key())) return;
		
		ChunkGasStorage storage = new ChunkGasStorage();
		pdc.set(ChunkGasStorage.key(), ChunkGasStorage.dataType(), storage);
		
		switch(environment) {
			case NETHER:
				SlimefunGas.NETHER_AIR.addPollutionValue(
					e.getChunk(), 
					SlimefunGas.NETHER_AIR.getVolumeFromPercentage(
						storage, // it's ok to use this even though it won't update because #getVolumeFromPercentage calls no methods of GasStorage other than #size which is final
						33
					)
				);
				break;
			case THE_END:
				SlimefunGas.END_AIR.addPollutionValue(
						e.getChunk(), 
						SlimefunGas.END_AIR.getVolumeFromPercentage(
							storage, // it's ok to use this even though it won't update because #getVolumeFromPercentage calls no methods of GasStorage other than #size which is final
							15
						)
					);
				break;
			default:
				SlimefunGas.AIR.addPollutionValue(
						e.getChunk(), 
						SlimefunGas.AIR.getVolumeFromPercentage(
							storage, // it's ok to use this even though it won't update because #getVolumeFromPercentage calls no methods of GasStorage other than #size which is final
							75
						)
					);
				break;
		}
	}
	
	
	
	@EventHandler
	public void onSlimefunTick(SFTickEvent e) {
		if(!e.subtick(40)) return;
		for(World w : Bukkit.getWorlds()) {
			for(Chunk c : w.getLoadedChunks()) {
				@NotNull ChunkGasStorage chunkGas = SlimefunGas.getChunkGasStorage(c);
				for(Entity entity : c.getEntities()) {
					if(!(entity instanceof LivingEntity)) continue;
					
					if((entity instanceof LivingEntity livingEntity)) {
						// handle breathing
						Lungs lungs = getLungs(livingEntity);
						if(lungs != null) {
							lungs.breathe(livingEntity);
						}
						
						float chunkTemperature = 60.0F;
						float chunkAtmosphere = 0.0F;
						
						// handle atmosphere
						for(StoredGas gas : chunkGas.gases()) {
							GasProperties properties = gas.toSlimefunGas().getProperties();
							float atmosphericPercentage = (gas.getPressure() / chunkGas.getAtmospherePressure());
							
							chunkAtmosphere += gas.getPressure();
							
							chunkTemperature += ((properties.getTemperature() - 60F) * atmosphericPercentage);
						}
						
						if(
							chunkAtmosphere > 102400F || chunkAtmosphere < 1024F && 
							!(Hazards.isProtected(livingEntity, HazardType.ATMOSPHERIC_PRESSURE))
						) {
							livingEntity.damage(
								20.0, 
								DamageSource.builder(
									Hazards.HazardDamageTypes.ATMOSPHERIC_DAMAGE.asDamageType()
								).build()
							);
						}
						
						if(
							chunkTemperature > 212F && 
							!(Hazards.isProtected(livingEntity, HazardType.HIGH_ATMOSPHERIC_TEMPERATURE))
						) {
							
							float exposure = livingEntity.getPersistentDataContainer().getOrDefault(
								HazardKeys.BURN.key(), 
								PersistentDataType.FLOAT,
								0F
							);
							
							if(exposure > 8) {
								livingEntity.setVisualFire(TriState.TRUE);
							}
							
							float burnRate;
							
						    if (chunkTemperature <= 110F) {
						    	burnRate = 0.0F;
						    } else {
							    float kelvin = ((chunkTemperature - 32.0F) * 0.555F) + 273.15F;
							    burnRate = (float) Math.exp(
							        (kelvin - 317.15F) / 22.0F
							    );
						    }

							
							float newExposure = (float) (exposure + (burnRate * 2));
															
							double damage = Math.max(1, 2.0 * Math.log1p(newExposure));
							
							livingEntity.damage(
								damage,
								DamageSource.builder(DamageType.ON_FIRE).build()
							);
							livingEntity.getPersistentDataContainer().set(HazardKeys.BURN.key(), PersistentDataType.FLOAT, newExposure);
													
						} else {
							float exposure = livingEntity.getPersistentDataContainer().get(HazardKeys.BURN.key(), PersistentDataType.FLOAT);
							float newExposure = (float) Math.max(0.0F, exposure - 0.1);
							
							livingEntity.getPersistentDataContainer().set(HazardKeys.BURN.key(), PersistentDataType.FLOAT, newExposure);
							
							if(newExposure <= 0.0F && exposure > 0.0F) {
								livingEntity.sendMessage(FormatUtils.mm("<red>You no longer feel on fire."));
								livingEntity.setVisualFire(TriState.NOT_SET);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		PersistentDataContainer pdc = e.getPlayer().getPersistentDataContainer();
		if(pdc.get(LUNGS_KEY, Lungs.dataType()) == null) {
			pdc.set(LUNGS_KEY, Lungs.dataType(), new Lungs());
		}
	}
	
	@EventHandler
	public void onPlayerSpawn(PlayerRespawnEvent e) {
		PersistentDataContainer pdc = e.getPlayer().getPersistentDataContainer();
		pdc.set(LUNGS_KEY, Lungs.dataType(), new Lungs());
	}
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent e) {
		if(e.getEntity() instanceof LivingEntity entity) {
			if(
				(entity instanceof Zombie) ||
				(entity instanceof Skeleton) ||
				(entity instanceof Phantom) ||
				(entity instanceof Zoglin) ||
				(entity instanceof Wither) ||
				(entity instanceof IronGolem)
			) {
				return;
			}
			PersistentDataContainer pdc = entity.getPersistentDataContainer();
			pdc.set(LUNGS_KEY, Lungs.dataType(), new Lungs());
		}
	}
	
	/**
	 * A return value of 1.0 means that the current stack is filtered,
	 * you will not be affected by the given gas. A return value of 0.0
	 * means that the full gas effectiveness is applied to you.
	 * @param breather
	 * @param gas
	 * @return
	 */
	public static float isFiltered(LivingEntity breather, SlimefunGas gas) {
		for(ItemStack item : breather.getEquipment().getArmorContents()) {
			SlimefunItem sfItem = SlimefunItem.getByItem(item);
			if(sfItem != null) {
				if(sfItem instanceof AtmosphereFilter filter) {
					float filtered = filter.getFilterProperties().filters(gas);
					if(filtered == 0) {
						return 0.0F;
					}
					return Math.clamp(filtered, 0F, 1F);
				}
			}
		}
		return 0.0F;
	}

	public static @Nullable Lungs getLungs(LivingEntity entity) {
		PersistentDataContainer pdc = entity.getPersistentDataContainer();
		if(pdc.has(LUNGS_KEY)) {
			return pdc.get(LUNGS_KEY, Lungs.dataType());
		}
		return null;
	}

	//TODO: implement wearable gas tanks as GasStorage atmosphere replacement
	public static GasStorage getAtmosphere(LivingEntity owner) {
		return SlimefunGas.getChunkGasStorage(owner.getChunk());
	}
}
