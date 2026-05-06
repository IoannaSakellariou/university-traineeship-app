package com.traineeshipApp.services;

import java.util.List;

import com.traineeshipApp.domainmodel.Professor;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.domainmodel.User;

public interface ProfessorService {

    Professor getCurrentProfessor();

    Professor findByUsername(String username);

    Professor findByUser(User user);

    void updateCurrentProfessor(Professor professorForm);

	Professor findById(Integer professorId);

	List<TraineeshipPosition> getSupervisedPositions();
}

