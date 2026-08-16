package dev.cworldstar.libs.cwlib.impl.breathing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import dev.cworldstar.libs.cwlib.impl.SlimefunGas;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class FilterProperties {
	
	@AllArgsConstructor
	public static class FilterProperty {
		private @Getter SlimefunGas gas;
		private @Getter float filterEffectiveness;
	}
	
	private ArrayList<FilterProperty> filterProperties;
	
	public FilterProperties(FilterProperty...filterProperties) {
		this.filterProperties = new ArrayList<FilterProperty>(Arrays.asList(filterProperties));
	}
	
	public float filters(SlimefunGas gas) {
		Optional<FilterProperty> property = filterProperties.stream().filter(prop -> prop.getGas().equals(gas)).findFirst();
		if(property.isEmpty()) {
			return 0.0f;
		}
		return property.get().getFilterEffectiveness();
	}
	
}
