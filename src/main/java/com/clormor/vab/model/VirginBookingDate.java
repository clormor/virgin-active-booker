package com.clormor.vab.model;

public enum VirginBookingDate {
	TODAY, TODAY_PLUS_1, TODAY_PLUS_2, TODAY_PLUS_3, TODAY_PLUS_4, TODAY_PLUS_5, TODAY_PLUS_6, TODAY_PLUS_7;

	public static VirginBookingDate getBookingDate(int relativeToToday) {
		switch (relativeToToday) {
		case 0:
			return TODAY;
		case 1:
			return TODAY_PLUS_1;
		case 2:
			return TODAY_PLUS_2;
		case 3:
			return TODAY_PLUS_3;
		case 4:
			return TODAY_PLUS_4;
		case 5:
			return TODAY_PLUS_5;
		case 6:
			return TODAY_PLUS_6;
		case 7:
			return TODAY_PLUS_7;
		default:
			return null;
		}
	}
}
