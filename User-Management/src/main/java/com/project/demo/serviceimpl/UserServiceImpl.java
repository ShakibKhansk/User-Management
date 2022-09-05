package com.project.demo.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.demo.model.User;
import com.project.demo.repository.UserRepository;
import com.project.demo.service.UserService;
import com.project.demo.utility.Constant;

@Service
public class UserServiceImpl implements UserService {

	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EntityManager entityManager;

	@Override
	public User addUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public List<User> getUserByNameOrSurnameOrPincode(String name, String surname, String pincode) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<User> cq = cb.createQuery(User.class);

		Root<User> user = cq.from(User.class);

		List<Predicate> allPredicates = new ArrayList<>();

		if (!name.isEmpty() && name != null)
			allPredicates.add(cb.equal(user.get(Constant.NAME), name));

		if (!surname.isEmpty() && surname != null)
			allPredicates.add(cb.equal(user.get(Constant.SURNAME), surname));

		if (!pincode.isEmpty() && pincode != null)
			allPredicates.add(cb.equal(user.get(Constant.PINCODE), pincode));

		allPredicates.add(cb.equal(user.get(Constant.ACTIVE_STATUS), true));

		cq.where(allPredicates.toArray(new Predicate[0]));

		return entityManager.createQuery(cq).getResultList();

	}

	@Override
	public List<User> getUserSortedByJoiningDate() {
		List<User> allUsers = userRepository.findByActiveStatus(true);

		Comparator<User> comparator = Comparator.comparing(User::getJoiningDate);

		Collections.sort(allUsers, comparator);

		return allUsers;
	}

	@Override
	public ResponseEntity<String> softDeleteUserById(Long id) {
		try {
			Optional<User> userFromRepo = userRepository.findById(id);
			if (userFromRepo.isPresent()) {
				User user = userFromRepo.get();
				if (user.getActiveStatus() == false) {
					return new ResponseEntity<>(Constant.ALREADY_DELETED, HttpStatus.OK);
				} else {
					user.setActiveStatus(false);
					userRepository.save(user);
					return new ResponseEntity<>(Constant.DELETED_SUCCESSFULLY, HttpStatus.OK);
				}

			} else {
				return new ResponseEntity<>(Constant.USER_NOT_FOUND, HttpStatus.EXPECTATION_FAILED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> updateUser(User user) {
		try {
			if (user.getId() != null) {
				Optional<User> userFromRepo = userRepository.findById(user.getId());
				if (userFromRepo.isPresent()) {
					userRepository.save(user);
					return new ResponseEntity<>(Constant.UPDATED_SUCCESSFULLY, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(Constant.USER_NOT_FOUND, HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				return new ResponseEntity<>(Constant.USER_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
