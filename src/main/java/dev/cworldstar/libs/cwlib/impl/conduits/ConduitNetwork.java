package dev.cworldstar.libs.cwlib.impl.conduits;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;

public abstract class ConduitNetwork {
	private static final ArrayList<ConduitNetwork> networks = new ArrayList<>();
	
	private Location originLocation;
	private ArrayList<Location> conduits = new ArrayList<>();
	
	public ConduitNetwork() {
		
	}
	
	public ConduitNetwork(Location origin, ArrayList<Location> locations) {
		originLocation = origin;
		conduits = locations;
	}
}
