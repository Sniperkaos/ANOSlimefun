package dev.cworldstar.anosf.entities.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.anosf.entities.Overhealth;
import lombok.Getter;
import lombok.Setter;

public class BossBarTask extends BukkitRunnable {

	@Getter
	@Setter
	private LivingEntity target;
	@Getter
	@Setter
	private Overhealth targetOverhealth;
	@Getter
	private BossBar targetBossBar;
	
	private List<UUID> viewers = new ArrayList<UUID>();
	
	/**
	 * Returns an unmodifiable list of {@link OfflinePlayer}s that can currently see this boss bar.
	 * @return A list of {@link OfflinePlayer} viewers.
	 */
	public @NotNull List<OfflinePlayer> getViewers() {
		return viewers.stream().map(uuid -> Bukkit.getOfflinePlayer(uuid)).collect(Collectors.toUnmodifiableList());
	}
	
	/**
	 * A method to get whether or not a {@link OfflinePlayer} is viewing this boss bar.
	 * @param player The player to check
	 * @return Whether or not the offline player is viewing the boss bar.
	 */
	public boolean isViewing(OfflinePlayer player) {
		return viewers.contains(player.getUniqueId());
	}
	
	private BossBarTask(BarColor color, String title, BarStyle style) {
		targetBossBar = Bukkit.getServer().createBossBar(title, color, style);
	}
	
	public BossBarTask(BarColor color, String title, BarStyle style, LivingEntity target) {
		this(color, title, style);
		setTarget(target);
	}
	
	public BossBarTask(BarColor color, String title, BarStyle style, LivingEntity target, Overhealth o) {
		this(color, title, style, target);
		setTargetOverhealth(o);
	}
	
	private void update() {
		if(target.isDead()) {
			targetBossBar.setVisible(false);
			cancel();
		} else {
			double entityHealth = target.getHealth();
			double entityMaxHealth = target.getAttribute(Attribute.MAX_HEALTH).getValue();
			double progress = 0;
			if(targetOverhealth != null) {
				if(targetOverhealth.getOverhealth() > 0) {
					entityHealth += targetOverhealth.getOverhealth();
				}
				entityMaxHealth += targetOverhealth.getMaxOverhealth();
			}
			progress = (entityHealth / entityMaxHealth);
			targetBossBar.setProgress(progress);
			for(Player player : Bukkit.getOnlinePlayers()) {
				if(
					player.getLocation().distance(target.getLocation()) < 32 &&
					!viewers.contains(player.getUniqueId())
				) {
					targetBossBar.addPlayer(player);
					viewers.add(player.getUniqueId());
				} else if(
					player.getLocation().distance(target.getLocation()) > 32 &&
					viewers.contains(player.getUniqueId())
				) {
					targetBossBar.removePlayer(player);
					viewers.remove(player.getUniqueId());
				}
			}
		}
	}
	
	public void forceUpdate() {
		update();
	}
	
	@Override
	public void run() {
		update();
	}
}
