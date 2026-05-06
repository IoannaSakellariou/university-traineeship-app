package com.traineeshipApp.assignments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupervisorAssignmentFactory {

    @Autowired
    private AssignmentBasedOnLoad assignmentBasedOnLoad;

    @Autowired
    private AssignmentBasedOnInterests assignmentBasedOnInterests;

    public SupervisorAssignmentStrategy create(String strategy) {
        return switch (strategy.toLowerCase()) {
            case "load" -> assignmentBasedOnLoad;
            case "interests" -> assignmentBasedOnInterests;
            default -> throw new IllegalArgumentException("Unknown strategy: " + strategy);
        };
    }
}
