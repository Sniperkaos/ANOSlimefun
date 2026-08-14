package dev.cworldstar.anosf.svoltz;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import org.bukkit.inventory.ItemStack;
import dev.cworldstar.libs.cwlib.utils.PDCHelper;

public interface SVContainer {
	/**
	 * "getPower()" method
	 * @param item
	 * @return
	 */
	public default BigInteger getVoltz(ItemStack item) {
		return PDCHelper.getComplex(item, "SV", SVoltz.BIDataType);
	}
	
	public abstract double getResistance(ItemStack item);
	
	public default BigDecimal getVoltage(ItemStack item) {
		return new BigDecimal(getVoltz(item)).multiply(BigDecimal.valueOf(getResistance(item))).sqrt(MathContext.UNLIMITED);
	}
}
