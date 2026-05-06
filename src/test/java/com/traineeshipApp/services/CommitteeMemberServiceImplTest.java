package com.traineeshipApp.services;

import com.traineeshipApp.assignments.AssignmentBasedOnInterests;
import com.traineeshipApp.assignments.AssignmentBasedOnLoad;
import com.traineeshipApp.domainmodel.CommitteeMember;
import com.traineeshipApp.domainmodel.Professor;
import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.CommitteeMemberMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;
import com.traineeshipApp.mappers.UserMapper;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommitteeMemberServiceImplTest {

    @Autowired
    private CommitteeMemberService committeeMemberService;

    @MockBean
    private CommitteeMemberMapper committeeMemberMapper;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private TraineeshipPositionMapper traineeshipPositionMapper;

    @MockBean
    private AssignmentBasedOnLoad assignmentBasedOnLoad;

    @MockBean
    private AssignmentBasedOnInterests assignmentBasedOnInterests;

    @Test
    void testFindInProgressTraineeships_ReturnsList() {
        List<TraineeshipPosition> dummyPositions = List.of(new TraineeshipPosition());
        when(traineeshipPositionMapper.findByIsAssignedTrueAndCompletedFalse()).thenReturn(dummyPositions);

        List<TraineeshipPosition> result = committeeMemberService.findInProgressTraineeships();
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetSuggestedProfessors_UsesLoadStrategy() {
        Integer positionId = 1;
        List<Professor> expected = List.of(new Professor());
        when(assignmentBasedOnLoad.findMatchingProfessors(positionId)).thenReturn(expected);

        List<Professor> result = committeeMemberService.getSuggestedProfessors(positionId, "load");
        assertEquals(expected, result);
    }

    @Test
    void testGetSuggestedProfessors_UsesInterestStrategy() {
        Integer positionId = 2;
        List<Professor> expected = List.of(new Professor());
        when(assignmentBasedOnInterests.findMatchingProfessors(positionId)).thenReturn(expected);

        List<Professor> result = committeeMemberService.getSuggestedProfessors(positionId, "interests");
        assertEquals(expected, result);
    }

    @Test
    void testFindStudentsWithApplications_ReturnsUniqueStudents() {
        CommitteeMember member = new CommitteeMember();
        Student student1 = new Student();
        student1.setId(1);
        Student student2 = new Student();
        student2.setId(2);

        TraineeshipPosition pos1 = new TraineeshipPosition();
        pos1.setApplicants(List.of(student1, student2));
        TraineeshipPosition pos2 = new TraineeshipPosition();
        pos2.setApplicants(List.of(student1));  // Duplicate student1

        when(traineeshipPositionMapper.findByCommitteeMember(member)).thenReturn(List.of(pos1, pos2));

        List<Student> result = committeeMemberService.findStudentsWithApplications(member);
        assertEquals(2, result.size());
    }
}
