package com.clormor.vab.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.clormor.vab.model.TennisCourt.Surface;

public class TennisBookingModelTest {

	private static final int TOTAL_NUM_COURTS = TennisCourt.values().length;
	
	private TennisBookingModel testModel;
	
	@Before
	public void setup() {
		testModel = new TennisBookingModel();
	}
	
	@Test
	public void testTodaysDate() {
		assertEquals(VirginActiveBookingDate.TODAY, testModel.getBookingDate(DateTime.now()));
	}
	
	@Test
	public void testTomorrowsDate() {
		assertEquals(VirginActiveBookingDate.TODAY_PLUS_1, testModel.getBookingDate(DateTime.now().plusDays(1)));
	}
	
	@Test
	public void testYesterdaysDate() {
		assertNull(testModel.getBookingDate(DateTime.now().minusDays(1)));
	}

	@Test
	public void testMaxRangeDate() {
		assertEquals(VirginActiveBookingDate.TODAY_PLUS_7, testModel.getBookingDate(DateTime.now().plusDays(7)));
	}
	
	@Test
	public void testOutOfRangeDate() {
		assertNull(testModel.getBookingDate(DateTime.now().plusDays(8)));
	}
	
	@Test
	public void testGetAnyCourt() {
		assertEquals(TOTAL_NUM_COURTS, testModel.getMatchingCourts(null, null, null).size());
	}
	
	@Test
	public void testCourtEnvironmentMatch() {
		// inside courts
		assertEquals(6, testModel.getMatchingCourts(null, null, Arrays.asList(true)).size());
		// outside courts
		assertEquals(3, testModel.getMatchingCourts(null, null, Arrays.asList(false)).size());
		// inside and outside courts (all courts)
		assertEquals(TOTAL_NUM_COURTS, testModel.getMatchingCourts(null, null, Arrays.asList(false, true)).size());
		// empty list (all courts)
		assertEquals(TOTAL_NUM_COURTS, testModel.getMatchingCourts(null, null, new ArrayList<Boolean>()).size());
	}
	

	@Test
	public void testCourtSurfaceMatch() {
		// carpet courts
		assertEquals(4, testModel.getMatchingCourts(null, Arrays.asList(Surface.CARPET), null).size());
		// carpet and hard courts
		assertEquals(8, testModel.getMatchingCourts(null, Arrays.asList(Surface.CARPET, Surface.HARD), null).size());
		// all surfaces
		assertEquals(TOTAL_NUM_COURTS, testModel.getMatchingCourts(null, Arrays.asList(Surface.values()), null).size());
		// empty list (all courts)
		assertEquals(TOTAL_NUM_COURTS, testModel.getMatchingCourts(null, new ArrayList<Surface>(), null).size());
	}
	
	@Test
	public void testCourtNameMatch() {
		// court 1
		assertEquals(1, testModel.getMatchingCourts(Arrays.asList("1"), null, null).size());
		// court 1 or court A
		assertEquals(2, testModel.getMatchingCourts(Arrays.asList("1", "A"), null, null).size());
		// court a, b, c mixed case
		assertEquals(3, testModel.getMatchingCourts(Arrays.asList("a", "A", "B", "c"), null, null).size());
		// empty list (all courts)
		assertEquals(TOTAL_NUM_COURTS, testModel.getMatchingCourts(new ArrayList<String>(), null, null).size());
		// empty string (no courts)
		assertEquals(0, testModel.getMatchingCourts(Arrays.asList(""), null, null).size());
	}
	
	@Test
	public void testCourtMatchCombos() {
		// all inside courts
		assertEquals(6, testModel.getMatchingCourts(new ArrayList<String>(), Arrays.asList(Surface.values()), Arrays.asList(true)).size());
		// court a is not inside
		assertEquals(0, testModel.getMatchingCourts(Arrays.asList("a"), null, Arrays.asList(true)).size());
		// court a is not inside
		assertEquals(2, testModel.getMatchingCourts(null, Arrays.asList(Surface.HARD), Arrays.asList(false)).size());
	}
}
