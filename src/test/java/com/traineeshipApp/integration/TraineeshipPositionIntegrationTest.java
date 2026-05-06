package com.traineeshipApp.integration;

import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TraineeshipPositionIntegrationTest {

    @Autowired
    private TraineeshipPositionMapper positionMapper;

    @Test
    public void shouldReturnOnlyInProgressTraineeships() {
        // given: Δημιουργούμε μοναδικά δεδομένα
        TraineeshipPosition p1 = new TraineeshipPosition();
        p1.setTitle("Test Internship - Active");
        p1.setAssigned(true);
        p1.setCompleted(false);
        positionMapper.save(p1);

        TraineeshipPosition p2 = new TraineeshipPosition();
        p2.setTitle("Test Internship - Completed");
        p2.setAssigned(true);
        p2.setCompleted(true);
        positionMapper.save(p2);

        // when: Κάνουμε την αναζήτηση
        List<TraineeshipPosition> results = positionMapper.findByIsAssignedTrueAndCompletedFalse();

        // then: Ελέγχουμε μόνο για την p1, ανεξαρτήτως άλλων στη βάση
        boolean containsActive = results.stream()
                .anyMatch(p -> "Test Internship - Active".equals(p.getTitle()));

        boolean containsCompleted = results.stream()
                .anyMatch(p -> "Test Internship - Completed".equals(p.getTitle()));

        assertThat(containsActive).isTrue();
        assertThat(containsCompleted).isFalse();
    }

    
    @AfterEach
    void cleanUp() {
        positionMapper.deleteAll();
    }
}
