package com.traineeshipApp.services;

import java.util.List;
import com.traineeshipApp.domainmodel.CommitteeMember;
import com.traineeshipApp.domainmodel.Professor;
import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.domainmodel.User;

public interface CommitteeMemberService {

    CommitteeMember getCurrentCommitteeMember();

    CommitteeMember findByUsername(String username);

    CommitteeMember findByUser(User user);

    CommitteeMember findById(Integer id);

    CommitteeMember save(CommitteeMember committeeMember);

    void updateCurrentCommitteeMember(CommitteeMember committeeMemberForm);

    CommitteeMember getFirstAvailable();

    List<TraineeshipPosition> findInProgressTraineeships();

	List<Student> findStudentsWithApplications(CommitteeMember member);

	List<Professor> getSuggestedProfessors(Integer positionId, String strategy);
}
