package com.nagarro.MiniAssignment2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nagarro.MiniAssignment2.model.User;
import com.nagarro.MiniAssignment2.service.RandomAPIService;
import com.nagarro.MiniAssignment2.service.UserService;
import com.nagarro.MiniAssignment2.service.ValidationService;
import reactor.core.publisher.Flux;


@RestController
public class UserController {

	@Autowired 
	private UserService userservice;
	@Autowired
	private ValidationService validate;
	@Autowired
	private RandomAPIService randomapiService;
	
	
	@PostMapping("/user")
	public Flux<User> registerUser(@RequestParam("size") int size) throws Exception{
		if (size < 1 || size > 5) {
			throw new Exception("Size should be a number between 1 and 5");
		}
		return randomapiService.fetchAndSaveData(size);
	}
	
	
	@GetMapping("/user")
	public void registerUser(@RequestParam(name="sortType") String sortType, 
			@RequestParam(name="sortOrder") String sortOrder, 
			@RequestParam(name="limit") int limit, 
			@RequestParam(name="offset") int offset) throws Exception {
		validate.validateAlphabets(sortType, sortOrder);
		validate.validateNumbers(limit, offset);
		userservice.getSortedUsers(sortType, sortOrder, limit, offset);
	}
}
