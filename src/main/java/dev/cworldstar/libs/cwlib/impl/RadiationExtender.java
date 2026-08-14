package dev.cworldstar.libs.cwlib.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.events.RadiationZoneEnterEvent;
import dev.cworldstar.libs.cwlib.events.RadiationZoneLeaveEvent;
import dev.cworldstar.libs.cwlib.events.SFTickEvent;
import dev.cworldstar.libs.cwlib.listeners.AbstractListener;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.utils.RadiationUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class RadiationExtender extends AbstractListener {
	
	public RadiationExtender(AbstractSFAddon addon) {
		super(addon);
	}

	private static List<RadiationZone> IRRADIATED_LOCATIONS = new ArrayList<RadiationZone>();
	private static List<Player> PLAYERS_IN_IRRADIATED_ZONE = new ArrayList<Player>();
	
	public static void spawnRadiationZoneCloud(@NotNull RadiationZone zone, @NotNull Vector vector) {
		World world = zone.world();
		world.spawnParticle(Particle.WHITE_SMOKE, vector.getX(), vector.getY(), vector.getZ(), RandomUtils.nextInt(3, 12));
	}
	
	public static void markIrradiatedLocation(RadiationZone location) {
		IRRADIATED_LOCATIONS.add(location);
	}
	
	public static List<RadiationZone> getIrradiatedLocations() {
		return List.copyOf(IRRADIATED_LOCATIONS);
	}
	
	public static double nextDoubleNegative(double min, double max) {
		return Math.random() * (max - min) + min;
	}
	
	@EventHandler
	public static void onSlimefunTick(SFTickEvent e) {
		List<Player> inZone = new ArrayList<Player>();
		for(RadiationZone bb : IRRADIATED_LOCATIONS) {
			
			if(e.tick() % 50 == 0) {
				for(int i=0; i<=RandomUtils.nextInt(4, 10); i++) {
					spawnRadiationZoneCloud(bb, bb.getCenter().add(
							new Vector(
								nextDoubleNegative(bb.getMinX(), bb.getMaxX()),
								nextDoubleNegative(bb.getMinY(), bb.getMaxY()),
								nextDoubleNegative(bb.getMinZ(), bb.getMaxZ())
							)
					));
				}
			}
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				Optional<PlayerProfile> profile = PlayerProfile.find(p);
				if(profile.get() != null) {
					PlayerProfile pprofile = profile.get();
					Location playerLocation = p.getLocation();
					if(bb.contains(playerLocation.toVector()) && !inZone.contains(p)) {
						if(!PLAYERS_IN_IRRADIATED_ZONE.contains(p)) {
							PLAYERS_IN_IRRADIATED_ZONE.add(p);
							inZone.add(p);
							p.sendMessage(MiniMessage.builder().tags(TagResolver.standard()).build().deserialize("<gradient:#118F1A:#D4D22A><bold>☢️ You are now entering a radioactive zone!</gradient>"));
							Bukkit.getServer().getPluginManager().callEvent(new RadiationZoneEnterEvent(p));
						}
						
						if(e.tick() % 100 == 0) {
							if(pprofile.hasFullProtectionAgainst(ProtectionType.RADIATION)) {
								return;
							}
							
							if(p.getGameMode().equals(GameMode.CREATIVE)) {
								return;
							}
							
							RadiationUtils.addExposure(p, 5);
							if(RadiationUtils.getExposure(p) > 70) {
								p.sendMessage(MiniMessage.builder().tags(TagResolver.standard()).build().deserialize("<gradient:#FF1E1E:#D4D22A><bold> ☢️ You are dangerously close to death! Leave the zone immediately!</gradient>"));
							}
						}
					} else {
						if(PLAYERS_IN_IRRADIATED_ZONE.contains(p) && inZone.contains(p)) {
							PLAYERS_IN_IRRADIATED_ZONE.remove(p);
							Bukkit.getServer().getPluginManager().callEvent(new RadiationZoneLeaveEvent(p));
							p.sendMessage(MiniMessage.builder().tags(TagResolver.standard()).build().deserialize("<gradient:#118F1A:#D4D22A><bold>☢️ You are now leaving a radioactive zone.</gradient>"));

						}
					}
				}	
			}
		}
	}
	
}
