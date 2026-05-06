package com.traineeshipApp.mappers;

import com.traineeshipApp.domainmodel.Evaluation;
import com.traineeshipApp.domainmodel.EvaluationType;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationMapper extends JpaRepository<Evaluation, Integer> {

    
    List<Evaluation> findByTraineeshipPosition(TraineeshipPosition traineeshipPosition);
    
    List<Evaluation> findByTraineeshipPositionId(Integer positionId);
    
    Evaluation findByTraineeshipPositionAndEvaluationType(TraineeshipPosition position, EvaluationType evaluationType);
}
