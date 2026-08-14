package dev.cworldstar.anosf.impl.drugs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * The runnable class DrugThread requires the methods deserialize and serialize to be overwritten.
 * It will not give a default method, so without these methods it will fail on runtime.
 * 
 * See also
 * {@link dev.cworldstar.anosf.impl.drugs.drugs.threads.TestDrugThread}
 * for an example on how this works.
 * 
 * @author cworldstar
 */
public abstract class DrugThread extends BukkitRunnable implements ConfigurationSerializable {

	public static enum SeverityLevel {
		VERY_LOW(0),
		LOW(1),
		VISIBLE(2),
		VERY_VISIBLE(3),
		HARMFUL(4),
		VERY_HARMFUL(5);
		
		@Getter
		private int level = 0;
		
		SeverityLevel(int i) {
			level = i;
		}

		public static SeverityLevel fromLevel(int level) {
			switch(level) {
				case 0:
					return VERY_LOW;
				case 1:
					return LOW;
				case 2:
					return VISIBLE;
				case 3:
					return VERY_VISIBLE;
				case 4: 
					return HARMFUL;
				case 5:
					return VERY_HARMFUL;
				default:
					return VERY_HARMFUL;
			}
		}
		
		public boolean greaterThanOrEqual(SeverityLevel level) {
			return this.level >= level.getLevel();
		}
		
		public boolean lessThanOrEqual(SeverityLevel level) {
			return this.level <= level.getLevel();
		}
		
		public boolean greaterThan(SeverityLevel level) {
			return this.level > level.getLevel();
		}
		
		public boolean lessThan(SeverityLevel level) {
			return this.level < level.getLevel();
		}
	}
	
	public static enum TickTimers {
			DAY(1728000),
			HOUR(72000),
			MINUTE(1200),
			SECOND(20);

			private int ticks = 0;
		
			TickTimers(int i) {
				ticks = i;
			}
			
			/**
			 * 
			 * @param amount The amount of said timer to do. Example, {@code HOUR.ticks(2)} is equal to 2 hours.
			 * @return the amount of time in ticks.
			 */
			public int ticks(int amount) {
				return ticks * amount;
			}
			public int ticks() {
				return ticks;
			}
	}
	
	static {
		ConfigurationSerialization.registerClass(DrugThread.class);
	}
	
	/**
	 * Serializes this DrugThread into a string.
	 */
	@Override
	public String toString() {
		return "Owner: " + 
					owner.getName() + 
					" | tickDuration: " +
					String.valueOf(getTickDuration()) +
					" | maxDuration: " +
					String.valueOf(getMaxDuration()) +
					" | interrupted: " +
					String.valueOf(isInterrupt()) +
					" | frozen: " +
					String.valueOf(isFrozen()) +
					" | canonicalName: " +
					String.valueOf(getCanonicalName()) +
					" | severity: " + 
					getSeverity().toString();
	}
	
	/**
	 * Method to determine thread equality.
	 * @param thread The thread to compare
	 * @return Whether or not the given thread is similar to this thread.
	 */
	public boolean equals(DrugThread thread) {
		return (	
			canonicalName == thread.canonicalName &&
			tickDuration == thread.getTickDuration() &&
			maxDuration == thread.getMaxDuration() &&
			interrupt == thread.isInterrupt() &&
			frozen == thread.isFrozen() &&
			owner.equals(thread.getOwner())
		);
	}
	
	public void use() {
		usages += 1;
		
	}
	
	@Getter
	private SeverityLevel severity = SeverityLevel.VERY_LOW;
	
	@Getter
	private int usages= 0;
	@Getter
	private int ticks = 0;
	@Getter
	protected LivingEntity owner;
	@Getter
	private int tickDuration = 0;
	@Getter
	private int maxDuration = 0;
	@Getter
	private boolean interrupt = false;
	@Getter
	private boolean frozen = false;
	@Getter
	private String canonicalName = "";
	
	@Setter
	private Consumer<DrugThread> onRefresh = (thread) -> {
		
	};
	
	private List<Consumer<DrugThread>> onExpire = new ArrayList<Consumer<DrugThread>>();
	
