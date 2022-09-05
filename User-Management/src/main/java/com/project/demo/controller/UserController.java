package com.project.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.demo.model.User;
import com.project.demo.repository.UserRepository;
import com.project.demo.service.UserService;
import com.project.demo.utility.Constant;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping(Constant.ADD_USER)
	public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
		try {
			return new ResponseEntity<User>(userService.addUser(user), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(Constant.UPDATE_USER)
	public ResponseEntity<String> updateUser(@Valid @RequestBody User user) {
		try {
			return userService.updateUser(user);
		} catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(Constant.SEARCH_USER)
	public ResponseEntity<List<User>> getUserByNameOrSurnameOrPincode(@RequestParam String name,
			@RequestParam String surname, @RequestParam String pincode) {
		try {
			return new ResponseEntity<List<User>>(userService.getUserByNameOrSurnameOrPincode(name,surname,pincode),HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(Constant.SORT_USER_DOB)
	public ResponseEntity<List<User>> getUserSortedByDOB(){
		try {
			return new ResponseEntity<List<User>>(userRepository.findByActiveStatus(true,Sort.by("dob")),HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(Constant.SORT_USER_JOINING_DATE)
	public ResponseEntity<List<User>> getUserSortedByJoiningDate(){
		try {
			return new ResponseEntity<List<User>>(userService.getUserSortedByJoiningDate(),HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(Constant.SOFT_DELETE_USER)
	public ResponseEntity<String> softDeleteUserById(@RequestParam Long id){
		try {
			return userService.softDeleteUserById(id);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(Constant.HARD_DELETE_USER)
	public ResponseEntity<String> hardDeleteUserById(@RequestParam Long id){
		try {
			 userRepository.deleteById(id);
			 return new ResponseEntity<>(Constant.DELETED_SUCCESSFULLY,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
