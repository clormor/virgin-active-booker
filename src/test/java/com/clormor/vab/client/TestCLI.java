package com.clormor.vab.client;

import org.apache.commons.cli.ParseException;

import com.clormor.vab.view.CommandLineView;

public class TestCLI extends VirginCLI {

	public TestCLI(String[] args, CommandLineView view) throws ParseException {
		super(args);
		this.view = view;
	}
}
