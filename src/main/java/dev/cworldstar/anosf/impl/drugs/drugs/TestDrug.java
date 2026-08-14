package dev.cworldstar.anosf.impl.drugs.drugs;

import org.bukkit.Material;
import dev.cworldstar.anosf.impl.drugs.AbstractDrug;
import dev.cworldstar.anosf.impl.drugs.DrugProfile;
import dev.cworldstar.anosf.impl.drugs.drugs.threads.TestDrugThread;
import dev.cworldstar.libs.cwlib.ItemRegistry;
import dev.cworldstar.libs.cwlib.builders.ItemStackBuilder;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;

public class TestDrug extends AbstractDrug {
	public TestDrug() {
		//TODO: complete recipes
		super(
				ItemRegistry.getItemGroup("DRUG_CATEGORY"), 
				new ItemStackBuilder(Material.SUGAR)
					.setName("<gradient:red:gold>Test Drug</gradient>")
					.get(), 
				"TEST_DRUG", 
				RecipeType.NULL, 
				null
		);
		addItemHandler(new ItemUseHandler() {
			@Override
			public void onRightClick(PlayerRightClickEvent e) {
				e.cancel();
				e.getItem().subtract(1);
				DrugProfile profile = DrugProfile.getProfile(e.getPlayer());
				profile.triggerThread(e.getPlayer(), 10, TestDrugThread.class);
			}
		});
	}
}
