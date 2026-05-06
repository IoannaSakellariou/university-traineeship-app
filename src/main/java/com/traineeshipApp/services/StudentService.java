package com.traineeshipApp.services;

import java.util.List;

import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.domainmodel.User;

public interface StudentService {
	
	Student findByUsername(String username);
	
	Student save(Student student);

    Student findById(Integer id);

    Student findByUser(User user);

    Student getCurrentStudent();

    void updateCurrentStudent(Student studentForm);
    
    void applyForPosition(String username, Integer positionId);

	List<TraineeshipPosition> getAvailablePositionsForCurrentStudent();

	List<TraineeshipPosition> getApplicationsForCurrentStudent();
	
	
	void appendToLogbook(String logbookText);

	List<TraineeshipPosition> getCompletedTraineeships();

	List<Student> findStudentsWithoutAssignedPosition();
	
	

	
	
}
