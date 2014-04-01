package com.clormor.vab.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
	private DomElement mockElement4;
	
	@Mock
	private DomElement mockElement5;
	
	@Mock
	private DomElement mockElement6;
	
	@Mock
	private DomElement mockElement7;
	
	@Mock
	private DomElement mockElement8;
	
	@Mock
	private DomElement mockElement9;

	@Mock
	private DomElement mockElement10;
	
	@Mock
	private DomElement mockElement11;
	
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
		when(client.waitForBackgroundJavaScript(anyInt())).thenReturn(0);
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
		when(client.waitForBackgroundJavaScript(anyInt())).thenReturn(0);
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
		when(client.waitForBackgroundJavaScript(anyInt())).thenReturn(0);
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
		List<DomElement> bookings = Arrays.asList(mockElement1);
		List<DomElement> standardHeader = Arrays.asList(mockElement2);
		List<DomElement> date1 = Arrays.asList(mockElement3);
		List<DomElement> time1 = Arrays.asList(mockElement4);
		List<DomElement> court1 = Arrays.asList(mockElement5);
		
		doReturn(bookings).when(currentPage).getByXPath("/html/body/form/table/tbody/tr/td/table/tbody/tr/td[@id='tcGrid']/ul");
		
		doReturn(standardHeader).when(mockElement1).getByXPath("li[@class='ul_pnl_h']/span[1]");
		
		when(mockElement2.getTextContent()).thenReturn("Tennis Court");
		
		doReturn(date1).when(mockElement1).getByXPath("li[3]/span[2]");
		doReturn(time1).when(mockElement1).getByXPath("li[3]/span[4]");
		
		when(mockElement3.getTextContent()).thenReturn("28/03/2014");
		when(mockElement4.getTextContent()).thenReturn("11:00");
		
		doReturn(court1).when(mockElement1).getByXPath("li[@class='ul_pnl_rowb']/span[2]");
		
		when(mockElement5.getTextContent()).thenReturn("Indoor Court 3");
		
		List<VirginCourtBooking> allBookings = testController.getAllBookings(currentPage);
		assertEquals(1, allBookings.size());
		assertEquals(VirginTennisCourt.COURT_3, allBookings.get(0).getCourt());
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
		DateTime dateTime1 = formatter.parseDateTime("28/03/2014 11:00");
		
		assertEquals(dateTime1, allBookings.get(0).getBookingTime());
	}
	
	@Test
	public void testNoBookings() throws Exception {
		when(currentPage.getByXPath("/html/body/form/table/tbody/tr/td/table/tbody/tr/td[@id='tcGrid']/ul")).thenReturn(null);
		
		List<VirginCourtBooking> bookings = testController.getAllBookings(currentPage);
		assertEquals(0, bookings.size());
	}
	
	@Test
	public void testMultipleBookings() throws Exception {
		List<DomElement> bookings = Arrays.asList(mockElement1, mockElement2, mockElement3);
		List<DomElement> standardHeader = Arrays.asList(mockElement4);
		List<DomElement> nonStandardHeader = Arrays.asList(mockElement5);
		List<DomElement> date1 = Arrays.asList(mockElement6);
		List<DomElement> time1 = Arrays.asList(mockElement7);
		List<DomElement> date2 = Arrays.asList(mockElement8);
		List<DomElement> time2 = Arrays.asList(mockElement9);
		List<DomElement> court1 = Arrays.asList(mockElement10);
		List<DomElement> court2 = Arrays.asList(mockElement11);
		
		doReturn(bookings).when(currentPage).getByXPath("/html/body/form/table/tbody/tr/td/table/tbody/tr/td[@id='tcGrid']/ul");
		
		doReturn(standardHeader).when(mockElement1).getByXPath("li[@class='ul_pnl_h']/span[1]");
		doReturn(standardHeader).when(mockElement2).getByXPath("li[@class='ul_pnl_h']/span[1]");
		doReturn(nonStandardHeader).when(mockElement3).getByXPath("li[@class='ul_pnl_h']/span[1]");
		
		when(mockElement4.getTextContent()).thenReturn("Tennis Court");
		when(mockElement5.getTextContent()).thenReturn("Something Else");
		
		doReturn(date1).when(mockElement1).getByXPath("li[3]/span[2]");
		doReturn(time1).when(mockElement1).getByXPath("li[3]/span[4]");
		doReturn(date2).when(mockElement2).getByXPath("li[3]/span[2]");
		doReturn(time2).when(mockElement2).getByXPath("li[3]/span[4]");
		
		when(mockElement6.getTextContent()).thenReturn("20/03/2014");
		when(mockElement7.getTextContent()).thenReturn("09:00");
		when(mockElement8.getTextContent()).thenReturn("22/03/2014");
		when(mockElement9.getTextContent()).thenReturn("20:00");
		
		doReturn(court1).when(mockElement1).getByXPath("li[@class='ul_pnl_rowb']/span[2]");
		doReturn(court2).when(mockElement2).getByXPath("li[@class='ul_pnl_rowb']/span[2]");
		
		when(mockElement10.getTextContent()).thenReturn("Indoor Court 2");
		when(mockElement11.getTextContent()).thenReturn("Outdoor Court A");
		
		List<VirginCourtBooking> allBookings = testController.getAllBookings(currentPage);
		assertEquals(2, allBookings.size());
		assertEquals(VirginTennisCourt.COURT_2, allBookings.get(0).getCourt());
		assertEquals(VirginTennisCourt.COURT_A, allBookings.get(1).getCourt());
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
		DateTime dateTime1 = formatter.parseDateTime("20/03/2014 09:00");
		DateTime dateTime2 = formatter.parseDateTime("22/03/2014 20:00");
		
		assertEquals(dateTime1, allBookings.get(0).getBookingTime());
		assertEquals(dateTime2, allBookings.get(1).getBookingTime());
	}
}
