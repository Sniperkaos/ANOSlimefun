package dev.cworldstar.anosf.impl.drugs.items;

import java.util.Map.Entry;

import org.bukkit.Material;
import dev.cworldstar.anosf.impl.drugs.AbstractDrug;
import dev.cworldstar.anosf.impl.drugs.DrugProfile;
import dev.cworldstar.anosf.impl.drugs.DrugThread;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import dev.cworldstar.libs.cwlib.utils.FormatUtils;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;

public class Narcan extends AbstractDrug {

	public Narcan() {
		super(				
				ItemRegistry.getItemGroup("DRUG_CATEGORY"), 
				new ItemStackBuilder(Material.OMINOUS_BOTTLE)
					.setName("<gradient:blue:white>Narcan</gradient>") // fallback name
					.setLore(new String[] {
							"",
					})
					.get(), 
				"NARCAN", 
				RecipeType.NULL, 
				null
			);
		addItemHandler(new ItemUseHandler() {
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				e.cancel();
				e.getItem().subtract();
				e.getPlayer().sendMessage(FormatUtils.mm("<gradient:gray:white>You feel the effects of your overdose drugs lessen.</gradient>"));
				DrugProfile profile = DrugProfile.getProfile(e.getPlayer());
				for(Entry<String, DrugThread> drug : profile.getThreads().entrySet()) {
					DrugThread thread = drug.getValue();
					thread.lowerSeverity();
				}
			}
		});
	}

}
