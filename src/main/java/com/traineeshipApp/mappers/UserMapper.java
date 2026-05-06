package com.traineeshipApp.mappers;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traineeshipApp.domainmodel.User;

@Repository
public interface UserMapper extends JpaRepository<User, String> {
	
	Optional<User> findByUsername(String username);



}
