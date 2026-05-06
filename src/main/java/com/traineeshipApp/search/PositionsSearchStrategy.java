package com.traineeshipApp.search;

import java.util.List;

import com.traineeshipApp.domainmodel.TraineeshipPosition;

public interface PositionsSearchStrategy {


	List<TraineeshipPosition> search(Integer studentId);

}
