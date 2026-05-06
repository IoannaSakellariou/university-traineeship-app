package com.traineeshipApp.search;

import java.util.Collections;
import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.CompanyMapper;
import com.traineeshipApp.mappers.StudentMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;

@Component
public class SearchBasedOnInterests implements PositionsSearchStrategy{
	public CompanyMapper companyMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TraineeshipPositionMapper traineeshipPositionMapper;
	
	
	public SearchBasedOnInterests( StudentMapper studentMapper, TraineeshipPositionMapper positionsMapper) {
        this.traineeshipPositionMapper = positionsMapper;
        this.studentMapper = studentMapper;
    }

	@Override
	public List<TraineeshipPosition> search(Integer studentId) {
	    Student student = studentMapper.findById(studentId)
	            .orElseThrow(() -> new RuntimeException("Student not found"));
	    
	    Set<String> studentInterests = student.getInterestsAsSet();
	    
	    return traineeshipPositionMapper.findByIsAssignedFalse().stream()
	            .filter(pos -> !Collections.disjoint(pos.getTopicsAsSet(), studentInterests))
	            .toList();
	}

}
