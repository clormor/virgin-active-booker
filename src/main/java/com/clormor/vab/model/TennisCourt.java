package com.clormor.vab.model;

import java.util.EnumSet;

public enum TennisCourt {

	COURT_1("1", Surface.CARPET, true), 
			COURT_2("2", Surface.CARPET, true),
			COURT_3("3", Surface.CARPET, true),
			COURT_4("4", Surface.CARPET, true),
			COURT_5("5", Surface.HARD, true),
			COURT_6("6", Surface.HARD, true),
			COURT_A("A", Surface.HARD, false),
			COURT_B("B", Surface.HARD, false),
			COURT_C("C", Surface.ARTIFICIAL_GRASS, false);

	public static final EnumSet<TennisCourt> all = EnumSet.of(COURT_1, COURT_2, COURT_3, COURT_4, COURT_5,COURT_6, COURT_A, COURT_B, COURT_C);
	
	private final String name;
	private final Surface surface;
	private final boolean indoor;

	TennisCourt(String name, Surface surface, boolean indoor) {
		this.name = name;
		this.surface = surface;
		this.indoor = indoor;
	}

	public String getName() {
		return name;
	}
	
	public Surface getSurface() {
		return surface;
	}
	
	public boolean isIndoor() {
		return indoor;
	}
	
	public enum Surface {
		CARPET, HARD, ARTIFICIAL_GRASS;
	}
}
