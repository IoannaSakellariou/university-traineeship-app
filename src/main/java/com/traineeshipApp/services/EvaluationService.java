package com.traineeshipApp.services;

import com.traineeshipApp.domainmodel.Evaluation;
import com.traineeshipApp.domainmodel.EvaluationType;
import com.traineeshipApp.domainmodel.TraineeshipPosition;

import java.util.List;

public interface EvaluationService {
	
    List<Evaluation> findByTraineeshipPosition(TraineeshipPosition position);
    
	Evaluation findByPositionAndType(TraineeshipPosition position, EvaluationType professor);

	void saveOrUpdateEvaluation(Evaluation evaluation);

	
}
