package com.nagarro.MiniAssignment2.model;

import java.util.Arrays;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

public class RandomAPIUser{

	    private List<Result> results;

	    public List<Result> getResults() {
			return results;
		}
		public void setResults(List<Result> results) {
			this.results = results;
		}

		
		public static class Result {
	        private String gender;
	        private Name name;
	        private Dob dob;
	        private String nat;
	       
	        public String getGender() {
				return gender;
			}
			public void setGender(String gender) {
				this.gender = gender;
			}
			public Name getName() {
				return name;
			}
			public void setName(Name name) {
				this.name = name;
			}			
			public Dob getDob() {
				return dob;
			}
			public void setDob(Dob dob) {
				this.dob = dob;
			}
			public String getNat() {
				return nat;
			}
			public void setNat(String nat) {
				this.nat = nat;
			}


			public static class Name {
	            private String title;
	            private String first;
	            private String last;
	            
				public String getTitle() {
					return title;
				}
				public void setTitle(String title) {
					this.title = title;
				}
				public String getFirst() {
					return first;
				}
				public void setFirst(String first) {
					this.first = first;
				}
				public String getLast() {
					return last;
				}
				public void setLast(String last) {
					this.last = last;
				}	            
	        }
			
			public static class Dob{
				private int age;
				private String date;
				public int getAge() {
					return age;
				}
				public void setAge(int age) {
					this.age = age;
				}
				public String getDate() {
					return date;
				}
				public void setDate(String date) {
					this.date = date;
				}
				
			}
	    }
}

