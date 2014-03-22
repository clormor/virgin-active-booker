package com.clormor.vab.controller;

import static org.junit.Assert.assertNull;
<<<<<<< HEAD
=======
import static org.junit.Assert.assertEquals;
>>>>>>> release/0.3.0
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

<<<<<<< HEAD
=======
import com.clormor.vab.model.VirginTennisCourt;
>>>>>>> release/0.3.0
import com.gargoylesoftware.htmlunit.WebClient;
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
<<<<<<< HEAD
		when(mockSelect.getSelectedOptions()).thenReturn(options);
=======
		when(mockSelect.getOptions()).thenReturn(options);
>>>>>>> release/0.3.0
		when(option1.getText()).thenReturn("Outdoor Court A");
		when(option2.getText()).thenReturn("Outdoor Court B");
		when(currentPage.getElementByName("rpProceed_b")).thenReturn(mockSubmit);
		when(mockSubmit.click()).thenReturn(currentPage);
		
<<<<<<< HEAD
		assertNull(testController.bookCourt(9, Arrays.asList(true)));
=======
		assertNull(testController.bookCourt(9, null, Arrays.asList(true)));
>>>>>>> release/0.3.0
	}
	
	@Test
	public void testOutdoorFilter() throws Exception {
		
		List<HtmlOption> options = Arrays.asList(option1, option2);
		when(currentPage.getElementById("rb_9_0")).thenReturn(mockRadioButton);
		when(mockRadioButton.click()).thenReturn(currentPage);
		when(client.waitForBackgroundJavaScript(HtmlUnitController.JS_TIMEOUT)).thenReturn(0);
		when(currentPage.getElementById("alb_5")).thenReturn(mockSelect);
<<<<<<< HEAD
		when(mockSelect.getSelectedOptions()).thenReturn(options);
=======
		when(mockSelect.getOptions()).thenReturn(options);
>>>>>>> release/0.3.0
		when(option1.getText()).thenReturn("Indoor Court 1");
		when(option2.getText()).thenReturn("Indoor Court 2");
		when(currentPage.getElementByName("rpProceed_b")).thenReturn(mockSubmit);
		when(mockSubmit.click()).thenReturn(currentPage);
		
<<<<<<< HEAD
		assertNull(testController.bookCourt(9, Arrays.asList(false)));
=======
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
>>>>>>> release/0.3.0
	}
}
