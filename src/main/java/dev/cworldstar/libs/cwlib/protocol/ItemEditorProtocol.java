package dev.cworldstar.libs.cwlib.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;

import dev.cworldstar.libs.cwlib.AbstractSFAddon;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import net.kyori.adventure.text.Component;

public class ItemEditorProtocol {
	
	protected static final List<BiFunction<ItemStack, Player, ItemStack>> passthroughs = new ArrayList<BiFunction<ItemStack, Player, ItemStack>>();
	
	
	/**
	 * 
	 * @param passthrough Adds a {@link BiFunction} passthrough handler to the LoreEditorProtocol. The BiFunction is Nullable.
	 * @return
	 */
	
	public static void passthrough(BiFunction<ItemStack, Player, ItemStack> passthrough) {
		passthroughs.add(passthrough);
	}
	
	protected static ItemStack passthrough(Player p, ItemStack item) {
		 for(BiFunction<ItemStack, Player, ItemStack> passthrough : passthroughs) {
			 passthrough.apply(item, p);
		 }
		 return item;
	}
	
	public static void updateItem(int slot, ItemStack newItem, Player who) {
		passthrough(who, newItem);
		PacketContainer container = new PacketContainer(PacketType.Play.Server.SET_SLOT);
		StructureModifier<Integer> intModifiers = container.getIntegers();
		intModifiers.writeSafely(0, -2); //-- window ID
		intModifiers.writeSafely(1, 1); //-- update ID
		intModifiers.writeSafely(2, slot); // slot ID
		StructureModifier<ItemStack> itemModifiers = container.getItemModifier();
		itemModifiers.writeSafely(0, newItem); //-- item, wrapper for Slot Data
		ProtocolLibrary.getProtocolManager().sendServerPacket(who, container);
	}
	
	public static void updateItem(ItemStack tool, @NotNull Player player) {
		int slot = 0;
		for(ItemStack item : player.getInventory().getContents()) {
			if(item == null) {
				slot +=1; continue;
			}
			
			if(item.isSimilar(tool)) {
				updateItem(slot, tool, player);
				break;
			}
		}
	}
	
	public static void start() {
		// this passthrough handles custom durability
		passthrough((item, player) -> {
			ItemMeta meta = item.getItemMeta();
			if(meta == null) {
				return item;
			}
			PersistentDataContainer pdc = meta.getPersistentDataContainer();
			if(pdc.has(AbstractSFAddon.key("DURABILITY"))) {
				long durability = pdc.get(AbstractSFAddon.key("DURABILITY"), PersistentDataType.LONG);
				long maxDurability = pdc.get(AbstractSFAddon.key("MAX_DURABILITY"), PersistentDataType.LONG);
				 
				List<Component> lore = meta.lore();
				lore.add(FormatUtils.asText(" "));
				lore.add(FormatUtils.asText("&fDurability: " + String.valueOf(durability) + "/" + String.valueOf(maxDurability)));
				meta.lore(lore);
			} else if(meta.isUnbreakable()) {
				List<Component> lore;
				if(!meta.hasLore()) {
					lore = new ArrayList<Component>();
				} else {
					lore = meta.lore();
				}
				lore.add(FormatUtils.asText(" "));
				lore.add(FormatUtils.mm("<white>Durability</white><gray>:</gray> " + "<gradient:gold:yellow:#696969>Infinite</gradient>"));
				meta.lore(lore);
			}
			item.setItemMeta(meta);
			return item;
		});
		
		ProtocolLibrary.getProtocolManager().addPacketListener(
					new PacketAdapter(PacketAdapter.params()
						.plugin(AbstractSFAddon.get())
						.listenerPriority(ListenerPriority.HIGH)
						.types(PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS)) 
					{
						@Override
						public void onPacketSending(PacketEvent event) {
							PacketContainer container = event.getPacket();
							PacketType type = event.getPacketType();
							Player p = event.getPlayer();
							
							if(p.getGameMode() == GameMode.CREATIVE) {
								return;
							}
							
							if(type == PacketType.Play.Server.SET_SLOT) {
								 StructureModifier<ItemStack> modifier = container.getItemModifier();
								 if(modifier.size() > 0) {
									 ItemStack item = modifier.readSafely(0).clone();
									 passthrough(p, item);
									 modifier.writeSafely(0, item);
									 event.setPacket(container);
								 }
							} else {
								 StructureModifier<List<ItemStack>> modifier = container.getItemListModifier();
								 if(modifier.size() > 0) {
									 List<ItemStack> items = modifier.readSafely(0); 
									 items = items.stream().map(item -> passthrough(p, item)).collect(Collectors.toList());
									 modifier.writeSafely(0, items);
									 event.setPacket(container);
								 }
							}
						}
					}			
			);
	}
}
