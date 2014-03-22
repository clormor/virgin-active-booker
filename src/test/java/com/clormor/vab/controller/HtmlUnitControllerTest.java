package com.clormor.vab.controller;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
		when(mockSelect.getSelectedOptions()).thenReturn(options);
		when(option1.getText()).thenReturn("Outdoor Court A");
		when(option2.getText()).thenReturn("Outdoor Court B");
		when(currentPage.getElementByName("rpProceed_b")).thenReturn(mockSubmit);
		when(mockSubmit.click()).thenReturn(currentPage);
		
		assertNull(testController.bookCourt(9, Arrays.asList(true)));
	}
	
}
