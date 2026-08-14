package dev.cworldstar.anosf.events;

public class EventRegistry {
	public static void complete() {
		new PlayerAddedEvent();
		new PlayerLeavingEvent();
		new EntityKilledEvent();
	}
}
