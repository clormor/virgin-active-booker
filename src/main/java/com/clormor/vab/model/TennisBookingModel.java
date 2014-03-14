package com.clormor.vab.model;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.clormor.vab.model.TennisCourt.Surface;

public class TennisBookingModel {

	public VirginActiveBookingDate getBookingDate(DateTime date) {
		DateTime current = DateTime.now().withTimeAtStartOfDay();
		DateTime requestedDate = date.withTimeAtStartOfDay();
		Days daysBetween = Days.daysBetween(current, requestedDate);

		return VirginActiveBookingDate.getBookingDate(daysBetween.getDays());
	}

	public Collection<TennisCourt> getMatchingCourts(List<String> names,
			List<Surface> surfaces, List<Boolean> environment) {
		Collection<TennisCourt> matchingCourts = EnumSet.copyOf(TennisCourt.all);

		for (TennisCourt court : matchingCourts) {
			
			// filter out indoor/outdoor
			if (environment != null && environment.size() > 0) {
				if (!environment.contains(court.isIndoor())) {
					matchingCourts.remove(court);
				}
			}
			
			// filter surface
			if (surfaces != null && surfaces.size() > 0) {
				if (!surfaces.contains(court.getSurface())) {
					matchingCourts.remove(court);
				}
			}
			
			// filter name
			if (names != null && names.size() > 0) {
				if (!names.contains(court.getName())) {
					matchingCourts.remove(court);
				}
			}
		}

		return matchingCourts;
	}
}
