package com.clormor.vab.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.clormor.vab.model.VirginCourtBooking;
import com.clormor.vab.model.VirginTennisCourt;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class HtmlUnitControllerTest {

	private IVirginController testController;
	
	@Mock
	private WebClient client;
	
	@Mock
	private HtmlPage currentPage;
	
	@Mock
	private HtmlSubmitInput mockSubmit;
	
	@Mock
	private HtmlRadioButtonInput mockRadioButton;
	
	@Mock
	private HtmlSelect mockSelect;
	
	@Mock
	private HtmlOption option1;
	
	@Mock
	private HtmlOption option2;
	
	@Mock
	private HtmlOption option3;
	
	@Mock
	private DomElement mockElement1;
	
	@Mock
	private DomElement mockElement2;
	
	@Mock
	private DomElement mockElement3;
	
	@Mock
	private HtmlImageInput mockImageInput;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		testController = new VirginTestController(client, currentPage);
	}
	
	@Test
	public void testIndoorFilter() throws Exception {
		
		List<HtmlOption> options = Arrays.asList(option1, option2);
		when(currentPage.getElementById("rb_9_0")).thenReturn(mockRadioButton);
		when(mockRadioButton.click()).thenReturn(currentPage);
		when(client.waitForBackgroundJavaScript(HtmlUnitController.JS_TIMEOUT)).thenReturn(0);
		when(currentPage.getElementById("alb_5")).thenReturn(mockSelect);
		when(mockSelect.getOptions()).thenReturn(options);
		when(option1.getText()).thenReturn("Outdoor Court A");
		when(option2.getText()).thenReturn("Outdoor Court B");
		when(currentPage.getElementByName("rpProceed_b")).thenReturn(mockSubmit);
		when(mockSubmit.click()).thenReturn(currentPage);
		
		assertNull(testController.bookCourt(9, null, Arrays.asList(true)));
	}
	
	@Test
	public void testOutdoorFilter() throws Exception {
		
		List<HtmlOption> options = Arrays.asList(option1, option2);
		when(currentPage.getElementById("rb_9_0")).thenReturn(mockRadioButton);
		when(mockRadioButton.click()).thenReturn(currentPage);
		when(client.waitForBackgroundJavaScript(HtmlUnitController.JS_TIMEOUT)).thenReturn(0);
		when(currentPage.getElementById("alb_5")).thenReturn(mockSelect);
		when(mockSelect.getOptions()).thenReturn(options);
		when(option1.getText()).thenReturn("Indoor Court 1");
		when(option2.getText()).thenReturn("Indoor Court 2");
		when(currentPage.getElementByName("rpProceed_b")).thenReturn(mockSubmit);
		when(mockSubmit.click()).thenReturn(currentPage);
		
		assertNull(testController.bookCourt(9, null, Arrays.asList(false)));
	}
	
	@Test
	public void testCourtFilter() throws Exception {
		
		List<HtmlOption> options = Arrays.asList(option1, option2);
		when(currentPage.getElementById("rb_9_0")).thenReturn(mockRadioButton);
		when(mockRadioButton.click()).thenReturn(currentPage);
		when(client.waitForBackgroundJavaScript(HtmlUnitController.JS_TIMEOUT)).thenReturn(0);
		when(currentPage.getElementById("alb_5")).thenReturn(mockSelect);
		when(mockSelect.getOptions()).thenReturn(options);
		when(option1.getText()).thenReturn("Indoor Court 1");
		when(option2.getText()).thenReturn("Indoor Court 2");
		when(currentPage.getElementByName("rpProceed_b")).thenReturn(mockSubmit);
		when(mockSubmit.click()).thenReturn(currentPage);
		
		VirginTennisCourt bookedCourt = testController.bookCourt(9, Arrays.asList("2"), null);
		assertEquals(VirginTennisCourt.COURT_2, bookedCourt);
	}
	
	@Test
	public void testMyBookings() throws Exception {
		when(currentPage.getElementByName("btnViewMy")).thenReturn(mockImageInput);
		when(mockImageInput.click()).thenReturn(currentPage);
		
		HtmlPage newPage = testController.myBookings(currentPage);
		assertNotNull(newPage);
	}
	
	@Test (expected = ElementNotFoundException.class)
	public void testMyBookingsWrongPage() throws Exception {
		when(currentPage.getElementByName("btnViewMy")).thenThrow(new ElementNotFoundException("", "", ""));
		testController.myBookings(currentPage);
	}
	
	@Test
	public void testOneBooking() throws Exception {
		when(currentPage.getElementById("_ctl8_lblDate")).thenReturn(mockElement1);
		when(currentPage.getElementById("_ctl8_lblFrom")).thenReturn(mockElement2);
		when(currentPage.getElementById("_ctl8_lblRes")).thenReturn(mockElement3);
		when(mockElement1.getTextContent()).thenReturn("30/03/2014");
		when(mockElement2.getTextContent()).thenReturn("21:00");
		when(mockElement3.getTextContent()).thenReturn("Indoor Tennis Court 1");
		
		List<VirginCourtBooking> bookings = testController.getAllBookings(currentPage);
		assertEquals(1, bookings.size());
	}
}
