package com.clormor.vab.model;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.clormor.vab.model.VirginTennisCourt.Surface;

public class VirginModel {

	public VirginBookingDate getBookingDate(DateTime date) {
		DateTime current = DateTime.now().withTimeAtStartOfDay();
		DateTime requestedDate = date.withTimeAtStartOfDay();
		Days daysBetween = Days.daysBetween(current, requestedDate);

		return VirginBookingDate.getBookingDate(daysBetween.getDays());
	}

	public Collection<VirginTennisCourt> getMatchingCourts(List<String> names,
			List<Surface> surfaces, List<Boolean> environment) {
		Collection<VirginTennisCourt> matchingCourts = EnumSet.copyOf(VirginTennisCourt.all);

		for (VirginTennisCourt court : matchingCourts) {
			
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
				if (! (names.contains(court.getName()) || names.contains(court.getName().toLowerCase()))) {
					matchingCourts.remove(court);
				}
			}
		}

		return matchingCourts;
	}
}
