package dev.cworldstar.libs.cwlib.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;


@SerializableAs("RadiationZone")
public class RadiationZone extends BoundingBox implements ConfigurationSerializable {
    
	static {
		ConfigurationSerialization.registerClass(RadiationZone.class);
	}
	
	public static enum RadiationZoneLevel {
		
			CHERNOBYL(100), //-- INSTANT DEATH
			HIGH(50),
			MEDIUM_HIGH(40),
			MEDIUM(30),
			MEDIUM_SMALL(20),
			SMALL(10),
			DEFAULT(5),
			TRIVIAL(1);

			private int level = 5;
		
			public int level() {
				return this.level;
			}
		
			RadiationZoneLevel(int i) {
				this.level = i;
			}
	}
	
	private RadiationZoneLevel radiationLevel;
	private World world;
	
	private RadiationZone(World w, BoundingBox fallout, RadiationZoneLevel level) {
		super(fallout.getMinX(), fallout.getMinY(), fallout.getMinZ(), fallout.getMaxX(), fallout.getMaxY(), fallout.getMaxZ());
		this.radiationLevel = level;
		this.world = w;
	}

	@NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("minX", getMinX());
        result.put("minY", getMinY());
        result.put("minZ", getMinZ());
        result.put("maxX", getMaxX());
        result.put("maxY", getMaxY());
        result.put("maxZ", getMaxZ());
        result.put("radiation", radiation().name());
        result.put("world", world().getName());
        return result;
    }
	
	@NotNull
	public static RadiationZone deserialize(@NotNull Map<String, Object> args) {
		BoundingBox box = BoundingBox.deserialize(args);
		RadiationZoneLevel level = RadiationZoneLevel.DEFAULT;
		World world = Bukkit.getWorlds().get(0);
		
        if (args.containsKey("radiation")) {
        	level = RadiationZoneLevel.valueOf((String) args.get("radiation"));
        }
        
        if (args.containsKey("world")) {
        	world = (Bukkit.getWorld((String) args.get("world")));
        }
        
        return new RadiationZone(world, box, level);
	}
	
	public World world() {
		return world;
	}

	public RadiationZoneLevel radiation() {
		return radiationLevel;
	}

	public static RadiationZone of(World w, BoundingBox fallout, RadiationZoneLevel level) {
		return new RadiationZone(w, fallout, level);
	}
}
