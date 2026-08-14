package dev.cworldstar.anosf.entities;

import java.util.function.Supplier;

import org.bukkit.scheduler.BukkitRunnable;

import dev.cworldstar.anosf.ANOSF;
import lombok.Getter;

public class Skill {

	public enum SkillType {
		PASSIVE,
		ACTIVE,
		SEQUENCED;
	}

	
	private int timer = 20;
	@Getter
	private Supplier<BukkitRunnable> provider;
	@Getter
	private SkillType skillType;
	@Getter
	private boolean locked = false;
	public String id;
	
	public Skill(SkillType type, Supplier<BukkitRunnable> provider) {
		this.provider = provider;
		skillType = type;
	}
	
	public Skill(String id, SkillType type, Supplier<BukkitRunnable> provider) {
		this.id = id;
		this.provider = provider;
		skillType = type;
	}
	
	
	public Skill(SkillType passive, Supplier<BukkitRunnable> provider, int i) {
		this.provider = provider;
		skillType = passive;
		timer = i;
	}

	public SkillType getType() {
		return skillType;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		if(locked == true) {
			return;
		}
		this.id = id;
	}
	
	public void lock() {
		locked = true;
	}
	
	public void use() {
		
		switch(skillType) {
			case PASSIVE:
				provider.get().runTaskTimer(ANOSF.get(), 0, timer);
				break;
			case ACTIVE:
				try {
					provider.get().runTaskAsynchronously(ANOSF.get());
				} catch(Error e) {
					e.printStackTrace();
				}
				break;
			case SEQUENCED:
				/**
				 * 
				 * Sequenced is passive but it runs every five seconds and needs to be
				 * manually canceled.
				 * 
				 * @author cworldstar
				 */
				//provider.runTaskTimer(SFDrugs.getPlugin(SFDrugs.class), 0, 5L);
			default:
				throw new Error("invalid skill type");
		}
	}
}
