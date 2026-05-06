package com.traineeshipApp.mappers;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traineeshipApp.domainmodel.CommitteeMember;
import com.traineeshipApp.domainmodel.User;

@Repository
public interface CommitteeMemberMapper extends JpaRepository<CommitteeMember, Integer> {


    Optional<CommitteeMember> findByUsername(String username);
    
    Optional<CommitteeMember> findById(Integer committeeMemberId);
	
	Optional<CommitteeMember> findByUser(User user);
}