	/**
	 * Adds a thread expire consumer to the list.
	 * @param expire A consumer to execute when this thread is expired.
	 */
	public void onThreadExpire(Consumer<DrugThread> expire) {
		onExpire.add(expire);
	}
	
	/**
	 * Refreshes this thread to its {@link #maxDuration}
	 */
	public void refresh() {
		tickDuration = maxDuration;
		usages += 1;
		tryIncreaseSeverity();
		onRefresh.accept(this);
	}
	
	public DrugThread(
			LivingEntity owner2,
			int duration,
			String name
	) {
		canonicalName = name;
		owner = owner2;
		tickDuration = duration;
		maxDuration = duration;
	}
	
	/**
	 * Interrupts this DrugThread. Interrupting a thread ends it early.
	 * @see {@link #freeze()} to pause without ending a thread.
	 */
	public void interrupt() {
		interrupt = true;
	}
	
	/**
	 * Freezes this DrugThread. Freezing a thread prevents it from running without cancelling it.
	 * @see {@link #interrupt()} to cancel and end a thread.
	 */
	public void freeze() {
		frozen = true;
	}
	
	/**
	 * This method is called every Runnable tick, however many runTaskLater is using.
	 */
	public abstract void tick();
	
	/**
	 * This method is called whenever the thread is refreshed. It will attempt to increase severity.
	 */
	public abstract void tryIncreaseSeverity();
	/**
	 * This method is called when the associated {@link DrugThread} is interrupted or completed.
	 */
	public abstract void expire();
	
	/**
	 * This method is called internally to handle onExpire hooks.
	 */
	private void triggerExpire() {
		onExpire.forEach((action)->{
			action.accept(this);
		});
		expire();
	}
	

	
	@SuppressWarnings("unchecked")
	public <T extends DrugThread> T deserialize(@Nonnull Map<String, Object> map) {
		try {
			Class<T> thread = (Class<T>) Class.forName((String) map.get("name"));
			return thread.getDeclaredConstructor(LivingEntity.class, String.class, Integer.TYPE, Integer.TYPE, SeverityLevel.class, Integer.TYPE).newInstance(
					(LivingEntity) map.get("owner"),
					(String) map.get("name"),
					(int) map.get("duration"),
					(int) map.get("maxduration"),
					SeverityLevel.valueOf((String) map.get("severity")),
					(int) map.get("usages")
			);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DrugThread(LivingEntity entity, String name, int duration, int maxDuration, SeverityLevel level, int usages) {
		this(entity, duration, name);
		this.maxDuration = maxDuration;
		severity = level;
		this.usages = usages;
	}
	
	@Override
	public @NotNull Map<String, Object> serialize() {
		return Map.of(
				"duration", getTickDuration(), 
				"owner", getOwner(), 
				"name", getCanonicalName(), 
				"maxduration", getMaxDuration(),
				"severity", getSeverity().toString(),
				"usages", getUsages()
		);
	}
	
	@Override
	public void run() {
		if(!isFrozen()) {
			if(isInterrupt() || tickDuration <= 0) {
				cancel();
				triggerExpire();
				return;
			}
			tickDuration -= 1;
			ticks += 1;
			tick();
		}
	}

	/**
	 * This method unfreezes a thread, allowing it
	 * to resume.
	 * @param owner The {@link LivingEntity} that owns this thread.
	 */
	public void unfreeze(LivingEntity owner) {
		this.owner = owner;
		frozen = false;
	}

	public void updateDuration(int intValue) {
		tickDuration = intValue;
	}

	public void updateMaxDuration(int intValue) {
		maxDuration = intValue;
	}

	public void raiseSeverity() {
		int newSeverity = severity.getLevel()+1;
		if(newSeverity > SeverityLevel.values().length) {
			// impossible, do not raise
			severity = SeverityLevel.VERY_HARMFUL;
			return;
		}
		severity = SeverityLevel.fromLevel(newSeverity);
	}
	
	public void lowerSeverity() {
		int newSeverity = severity.getLevel()-1;
		if(newSeverity < 0) {
			// end the thread
			interrupt();
			return;
		}
		severity = SeverityLevel.fromLevel(newSeverity);
	}
}
