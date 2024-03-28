package com.nagarro.MiniAssignment2.validators;

public class EnglishAlphabetValidator {
	private static final EnglishAlphabetValidator instance = new EnglishAlphabetValidator();

	private EnglishAlphabetValidator() {
	}

	public static EnglishAlphabetValidator getInstance() {
		return instance;
	}

	public boolean validate(String value) {
		if (value.matches("^[a-zA-Z]+$")) {
			return true;
		} else {
			return false;
		}
	}

}
