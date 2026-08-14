package dev.cworldstar.anosf.items.materials;

import org.bukkit.Material;
import dev.cworldstar.anosf.items.Items.ItemTier;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class FallenSpark extends Particle {

	public FallenSpark() {
		super("FALLEN_SPARK_PARTICLE", Material.GLASS_BOTTLE, ItemTier.STRANGE,"<gradient:dark_purple:gray>Fallen Spark</gradient>", "Material/Particle", new String[] {
				"",
				"<gradient:white:gold>\"The lifeblood of a god... I suppose- as all things do,",
				"<gradient:white:gold>\"even cosmic deities may perish.\"",
				"<gray> -Unknown, 21XX"
			}, RecipeType.NULL, null);
	}

}
