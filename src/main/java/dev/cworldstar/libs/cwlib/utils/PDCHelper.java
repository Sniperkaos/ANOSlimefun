package dev.cworldstar.libs.cwlib.utils;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.base.Defaults;

import dev.cworldstar.anosf.ANOSF;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;
import io.papermc.paper.persistence.PersistentDataContainerView;

public class PDCHelper {
	public static @Nullable Optional<PersistentDataContainer> getPDC(@Nonnull ItemMeta meta) {
		Validate.notNull(meta, "The ItemMeta cannot be null.");
		if(meta == null) {
			return Optional.empty();
		}
		return Optional.of(meta.getPersistentDataContainer());
	}
	
	public static void setPDC(ItemMeta meta, ItemStack i) {
		i.setItemMeta(meta);
	}

	public static <T> T getPrimitive(ItemStack item, String id, PersistentDataType<T, T> pdt) {
		@NotNull PersistentDataContainerView pdc = item.getPersistentDataContainer();
		return pdc.getOrDefault(
				ANOSF.key(id), 
				pdt, 
				Defaults.defaultValue(pdt.getPrimitiveType())
		);
	}
	
	public static @Nullable <T, C> C getComplex(ItemStack item, String id, PersistentDataType<T, C> pdt) {
		@NotNull PersistentDataContainerView pdc = item.getPersistentDataContainer();
		return pdc.getOrDefault(
				ANOSF.key(id), 
				pdt, 
				Defaults.defaultValue(pdt.getComplexType())
		);
	}
	
	
	public static <T> void set(ItemStack item, String id, PersistentDataType<T, T> pdt, T value) {
		@NotNull ItemMeta meta = item.getItemMeta();
		if(meta == null) {
			meta = Bukkit.getItemFactory().getItemMeta(item.getType());
		}
		@NotNull PersistentDataContainer pdc = meta.getPersistentDataContainer();
		pdc.set(ANOSF.key(id), pdt, value);
		item.setItemMeta(meta);
	}
}
