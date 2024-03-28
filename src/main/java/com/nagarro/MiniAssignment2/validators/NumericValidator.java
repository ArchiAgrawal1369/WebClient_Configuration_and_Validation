package com.nagarro.MiniAssignment2.validators;

public class NumericValidator {
	private static final NumericValidator instance = new NumericValidator();

	private NumericValidator() {
	}

	public static NumericValidator getInstance() {
		return instance;
	}

	public boolean validate(int parameter) {
		try {
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}