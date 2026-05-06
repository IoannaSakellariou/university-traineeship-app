package com.traineeshipApp.mappers;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traineeshipApp.domainmodel.Professor;

import com.traineeshipApp.domainmodel.User;

@Repository
public interface ProfessorMapper extends JpaRepository<Professor, Integer> {


	 Optional<Professor> findByUsername(String username);

	 Optional<Professor> findById(Integer professorId);
		
	 Optional<Professor> findByUser(User user);
}
