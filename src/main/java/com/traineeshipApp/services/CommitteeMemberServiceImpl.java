package com.traineeshipApp.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.traineeshipApp.assignments.AssignmentBasedOnInterests;
import com.traineeshipApp.assignments.AssignmentBasedOnLoad;
import com.traineeshipApp.domainmodel.CommitteeMember;
import com.traineeshipApp.domainmodel.Professor;
import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.domainmodel.User;
import com.traineeshipApp.mappers.CommitteeMemberMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;
import com.traineeshipApp.mappers.UserMapper;

@Service
public class CommitteeMemberServiceImpl implements CommitteeMemberService {

	@Autowired
	AssignmentBasedOnLoad assignmentBasedOnLoad;
	
	@Autowired
	AssignmentBasedOnInterests assignmentBasedOnInterests;
	
	@Autowired private CommitteeMemberMapper committeeMemberMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private TraineeshipPositionMapper traineeshipPositionMapper;

    @Override
    public CommitteeMember getCurrentCommitteeMember() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return committeeMemberMapper.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("CommitteeMember not found"));
    }

    @Override
    public CommitteeMember findByUsername(String username) {
        return committeeMemberMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("CommitteeMember not found"));
    }

    @Override
    public CommitteeMember findByUser(User user) {
        return committeeMemberMapper.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("CommitteeMember not found"));
    }

    @Override
    public CommitteeMember findById(Integer id) {
        return committeeMemberMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("CommitteeMember with ID " + id + " not found"));
    }

    @Override
    public CommitteeMember save(CommitteeMember committeeMember) {
        return committeeMemberMapper.save(committeeMember);
    }

    @Override
    public void updateCurrentCommitteeMember(CommitteeMember form) {
        CommitteeMember cm = getCurrentCommitteeMember();
        committeeMemberMapper.save(cm);
    }

    @Override
    public CommitteeMember getFirstAvailable() {
        return committeeMemberMapper.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No committee member found"));
    }
    
    @Override
    public List<Professor> getSuggestedProfessors(Integer positionId, String strategy) {
        if ("load".equalsIgnoreCase(strategy)) {
            return assignmentBasedOnLoad.findMatchingProfessors(positionId);
        } else {
            return assignmentBasedOnInterests.findMatchingProfessors(positionId);
        }
    }
    
    @Override
    public List<TraineeshipPosition> findInProgressTraineeships() {
        return traineeshipPositionMapper.findByIsAssignedTrueAndCompletedFalse();
    }
    
    @Override
    public List<Student> findStudentsWithApplications(CommitteeMember member) {
        List<TraineeshipPosition> positions = traineeshipPositionMapper.findByCommitteeMember(member);

        Set<Integer> seenIds = new HashSet<>();
        List<Student> uniqueStudents = new ArrayList<>();

        for (TraineeshipPosition position : positions) {
            if (!position.isAssigned()) {  
                for (Student student : position.getApplicants()) {
                    if (student != null && seenIds.add(student.getId())) {
                        uniqueStudents.add(student); 
                    }
                }
            }
        }
        return uniqueStudents;
    }
}

