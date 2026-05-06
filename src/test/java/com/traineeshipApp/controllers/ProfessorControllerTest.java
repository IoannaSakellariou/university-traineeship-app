package com.traineeshipApp.controllers;

import com.traineeshipApp.domainmodel.*;
import com.traineeshipApp.services.EvaluationService;
import com.traineeshipApp.services.ProfessorService;
import com.traineeshipApp.services.TraineeshipPositionService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfessorController.class)
class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfessorService professorService;

    @MockBean
    private TraineeshipPositionService traineeshipPositionService;

    @MockBean
    private EvaluationService evaluationService;

    @Test
    @WithMockUser(roles = "PROFESSOR")
    void testDashboardAccess() throws Exception {
        mockMvc.perform(get("/professor/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/dashboard"));
    }
    

    @Test
    @WithMockUser(roles = "PROFESSOR")
    void testEditProfileView() throws Exception {
        Professor professor = new Professor();
        when(professorService.getCurrentProfessor()).thenReturn(professor);
       
        mockMvc.perform(get("/professor/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/edit"))
                .andExpect(model().attributeExists("professor"));
    }

    @Test
    @WithMockUser(roles = "PROFESSOR")
    void testListSupervisedPositions() throws Exception {
    	TraineeshipPosition mockPosition = new TraineeshipPosition();
    	Company company = new Company();
    	company.setCompanyName("TestCo");
    	company.setCompanyLocation("Athens");
    	mockPosition.setCompany(company);
    	when(professorService.getSupervisedPositions()).thenReturn(List.of(mockPosition));

        mockMvc.perform(get("/professor/supervisedpositions"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/supervisedpositions"))
                .andExpect(model().attributeExists("positions"));
    }

    @Test
    @WithMockUser(roles = "PROFESSOR")
    void testEvaluateAssignedPosition_NotCompleted() throws Exception {
        TraineeshipPosition position = new TraineeshipPosition();
        position.setCompleted(false);

        when(traineeshipPositionService.findById(1)).thenReturn(position);
        when(evaluationService.findByPositionAndType(position, EvaluationType.PROFESSOR)).thenReturn(null);

        mockMvc.perform(get("/professor/evaluate/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/evaluationform"))
                .andExpect(model().attributeExists("evaluation"));
    }

    @Test
    @WithMockUser(roles = "PROFESSOR")
    void testEvaluateAssignedPosition_Completed() throws Exception {
        TraineeshipPosition position = new TraineeshipPosition();
        position.setCompleted(true);

        when(traineeshipPositionService.findById(1)).thenReturn(position);

        mockMvc.perform(get("/professor/evaluate/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professor/dashboard?error=completed"));
    }

    @Test
    @WithMockUser(roles = "PROFESSOR")
    void testSaveEvaluation_NotCompleted() throws Exception {
        TraineeshipPosition position = new TraineeshipPosition();
        position.setId(1);
        position.setCompleted(false);

        Evaluation evaluation = new Evaluation();
        evaluation.setTraineeshipPosition(position);

        mockMvc.perform(post("/professor/evaluate")
                        .with(csrf()) 
                        .flashAttr("evaluation", evaluation))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professor/dashboard"));
    }

}
