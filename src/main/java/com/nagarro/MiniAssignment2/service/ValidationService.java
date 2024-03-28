package com.nagarro.MiniAssignment2.service;

import com.nagarro.MiniAssignment2.validators.EnglishAlphabetValidator;
import com.nagarro.MiniAssignment2.validators.NumericValidator;

public class ValidationService {

	public void validateNumbers(int... parameters) {
		for (int parameter : parameters) {
			if (NumericValidator.getInstance().validate(parameter)) {
				// It's a numeric parameter
			} else {
				// Invalid parameter, handle accordingly
				throw new IllegalArgumentException("Invalid num parameter: " + parameter);
			}
		}
	}

	public void validateAlphabets(String... parameters) {
		for (String parameter : parameters) {
			if (EnglishAlphabetValidator.getInstance().validate(parameter)) {
				// It's an alphabetic parameter
			} else {
				// Invalid parameter, handle accordingly
				throw new IllegalArgumentException("Invalid alpha parameter: " + parameter);
			}
		}
	}

}
