package com.traineeshipApp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.domainmodel.User;
import com.traineeshipApp.mappers.StudentMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;
import com.traineeshipApp.mappers.UserMapper;
import org.mockito.quality.Strictness;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StudentServiceTest {

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private UserMapper userMapper;
    
    @Mock
    private TraineeshipPositionMapper traineeshipPositionMapper;

    @InjectMocks
    private StudentServiceImpl studentService;
    
    @Test
    void testAppendToLogbook_AppendsSuccessfully() {
        
        String username = "testStudent";
        String logbookEntry = "Entry 1";

        User user = new User();
        user.setUsername(username);

        Student student = new Student();
        TraineeshipPosition traineeshipPosition = new TraineeshipPosition();
        student.setUser(user);
        traineeshipPosition.setStudentLogBook("Info.\n");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userMapper.findByUsername(username)).thenReturn(Optional.of(user));
        when(studentMapper.findByUser(user)).thenReturn(Optional.of(student));
        when(studentMapper.save(any(Student.class))).thenReturn(student);
        when(traineeshipPositionMapper.findByStudent(student)).thenReturn(List.of(traineeshipPosition));

        studentService.appendToLogbook(logbookEntry);

        assertTrue(traineeshipPosition.getStudentLogBook().contains(logbookEntry));
    }

    @Test
    void testFindByUsername_WhenStudentExists() {
        String username = "john";
        
        User mockUser = new User();
        mockUser.setUsername(username);
        
        Student mockStudent = new Student();
        mockStudent.setUser(mockUser);

        when(userMapper.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(studentMapper.findByUser(mockUser)).thenReturn(Optional.of(mockStudent));

        Student result = studentService.findByUsername(username);

        assertEquals(mockStudent, result);
    }
    
    @Test
    void testFindByUsername_WhenStudentDoesNotExist() {
        when(studentMapper.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            studentService.findByUsername("unknown");
        });
    }


    @Test
    void testFindById_WhenNotFound() {
        when(studentMapper.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            studentService.findById(999);
        });
    }
    
    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
    
    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

}
