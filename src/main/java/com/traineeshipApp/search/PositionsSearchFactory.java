package com.traineeshipApp.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PositionsSearchFactory {

    @Autowired private SearchBasedOnInterests searchBasedOnInterests;
    @Autowired private SearchBasedOnLocation searchBasedOnLocation;
    @Autowired private CompositeSearch compositeSearch;

    public PositionsSearchStrategy create(String strategy) {
        return switch (strategy.toLowerCase()) {
            case "interests" -> searchBasedOnInterests;
            case "location" -> searchBasedOnLocation;
            case "both" -> compositeSearch;
            default -> throw new IllegalArgumentException("Unknown strategy: " + strategy);
        };
    }
}
