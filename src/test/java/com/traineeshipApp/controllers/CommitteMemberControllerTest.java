package com.traineeshipApp.controllers;

import org.springframework.security.test.context.support.WithMockUser;

import com.traineeshipApp.domainmodel.Evaluation;
import com.traineeshipApp.domainmodel.EvaluationType;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.StudentMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;
import com.traineeshipApp.services.CommitteeMemberService;
import com.traineeshipApp.services.EvaluationService;
import com.traineeshipApp.services.ProfessorService;
import com.traineeshipApp.services.StudentService;
import com.traineeshipApp.services.TraineeshipPositionService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Import(SecurityConfig.class)

@SpringBootTest
@AutoConfigureMockMvc
class CommitteMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TraineeshipPositionMapper traineeshipPositionMapper;

    
    @MockBean
    private StudentMapper studentMapper;


    @MockBean 
    private CommitteeMemberService committeeMemberService;
    
    @MockBean 
    private StudentService studentService;
    
    @MockBean 
    private ProfessorService professorService;
    
    @MockBean 
    private EvaluationService evaluationService;
    
    @MockBean 
    private TraineeshipPositionService traineeshipPositionService;
    
    @MockBean 
    private com.traineeshipApp.search.PositionsSearchFactory positionsSearchFactory;

    @Test
    @WithMockUser(authorities = "COMMITTEE_MEMBER")
    void testGetDashboard() throws Exception {
        mockMvc.perform(get("/committeemember/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("committeemember/dashboard"));
    }


    @Test
    @WithMockUser(authorities = "COMMITTEE_MEMBER")
    void testInProgressWhenEmpty() throws Exception {
        when(committeeMemberService.findInProgressTraineeships()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/committeemember/inprogress"))
                .andExpect(status().isOk())
                .andExpect(view().name("committeemember/nopositionsinprogress"));
    }

    @Test
    @WithMockUser(authorities = "COMMITTEE_MEMBER")
    void testHistoryView() throws Exception {
        when(traineeshipPositionService.getAllCompleted()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/committeemember/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("committeemember/history"))
                .andExpect(model().attributeExists("positions"));
    }

    @Test
    @WithMockUser(authorities = "COMMITTEE_MEMBER")
    void testCompleteTraineeshipFailsWhenMissingEvaluation() throws Exception {
        TraineeshipPosition position = new TraineeshipPosition();
        when(traineeshipPositionService.findById(1)).thenReturn(position);
        when(evaluationService.findByPositionAndType(position, EvaluationType.COMPANY)).thenReturn(null);

        mockMvc.perform(post("/committeemember/complete/1")
                        .param("pass", "true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committeemember/inprogress"))
                .andExpect(flash().attributeExists("error"));
                
        		
    }

    @Test
    @WithMockUser(authorities = "COMMITTEE_MEMBER")
    void testCompleteTraineeshipSuccess() throws Exception {
        TraineeshipPosition position = new TraineeshipPosition();
        when(traineeshipPositionService.findById(1)).thenReturn(position);
        when(evaluationService.findByPositionAndType(position, EvaluationType.COMPANY)).thenReturn(new Evaluation());
        when(evaluationService.findByPositionAndType(position, EvaluationType.PROFESSOR)).thenReturn(new Evaluation());

        mockMvc.perform(post("/committeemember/complete/1")
                        .param("pass", "true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committeemember/inprogress"))
                .andExpect(flash().attributeExists("success"));
    }

} 
