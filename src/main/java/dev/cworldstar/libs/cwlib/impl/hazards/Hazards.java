package dev.cworldstar.libs.cwlib.impl.hazards;

import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageScaling;
import org.bukkit.damage.DamageType;
import org.bukkit.damage.DeathMessageType;
import org.bukkit.entity.LivingEntity;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.DamageTypeRegistryEntry.Builder;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.event.WritableRegistry;
import io.papermc.paper.registry.keys.DamageTypeKeys;
import net.kyori.adventure.key.Key;

public class Hazards {
	
	public static enum HazardKeys {
		BURN(AbstractSFAddon.key("CWLIB_HAZARD_BURN"));
		
		private NamespacedKey key;
		
		private HazardKeys(NamespacedKey key) {
			this.key = key;
		}
		
		public NamespacedKey key() {
			return this.key;
		}
	}
	
	public static enum HazardType {
		ATMOSPHERIC_PRESSURE,
		HIGH_ATMOSPHERIC_TEMPERATURE,
		LOW_ATMOSPHERIC_TEMPERATURE, 
	}
	
	public static enum HazardDamageTypes {
		ATMOSPHERIC_DAMAGE(Key.key("hazards", "ATMOSPHERIC_DAMAGE"), DamageEffect.HURT, DamageScaling.NEVER, DeathMessageType.DEFAULT, 1.0F);
		
		private Key key;
		private DamageEffect effect = DamageEffect.HURT;
		private DamageScaling scaling = DamageScaling.NEVER;
		private DeathMessageType messageType = DeathMessageType.INTENTIONAL_GAME_DESIGN;
		private float exhaustion = 0F;
		private boolean registered = false;
		
		HazardDamageTypes(Key key, DamageEffect effect, DamageScaling scaling, DeathMessageType messageType, float exhaustion) {
			this.key = key;
			this.effect = effect;
			this.scaling = scaling;
			this.messageType = messageType;
			this.exhaustion = exhaustion;
		}
		
		public Key getKey() {
			return key;
		}	
		
		public void register(WritableRegistry<DamageType, Builder> registry) {
			if(registered) return;
			registry.register(DamageTypeKeys.create(key), b -> {
				b.damageEffect(effect);
				b.damageScaling(scaling);
				b.deathMessageType(messageType);
				b.exhaustion(exhaustion);
			});
			registered = true;
		}
		
		private static Optional<DamageType> getDamageType(Key key) {
			final Registry<DamageType> damageTypeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.DAMAGE_TYPE);
			return Optional.ofNullable(damageTypeRegistry.get(key));
		}
		
		public DamageType asDamageType() {
			return getDamageType(key).orElseGet(() -> {
				return DamageType.OUT_OF_WORLD;
			});
		}
		
		public static void registerAll(WritableRegistry<DamageType, Builder> registry) {
			for(HazardDamageTypes type : HazardDamageTypes.values()) {
				type.register(registry);
			}
		}
		
	}
	
	public static boolean isProtected(LivingEntity livingEntity, HazardType type) {
		return true;
	}

	public static void registerDamageTypes(BootstrapContext context) {
		context.getLifecycleManager().registerEventHandler(
			RegistryEvents.DAMAGE_TYPE.compose().newHandler(e -> {
				HazardDamageTypes.registerAll(e.registry());
			})
		);
	}
}
