package com.traineeshipApp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.traineeshipApp.domainmodel.Company;
import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.StudentMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;

@Service
public class TraineeshipPositionServiceImpl implements TraineeshipPositionService{
	
	@Autowired
    private TraineeshipPositionMapper traineeshipPositionMapper;
	
	@Autowired 
	private StudentMapper studentMapper;

	@Override
	public Optional<TraineeshipPosition> findActiveByStudent(Student student) {
	    return traineeshipPositionMapper.findFirstByStudentAndCompletedFalseOrderByFromDateDesc(student);
	}

    
	@Override
	public Optional<TraineeshipPosition> findOptionalByStudent(Student student) {
	    return traineeshipPositionMapper.findFirstByStudentAndCompletedFalseOrderByFromDateDesc(student);
	}


    public void save(TraineeshipPosition position) {
        traineeshipPositionMapper.save(position);
    }
    

    @Override
    public TraineeshipPosition findById(Integer id) {
        return traineeshipPositionMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("Η θέση πρακτικής με ID " + id + " δεν βρέθηκε."));
    }
    
    @Override
    public void deleteById(Integer id) {
        traineeshipPositionMapper.deleteById(id);
    }
    
    @Override
    public void assignStudentToPosition(Integer positionId, Integer studentId) {
        TraineeshipPosition newPosition = traineeshipPositionMapper.findById(positionId)
            .orElseThrow(() -> new RuntimeException("Position not found"));
        Student student = studentMapper.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        TraineeshipPosition oldPosition = student.getAssignedTraineeship();

        if (oldPosition != null) {
            if (!oldPosition.isCompleted()) {
                throw new IllegalStateException("Student already has an active traineeship!");
            }

        }

        newPosition.setStudent(student);
        newPosition.setAssigned(true);
        newPosition.setAssignedStudentAM(student.getAM());
        student.setAssignedTraineeship(newPosition);

        traineeshipPositionMapper.save(newPosition);
        studentMapper.save(student);
        System.out.println("Assigned student " + student.getAM() + " to position " + newPosition.getTitle());

    }
    
    public List<TraineeshipPosition> getAvailablePositionsByLocationAndSkills(String location, String skills) {
        return traineeshipPositionMapper.findByTopicsContainingIgnoreCaseAndSkillsContainingIgnoreCaseAndIsAssignedFalse(location, skills);
    }

    
    @Override
    public List<TraineeshipPosition> getAssignedPositionsByCompany(Company company) {
        return traineeshipPositionMapper.findByCompanyAndIsAssignedTrueAndCompletedFalse(company);
    }
   
    
    public List<TraineeshipPosition> findAllAssignedPositions() {
        return traineeshipPositionMapper.findByStudentIsNotNull();
    }
    
    public List<TraineeshipPosition> findAssignedWithoutSupervisor() {
        return traineeshipPositionMapper.findAssignedWithoutSupervisor();
    }
    
    
    public List<TraineeshipPosition> getAllCompleted() {
        return traineeshipPositionMapper.findByCompletedTrue();
    }
    


}
