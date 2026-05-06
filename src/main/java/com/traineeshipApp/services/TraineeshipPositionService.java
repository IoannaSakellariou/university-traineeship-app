package com.traineeshipApp.services;

import java.util.List;
import java.util.Optional;
import com.traineeshipApp.domainmodel.Company;
import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;

public interface TraineeshipPositionService {

    Optional<TraineeshipPosition> findOptionalByStudent(Student student);

    void save(TraineeshipPosition position);

    TraineeshipPosition findById(Integer positionId);

    void deleteById(Integer id);

    void assignStudentToPosition(Integer positionId, Integer studentId);
    
    List<TraineeshipPosition> getAssignedPositionsByCompany(Company company);

	List<TraineeshipPosition> findAssignedWithoutSupervisor();

	List<TraineeshipPosition> getAllCompleted();

	
	Optional<TraineeshipPosition> findActiveByStudent(Student student);

}
