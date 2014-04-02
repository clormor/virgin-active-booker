package com.clormor.vab.view;

import com.clormor.vab.controller.IVirginController;

public class TestViewImpl extends CommandLineView {

	public TestViewImpl(IVirginController controller) {
		super("", "");
		this.controller = controller;
	}

}
