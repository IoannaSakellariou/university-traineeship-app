package com.traineeshipApp.assignments;

import java.util.Comparator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.traineeshipApp.domainmodel.Professor;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.ProfessorMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;

@Component
public class AssignmentBasedOnInterests implements SupervisorAssignmentStrategy {

    @Autowired
    private TraineeshipPositionMapper positionMapper;

    @Autowired
    private ProfessorMapper professorMapper;

    @Override
    public void assign(Integer positionId) {
        TraineeshipPosition position = positionMapper.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        Professor bestMatch = professorMapper.findAll().stream()
                .max(Comparator.comparingInt(p ->
                        (int) p.getInterestsAsSet().stream()
                            .filter(i -> position.getTopicsAsSet().contains(i))
                            .count()))
                .orElseThrow(() -> new RuntimeException("No professors found"));

        position.setSupervisor(bestMatch);
        positionMapper.save(position);
    }
    

    @Override
    public List<Professor> findMatchingProfessors(Integer positionId) {
        TraineeshipPosition position = positionMapper.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        return professorMapper.findAll().stream()
                .filter(prof -> !prof.getInterestsAsSet().isEmpty() &&
                        prof.getInterestsAsSet().stream().anyMatch(
                            topic -> position.getTopicsAsSet().contains(topic)
                        ))
                .sorted((p1, p2) -> {
                    long matches1 = p1.getInterestsAsSet().stream()
                            .filter(position.getTopicsAsSet()::contains).count();
                    long matches2 = p2.getInterestsAsSet().stream()
                            .filter(position.getTopicsAsSet()::contains).count();
                    return Long.compare(matches2, matches1); 
                })
                .collect(Collectors.toList());
    }


}