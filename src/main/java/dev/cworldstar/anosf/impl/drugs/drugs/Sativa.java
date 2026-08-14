package dev.cworldstar.anosf.impl.drugs.drugs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.cworldstar.anosf.impl.drugs.AbstractDrug;
import dev.cworldstar.anosf.impl.drugs.DrugProfile;
import dev.cworldstar.anosf.impl.drugs.DrugThread;
import dev.cworldstar.anosf.impl.drugs.drugs.threads.SativaThread;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.handlers.TickHandler;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;

public class Sativa extends AbstractDrug {

	public Sativa() {
		super(				
			ItemRegistry.getItemGroup("DRUG_CATEGORY"), 
			new ItemStackBuilder(Material.OAK_SAPLING)
				.setNameLerp("<gradient:green:dark_green:%phase%>Sativa</gradient>")
				.setName("<gradient:green:dark_green>Sativa</gradient>") // fallback name
				.setLore(new String[] {
						"",
				})
				.get(), 
			"SATIVA", 
			RecipeType.NULL, 
			null
		);
		addItemHandler(new TickHandler() {
			@Override
			public void onTick(SlimefunItem thisItem, Player p, ItemStack item, int slot) {
				//Bukkit.getLogger().log(Level.INFO, "updating item in slot " + String.valueOf(slot));
				//ItemEditorProtocol.updateItem(slot, item.clone(), p);
			}
		});
		addItemHandler(new ItemUseHandler() {
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				e.cancel();
				e.getItem().subtract(1);
				DrugProfile profile = DrugProfile.getProfile(e.getPlayer());
				profile.triggerThread(e.getPlayer(), DrugThread.TickTimers.HOUR.ticks(2) , SativaThread.class);
			}
		});
	}

}
