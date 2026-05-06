package com.traineeshipApp.search;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.traineeshipApp.domainmodel.TraineeshipPosition;

@Component
public class CompositeSearch implements PositionsSearchStrategy {

    @Autowired private SearchBasedOnInterests interestStrategy;
    @Autowired private SearchBasedOnLocation locationStrategy;

    @Override
    public List<TraineeshipPosition> search(Integer studentId) {
        List<TraineeshipPosition> byInterest = interestStrategy.search(studentId);
        List<TraineeshipPosition> byLocation = locationStrategy.search(studentId);

        List<TraineeshipPosition> intersection = byInterest.stream()
                .filter(byLocation::contains)
                .toList();

        List<TraineeshipPosition> combined = new java.util.ArrayList<>(intersection);

        byInterest.stream()
                .filter(p -> !intersection.contains(p))
                .forEach(combined::add);

        byLocation.stream()
                .filter(p -> !combined.contains(p))  
                .forEach(combined::add);

        return combined;
    }
}

