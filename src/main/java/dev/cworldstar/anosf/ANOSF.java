package dev.cworldstar.anosf;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageScaling;
import org.bukkit.damage.DamageType;
import org.bukkit.damage.DeathMessageType;
import org.bukkit.plugin.java.JavaPlugin;


import dev.cworldstar.anosf.entities.AbstractEnemy;
import dev.cworldstar.anosf.events.EventRegistry;
import dev.cworldstar.anosf.guide.MultiblockChatOption;
import dev.cworldstar.anosf.impl.drugs.DrugProfile;
import dev.cworldstar.anosf.impl.radiation.ExtendedRadiation;
import dev.cworldstar.anosf.items.Items;
import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.utils.ConfigUtils;
import io.github.thebusybiscuit.slimefun4.core.guide.options.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.event.WritableRegistry;
import io.papermc.paper.registry.keys.DamageTypeKeys;
import io.papermc.paper.registry.data.DamageTypeRegistryEntry.Builder;

import lombok.Getter;
import net.kyori.adventure.key.Key;

public class ANOSF extends AbstractSFAddon {

	@Getter
	private YamlConfiguration drugContainer;
	@Getter
	private ExtendedRadiation radiation;
	@Getter
	private MultiblockChatOption multiblockChatOption;
	
	public static ExtendedRadiation radiation() {
		return ((ANOSF) get()).getRadiation();
	}
	
	public ConfigurationSection getDProfile(String key) {
		return drugContainer.getConfigurationSection(key);
	}
	
	public JavaPlugin getJavaPlugin() {
		return this;
	}

	public String getBugTrackerURL() {
		return "";
	}
	
	@Override
	protected void enable() {
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		File saveFolder = new File(getDataFolder() + "/save");
		if(!saveFolder.exists()) {
			saveFolder.mkdir();
		}

		ConfigUtils.saveDefault(new File(getDataFolder() + "/save/threads.yml"), "threads.yml");
		drugContainer = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "/save/threads.yml"));
		multiblockChatOption = new MultiblockChatOption();
		SlimefunGuideSettings.addOption(multiblockChatOption);
		Items.toRegistry();
		ItemRegistry.finalizeRegistry();
		EventRegistry.complete();
		
		Bukkit.getScheduler().runTask(this, ()-> {
			Slimefun.getRegistry().getAllSlimefunItems().forEach(item -> {
				
			});
		});
	}
	
	
	public static enum ANOSFDamageType {
		COSMIC_DAMAGE(Key.key("anosf", "cosmic_damage"), DamageEffect.HURT, DamageScaling.NEVER, DeathMessageType.DEFAULT, 1.0F),
		RADIATION_DAMAGE(Key.key("anosf", "radiation_damage"), DamageEffect.THORNS, DamageScaling.NEVER, DeathMessageType.DEFAULT, 0.1F);

		private Key key;
		private DamageEffect effect = DamageEffect.HURT;
		private DamageScaling scaling = DamageScaling.NEVER;
		private DeathMessageType messageType = DeathMessageType.INTENTIONAL_GAME_DESIGN;
		private float exhaustion = 0F;
		private boolean registered = false;
		
		ANOSFDamageType(Key key, DamageEffect effect, DamageScaling scaling, DeathMessageType messageType, float exhaustion) {
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
				ANOSF.log(Level.WARNING, "An attempt was made to get DamageType " + key.asString() + ", but it did not exist!");
				return DamageType.OUT_OF_WORLD;
			});
		}
		
		public static void registerAll(WritableRegistry<DamageType, Builder> registry) {
			for(ANOSFDamageType type : ANOSFDamageType.values()) {
				type.register(registry);
			}
		}
		
	}

	@Override
	public void onStart(BootstrapContext context) {		
		context.getLifecycleManager().registerEventHandler(
			RegistryEvents.DAMAGE_TYPE.compose().newHandler(e -> {
				ANOSFDamageType.registerAll(e.registry());
			})
		);
	}
	
	@Override
	protected void disable() {
		try {
			DrugProfile.toYamlConfiguration().save(new File(getDataFolder() + "/save/threads.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		AbstractEnemy.enemies.forEach(enemy -> {
			enemy.getEntity().remove();
		});
	}
}
