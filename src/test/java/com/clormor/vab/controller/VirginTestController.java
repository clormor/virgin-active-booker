package com.clormor.vab.controller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class VirginTestController extends HtmlUnitController {

	public VirginTestController(WebClient client, HtmlPage currentPage) {
		this.webClient = client;
		this.currentPage = currentPage;
	}
	
}
