package dev.cworldstar.libs.cwlib.builders;

import org.bukkit.Material;

import dev.cworldstar.libs.cwlib.utils.SkullCreator;

public class PlayerHeadBuilder extends ItemStackBuilder {

	public PlayerHeadBuilder() {
		super(Material.PLAYER_HEAD);
	}
	
	public PlayerHeadBuilder texture(String texture) {
		SkullCreator.loadBase64(item, texture);
		return this;
	}

}
