package com.clormor.vab.view;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.clormor.vab.controller.IVirginController;
import com.clormor.vab.model.VirginTennisCourt;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CommandLineViewTest {

	private ICommandLineView testView;

	@Mock
	private IVirginController mockController;

	@Mock
	private HtmlPage mockPage;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		testView = new TestViewImpl(mockController);
	}

	@Test
	public void test_date_rounded_correctly() throws Exception {
		DateTime now = DateTime.now();

		when(mockController.login(anyString(), anyString())).thenReturn(mockPage);
		DateTime justBeforeMidnight = now.dayOfMonth().roundCeilingCopy()
				.minusSeconds(1);

		doNothing().when(mockController).newCourtBooking(any(HtmlPage.class),any(DateTime.class));
		when(mockController.bookCourt(anyInt(), anyListOf(String.class), anyListOf(Boolean.class))).thenReturn(VirginTennisCourt.COURT_1);
		
		String message = testView.bookCourts(justBeforeMidnight, 11, null, null);
		assertNotNull(message);
		
		String expectedDate = new SimpleDateFormat("EEE, MMM d").format(now.toDate());
		assertTrue(message.contains(expectedDate));
		System.out.println(message);
	}
}
