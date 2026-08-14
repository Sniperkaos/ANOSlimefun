package dev.cworldstar.anosf.guide;

import java.util.ArrayList;
import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import dev.cworldstar.anosf.ANOSF;
import dev.cworldstar.libs.cwlib.builders.PlayerHeadBuilder;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.guide.options.SlimefunGuideOption;
import io.github.thebusybiscuit.slimefun4.core.guide.options.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import net.kyori.adventure.text.Component;

public class MultiblockChatOption implements SlimefunGuideOption<Boolean> {
	
	private static ItemStack DISPLAY_ITEM = new PlayerHeadBuilder()
			.texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2QwMDA3MzExZjY1ODk0NTg2Nzg4NDk0NTcyMmE0MzBjNTI3ZjMyMjNiOTA5NjEyZTQ1ZDAzMjA4OTZlYWU5YSJ9fX0=")
			.build();
	
	@Override
	public @NotNull NamespacedKey getKey() {
		return ANOSF.key("MULTIBLOCK_CHAT_OPTION");
	}

	@Override
	public SlimefunAddon getAddon() {
		return ANOSF.get();
	}

	@Override
	public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        boolean enabled = getSelectedOption(p, guide).orElse(true);
        String optionState = enabled ? "<green>enabled<gray>" : "<red>disabled<gray>";
        ItemStack item = DISPLAY_ITEM.clone();
        item.editMeta(meta -> {
        	meta.displayName(FormatUtils.mm("<gray>Multiblock Chat Spam: " + optionState + "."));
            ArrayList<Component> lore = new ArrayList<Component>();
            lore.add(FormatUtils.empty());
            lore.add(FormatUtils.mm("<gray>Click to enable whether or not"));
            lore.add(FormatUtils.mm("<gray>multiblock structures will print errors"));
           	lore.add(FormatUtils.mm("<gray>to your chat."));
            lore.add(FormatUtils.empty());
            lore.add(FormatUtils.mm("<gray>↳ <yellow>Click to toggle."));
            meta.lore(lore);
        });   
        return Optional.of(item);
	}

	@Override
	public void onClick(Player p, ItemStack guide) {
        setSelectedOption(p, guide, !getSelectedOption(p, guide).orElse(true));
        SlimefunGuideSettings.openSettings(p, guide);
	}

	@Override
	public Optional<Boolean> getSelectedOption(Player p, ItemStack guide) {
        NamespacedKey key = getKey();
        boolean value = !PersistentDataAPI.hasByte(p, key) || PersistentDataAPI.getByte(p, key) == (byte) 1;
        return Optional.of(value);
	}

	@Override
	public void setSelectedOption(Player p, ItemStack guide, Boolean value) {
        PersistentDataAPI.setByte(p, getKey(), value.booleanValue() ? (byte) 1 : (byte) 0);
	}
}
