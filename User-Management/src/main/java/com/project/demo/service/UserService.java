package com.project.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.project.demo.model.User;

public interface UserService {
	
	public User addUser(User user);

	public List<User> getUserByNameOrSurnameOrPincode(String name, String surname, String pincode);

	public List<User>  getUserSortedByJoiningDate();

	public ResponseEntity<String> softDeleteUserById(Long id);

	public ResponseEntity<String> updateUser(User user);
	
}
