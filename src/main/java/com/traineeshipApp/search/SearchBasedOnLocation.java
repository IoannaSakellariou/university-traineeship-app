package com.traineeshipApp.search;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.StudentMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;

@Component
public class SearchBasedOnLocation implements PositionsSearchStrategy{
	
	@Autowired
	public TraineeshipPositionMapper traineeshipPositionMapper;
	
	
	@Autowired
	public StudentMapper studentMapper;
	
	@Autowired
	public SearchBasedOnLocation( StudentMapper studentMapper, TraineeshipPositionMapper traineeshipPositionMapper) {

        this.studentMapper = studentMapper;
        this.traineeshipPositionMapper = traineeshipPositionMapper;
    }

	@Override
    public List<TraineeshipPosition> search(Integer studentId) {
		Student student = studentMapper.findById(studentId)
	            .orElseThrow(() -> new RuntimeException("Student not found"));
		
		Set<String> preferredLocations = student.getPreferredLocationsAsSet();

	    
		return traineeshipPositionMapper.findByIsAssignedFalse().stream()
	            .filter(pos -> {
	                String companyLocation = pos.getCompany() != null ? pos.getCompany().getCompanyLocation() : "";
	                return preferredLocations.stream()
	                        .anyMatch(loc -> companyLocation.toLowerCase().contains(loc.toLowerCase()));
	            })
	            .toList();}
}

