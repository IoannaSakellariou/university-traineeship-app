package com.traineeshipApp.mappers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.User;



@Repository
public interface StudentMapper extends JpaRepository<Student, Integer> {

    Optional<Student> findByUsername(String username);

	Optional<Student> findById(Integer studentId);
	
	Optional<Student> findByUser(User user);

	List<Student> findByAssignedTraineeshipIsNull();
	
	@Query("SELECT DISTINCT s FROM Student s JOIN s.appliedPositions p WHERE p.isAssigned = false")
	List<Student> findStudentsWithActiveApplications();


}
