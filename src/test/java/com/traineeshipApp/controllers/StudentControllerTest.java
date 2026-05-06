package com.traineeshipApp.controllers;

import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.services.StudentService;
import com.traineeshipApp.services.TraineeshipPositionService;
import com.traineeshipApp.services.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private TraineeshipPositionService traineeshipPositionService;
    
    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "STUDENT")
    void testDashboardAccess() throws Exception {
        Student mockStudent = new Student();
        mockStudent.setId(1);
        mockStudent.setUsername("student1");
        mockStudent.setAssignedTraineeship(null); 

        Mockito.when(studentService.getCurrentStudent()).thenReturn(mockStudent);

        mockMvc.perform(get("/student/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/dashboard"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testEditProfileGet() throws Exception {
        when(studentService.getCurrentStudent()).thenReturn(new Student());

        mockMvc.perform(get("/student/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("student"))
                .andExpect(view().name("student/edit"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testListAvailablePositions() throws Exception {
        when(studentService.getAvailablePositionsForCurrentStudent()).thenReturn(List.of());

        mockMvc.perform(get("/student/availablepositions"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("positions"))
                .andExpect(view().name("student/availablepositions"));
    }

    @Test
    @WithMockUser(username = "studentuser", roles = "STUDENT")
    void testApplyForPosition() throws Exception {
        mockMvc.perform(post("/student/apply/1")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/availablepositions"));

        verify(studentService).applyForPosition("studentuser", 1);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testViewApplications() throws Exception {
        when(studentService.getApplicationsForCurrentStudent()).thenReturn(List.of());

        mockMvc.perform(get("/student/myapplications"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("positions"))
                .andExpect(view().name("student/myapplications"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testViewLogbookWhenAssignedAndNotCompleted() throws Exception {
        Student student = new Student();
        TraineeshipPosition position = new TraineeshipPosition();
        position.setCompleted(false);

        when(studentService.getCurrentStudent()).thenReturn(student);
        when(traineeshipPositionService.findOptionalByStudent(student)).thenReturn(Optional.of(position));

        mockMvc.perform(get("/student/logbook"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("position", position))
                .andExpect(view().name("student/logbook"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testViewLogbookWhenNotAssigned() throws Exception {
        when(studentService.getCurrentStudent()).thenReturn(new Student());
        when(traineeshipPositionService.findOptionalByStudent(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/student/logbook"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/no-position"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testUpdateLogbook() throws Exception {
        mockMvc.perform(post("/student/logbook")
                        .with(csrf())
                        .param("logbookText", "Updated entry"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/logbook"));

        verify(studentService).appendToLogbook("Updated entry");
    }
    
    /*
    @Test
    @WithMockUser(roles = "STUDENT")
    void testViewHistory() throws Exception {
        when(studentService.getCompletedTraineeships()).thenReturn(List.of());

        mockMvc.perform(get("/student/history"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("positions"))
                .andExpect(view().name("student/history"));
    }*/
}
