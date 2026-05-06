package com.traineeshipApp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.traineeshipApp.domainmodel.Evaluation;
import com.traineeshipApp.domainmodel.EvaluationType;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.EvaluationMapper;


@ExtendWith(MockitoExtension.class)
public class EvaluationServiceTest {

    @Mock
    private EvaluationMapper evaluationMapper;

    @InjectMocks
    private EvaluationServiceImpl evaluationService;

    @Test
    void testFindByTraineeshipPosition_ReturnsEvaluations() {
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(1);

        List<Evaluation> mockEvaluations = List.of(new Evaluation(), new Evaluation());

        when(evaluationMapper.findByTraineeshipPosition(position)).thenReturn(mockEvaluations);

        List<Evaluation> result = evaluationService.findByTraineeshipPosition(position);

        assertEquals(2, result.size());
        verify(evaluationMapper).findByTraineeshipPosition(position);
    }

    @Test
    void testFindByPositionAndType_WhenNotFound() {
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(1);

        when(evaluationService.findByPositionAndType(position, EvaluationType.COMPANY)).thenReturn(null);

        Evaluation eval = evaluationService.findByPositionAndType(position, EvaluationType.COMPANY);

        assertNull(eval);
    }
}
