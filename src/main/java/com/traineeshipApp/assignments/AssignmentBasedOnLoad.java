package com.traineeshipApp.assignments;

import com.traineeshipApp.domainmodel.*;
import com.traineeshipApp.mappers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssignmentBasedOnLoad implements SupervisorAssignmentStrategy {

    @Autowired
    private TraineeshipPositionMapper positionMapper;

    @Autowired
    private ProfessorMapper professorMapper;

    @Override
    public void assign(Integer positionId) {
        TraineeshipPosition position = positionMapper.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        Professor leastBusy = professorMapper.findAll().stream()
        	    .min(Comparator.comparingInt(this::getActiveSupervisions))
                .orElseThrow(() -> new RuntimeException("No professors available"));

        position.setSupervisor(leastBusy);
        positionMapper.save(position);
    }
    

    @Override
    public List<Professor> findMatchingProfessors(Integer positionId) {
    	 List<Professor> all = professorMapper.findAll();
    	    for (Professor p : all) {
    	        System.out.println(p.getProfessorName() + " is supervising " + getActiveSupervisions(p) + " students");
    	    }
        return professorMapper.findAll().stream()
                .sorted(Comparator.comparingInt(Professor::getSupervisedCount))
                .collect(Collectors.toList());
    }
    
    private int getActiveSupervisions(Professor prof) {
        return (int) prof.getSupervisedPositions()
                         .stream()
                         .filter(p -> !p.isCompleted())
                         .count();
    }


    
}
