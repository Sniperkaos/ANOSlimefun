package dev.cworldstar.libs.cwlib.containers;

public class NumberContainer {
	private int number = 0;
	public NumberContainer() {
		
	}
	public void increment() {
		this.number += 1;
	}
	public int asInteger() {
		return number;
	}
}
