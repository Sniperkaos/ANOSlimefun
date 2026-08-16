package dev.cworldstar.libs.cwlib;

import java.io.File;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import dev.cworldstar.libs.cwlib.events.SFTickEvent;
import dev.cworldstar.libs.cwlib.impl.ExplosionManager;
import dev.cworldstar.libs.cwlib.impl.RadiationExtender;
import dev.cworldstar.libs.cwlib.impl.RadiationZone;
import dev.cworldstar.libs.cwlib.impl.breathing.Breathing;
import dev.cworldstar.libs.cwlib.impl.hazards.Hazards;
import dev.cworldstar.libs.cwlib.listeners.AbstractListener;
import dev.cworldstar.libs.cwlib.listeners.AutoDisenchantListener;
import dev.cworldstar.libs.cwlib.listeners.DurabilityDamageListener;
import dev.cworldstar.libs.cwlib.listeners.PlayerAttackHandlerListener;
import dev.cworldstar.libs.cwlib.listeners.PlayerCraftListener;
import dev.cworldstar.libs.cwlib.listeners.TickListener;
import dev.cworldstar.libs.cwlib.protocol.ItemEditorProtocol;
import dev.cworldstar.libs.cwlib.protocol.ItemNameLerpPassthrough;
import dev.cworldstar.libs.cwlib.utils.ConfigUtils;
import dev.cworldstar.libs.cwlib.utils.RecipeUtils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractSFAddon extends JavaPlugin implements SlimefunAddon, PluginBootstrap {
	
	private static AbstractSFAddon addon;
	
    @SuppressWarnings("unchecked")
	public static <T extends AbstractSFAddon> T get() {
        return (T) addon;
    }
    
    @Override
    public void bootstrap(BootstrapContext context) {
    	onStart(context);
    }
    
    @Getter
    private static Lang lang;

    @Getter
	private ExplosionManager explosionManager;
    
	@Getter
	@Setter
	private long lastSlimefunTick = 0L;
	@Getter
	@Setter
	private double slimefunTickDelta = 0D;
    @Getter
    @Setter
    private long slimefunTicks = 0L;
    
    public static NamespacedKey key(String key) {
    	return new NamespacedKey(get(), key);
    }
    
	@Override
	public void onLoad() {
		try {
			load();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onStart(BootstrapContext context) {
		try {
			Hazards.registerDamageTypes(context);
			start(context);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void start(BootstrapContext context) {
		
	}
	
	public static void sync(Runnable r) {
		if(addon.isEnabled() == true) {
			addon.getServer().getScheduler().runTask(addon, r);
		}
	}
	
	private void setup() {
		
		RecipeUtils.plugin(this);
		
		explosionManager = ExplosionManager.start();
		
		AbstractListener.register(this, DurabilityDamageListener.class);
		AbstractListener.register(this, PlayerAttackHandlerListener.class);
		AbstractListener.register(this, PlayerCraftListener.class);
		AbstractListener.register(this, TickListener.class);
		AbstractListener.register(this, AutoDisenchantListener.class);
    	
		File savefile = new File(getDataFolder(), "irradiatedlocations.yml");
		ConfigUtils.saveDefault(savefile, "irradiatedlocations.yml");
		YamlConfiguration locations = YamlConfiguration.loadConfiguration(savefile);
		for(Entry<String, Object> entry : locations.getValues(false).entrySet()) {
			if(entry.getValue() instanceof RadiationZone) {
				RadiationExtender.markIrradiatedLocation((RadiationZone) entry.getValue());
			}
		}
		
    	Bukkit.getServer().getAsyncScheduler().runAtFixedRate(
        		this, 
        		(ScheduledTask task) -> {
        			
        			if(Slimefun.instance() == null) return;
        			
        			if(!(this.lastSlimefunTick == 0)) {
            			this.slimefunTickDelta = (System.currentTimeMillis() - getLastSlimefunTick()) / Slimefun.getTickerTask().getTickRate();
        			}
        			SFTickEvent event = new SFTickEvent(lastSlimefunTick);
        			sync(new Runnable() {
						@Override
						public void run() {
							Bukkit.getServer().getPluginManager().callEvent(event);							
						} 
        			});
        			addon.setSlimefunTicks(addon.getSlimefunTicks() + 1);
        			addon.setLastSlimefunTick(System.currentTimeMillis());
        		}, 
        		0L, 
        		Slimefun.getTickerTask().getTickRate(), 
        		TimeUnit.MILLISECONDS
        	);
		
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
			ItemEditorProtocol.start();
			new ItemNameLerpPassthrough();
		}
	}

	@Override
	public void onEnable() {
		addon = this;
		lang = new Lang();
		setup();
		
		try {
			enable();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable() {
		try {
			disable();
			File saveFile = new File(getDataFolder(), "irradiatedlocations.yml");
			YamlConfiguration locations = YamlConfiguration.loadConfiguration(saveFile);
			int index = 0;
	    	for(RadiationZone zone : RadiationExtender.getIrradiatedLocations()) {
	    		locations.set(String.valueOf(index), zone);
	    		index ++;
	    	}
	    	locations.save(saveFile);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
    /**
     * Triggers when {@link JavaPlugin#onLoad()} triggers. At this point, {@link AbstractSFAddon#get()} will return null.
     */
	protected void load() {
		
	};
	
	protected abstract void enable();
	
	protected abstract void disable();

	public static void log(Level level, String string) {
		Bukkit.getLogger().log(level, string);
	}

	public static ScheduledTask async(Consumer<ScheduledTask> r) {
		ScheduledTask task = Bukkit.getServer().getAsyncScheduler().runNow(addon, r);
		return task;
	}

	public static <T extends Listener> void registerListener(T listener) {
		Bukkit.getPluginManager().registerEvents(listener, addon);
	}
}
