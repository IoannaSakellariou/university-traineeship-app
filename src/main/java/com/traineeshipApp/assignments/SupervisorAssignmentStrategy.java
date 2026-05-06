package com.traineeshipApp.assignments;

import java.util.List;

import com.traineeshipApp.domainmodel.Professor;

public abstract interface SupervisorAssignmentStrategy {
	
	public void assign(Integer positionId);
	
	List<Professor> findMatchingProfessors(Integer positionId);

}
