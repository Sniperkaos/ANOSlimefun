package dev.cworldstar.libs.cwlib.impl.multiblock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;
import lombok.NonNull;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class MultiblockChoice {
	private List<String> choices = new ArrayList<String>();
	private @Nullable Color errorColor = Color.RED;
	public MultiblockChoice(@Nullable Color errorColor, String... all) {
		this.errorColor = errorColor;
		choices.addAll(Arrays.asList(all));
	}
	
	@Override
	public String toString() {
		String string = "[";
		for(String choice : choices) {
			string += choice + ",";
		}
		string += "]";
		return string;
	}
	
	public boolean resolve(@NonNull Location loc) {
		Validate.notNull(loc, "You cannot resolve a multiblock without a location!");
		Block b = loc.getBlock();
		SlimefunItem potentialSFBlock = BlockStorage.check(loc);
		if(potentialSFBlock != null) {
			return choices.contains(potentialSFBlock.getId());
		} else {
			return choices.contains(b.getType().toString());
		}
	}		

	
	public Component[] error(Location loc, boolean text) {
		ArrayList<Component> components = new ArrayList<Component>();
		Vector vec = loc.toVector();
		if(errorColor != null) {
			loc.getWorld().spawnParticle(Particle.DUST, loc.add(0.5, 0, 0.5), 4, new DustOptions(errorColor, 4));
		}
		if(text) {
			components.add(FormatUtils.mm("".concat("at " + "[" + String.valueOf(vec.getBlockX()) + "; " + String.valueOf(vec.getBlockY()) + "; " + String.valueOf(vec.getBlockZ()) + "]")));
			components.add(
					FormatUtils.mm(", expected ")
					.append(
							FormatUtils.mm(
									"<color:" + String.valueOf(TextColor.color(errorColor.getRed(), errorColor.getBlue(), errorColor.getGreen()).asHexString()).replace("0x", "") + ">"+ choices.toString() + "<gray>"
							)
					).append(
							FormatUtils.mm(
									", got " + 
											((BlockStorage.check(loc) != null) ? BlockStorage.check(loc).getId() : loc.getBlock().getType().toString())
							)
					)
			);
		}
		return components.toArray(Component[]::new);
	}
}
