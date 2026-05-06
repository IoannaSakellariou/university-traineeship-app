package com.traineeshipApp.services;

import com.traineeshipApp.domainmodel.Evaluation;
import com.traineeshipApp.domainmodel.EvaluationType;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.EvaluationMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationMapper evaluationMapper;
    
    @Autowired
    TraineeshipPositionMapper traineeshipPositionMapper;


    @Override
    public List<Evaluation> findByTraineeshipPosition(TraineeshipPosition position) {
        return evaluationMapper.findByTraineeshipPosition(position);
    }

    @Override
    public Evaluation findByPositionAndType(TraineeshipPosition position, EvaluationType type) {
        return evaluationMapper.findByTraineeshipPositionAndEvaluationType(position, type);
    }

    public void saveProfessorEvaluation(Evaluation evaluation) {
        Integer positionId = evaluation.getTraineeshipPosition().getId();
        TraineeshipPosition position = traineeshipPositionMapper.findById(positionId)
            .orElseThrow(() -> new RuntimeException("Position not found"));
        evaluation.setTraineeshipPosition(position);
        evaluation.setEvaluationType(EvaluationType.PROFESSOR);
        evaluationMapper.save(evaluation);
    }
    
    public void saveOrUpdateEvaluation(Evaluation newEval) {
        TraineeshipPosition position = newEval.getTraineeshipPosition();
        EvaluationType type = newEval.getEvaluationType();

        Optional<Evaluation> existingOpt =
            Optional.ofNullable(evaluationMapper.findByTraineeshipPositionAndEvaluationType(position, type));

        Evaluation evaluationToSave = existingOpt.orElseGet(() -> {
            Evaluation e = new Evaluation();
            e.setTraineeshipPosition(position);
            e.setEvaluationType(type);
            return e;
        });

        evaluationToSave.setMotivation(newEval.getMotivation());
        evaluationToSave.setEffectiveness(newEval.getEffectiveness());
        evaluationToSave.setEfficiency(newEval.getEfficiency());

        if (type == EvaluationType.PROFESSOR || type == EvaluationType.COMMITTEE_MEMBER) {
            evaluationToSave.setFacilities(newEval.getFacilities());
            evaluationToSave.setGuidance(newEval.getGuidance());
        } else {
            evaluationToSave.setFacilities(0);
            evaluationToSave.setGuidance(0);
        }

        evaluationMapper.save(evaluationToSave);
    }

    public Evaluation existsByPositionAndType(TraineeshipPosition position, EvaluationType type) {
        return evaluationMapper.findByTraineeshipPositionAndEvaluationType(position, type);
    }

 }
