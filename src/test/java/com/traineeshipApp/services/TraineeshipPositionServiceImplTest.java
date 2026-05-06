package com.traineeshipApp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.StudentMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;

@ExtendWith(MockitoExtension.class)
public class TraineeshipPositionServiceImplTest {
	
	@Mock
    private TraineeshipPositionMapper traineeshipPositionMapper;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private TraineeshipPositionServiceImpl service;
    
    @Mock
    private Student student;
    
    @Mock
    private TraineeshipPosition position;
    
    @BeforeEach
    void setup() {
        student = new Student();
        student.setId(1);
        student.setStudentName("Test Student");

        position = new TraineeshipPosition();
        position.setId(10);
    }


    @Test
    void testAssignStudentToPosition_withCompletedPrevious_shouldAssignNew() {
        // Arrange
        Student student = new Student();
        student.setId(1);
        TraineeshipPosition oldPos = new TraineeshipPosition();
        oldPos.setId(10);
        oldPos.setCompleted(true);
        oldPos.setStudent(student);
        student.setAssignedTraineeship(oldPos);

        TraineeshipPosition newPos = new TraineeshipPosition();
        newPos.setId(20);

        when(studentMapper.findById(1)).thenReturn(Optional.of(student));
        when(traineeshipPositionMapper.findById(20)).thenReturn(Optional.of(newPos));

        // Act
        service.assignStudentToPosition(20, 1);

        // Assert
        assertEquals(student, newPos.getStudent());
        assertEquals(newPos, student.getAssignedTraineeship());
        assertTrue(newPos.isAssigned());

        verify(studentMapper).save(student);
        verify(traineeshipPositionMapper).save(newPos);
    }
    
    
    @Test
    void testAssignStudentToPosition() {
        when(traineeshipPositionMapper.findById(10)).thenReturn(Optional.of(position));
        when(studentMapper.findById(1)).thenReturn(Optional.of(student));

        service.assignStudentToPosition(10, 1);

        assertEquals(student, position.getStudent());
        assertTrue(position.isAssigned());
        verify(traineeshipPositionMapper).save(position);
    }
}

