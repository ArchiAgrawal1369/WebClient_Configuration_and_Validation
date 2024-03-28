package com.nagarro.MiniAssignment2.model;

import java.util.List;

public class NationalizeAPI {
	
    private List<Country> country;
    
	public List<Country> getCountry() {
		return country;
	}
	public void setCountry(List<Country> country) {
		this.country = country;
	}
	
	public static class Country {
        private String country_id;

		public String getCountry_id() {
			return country_id;
		}

		public void setCountry_id(String country_id) {
			this.country_id = country_id;
		}
        
	}
}
