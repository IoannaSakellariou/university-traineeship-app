package com.traineeshipApp.services;

import java.util.List;

import com.traineeshipApp.domainmodel.CommitteeMember;
import com.traineeshipApp.domainmodel.Company;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.domainmodel.User;

public interface CompanyService {

    Company getCurrentCompany();

    Company findByUsername(String username);

    Company findByUser(User user);

    Company save(Company company);

    void updateCurrentCompany(Company companyForm);

    List<TraineeshipPosition> retrieveAvailablePositions();

	void createPositionForCurrentCompany(TraineeshipPosition position, CommitteeMember cm);
}
