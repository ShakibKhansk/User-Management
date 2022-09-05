package com.project.demo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

	List<User> findByActiveStatus(boolean b, Sort by);

	List<User> findByActiveStatus(boolean b);


}
