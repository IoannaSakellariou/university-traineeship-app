package com.traineeshipApp.controllers;

import com.traineeshipApp.domainmodel.*;
import com.traineeshipApp.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(CompanyController.class)
public class CompanyControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private CompanyService companyService;
    @MockBean private TraineeshipPositionService traineeshipPositionService;
    @MockBean private CommitteeMemberService committeeMemberService;
    @MockBean private EvaluationService evaluationService;

    @Test
    @WithMockUser(authorities = "COMPANY")
    void testGetDashboard() throws Exception {
        mockMvc.perform(get("/company/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/dashboard"));
    }

    @Test
    @WithMockUser(authorities = "COMPANY")
    void testEditProfile() throws Exception {
        when(companyService.getCurrentCompany()).thenReturn(new Company());

        mockMvc.perform(get("/company/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/edit"))
                .andExpect(model().attributeExists("company"));
    }

    @Test
    @WithMockUser(authorities = "COMPANY")
    void testSaveProfile() throws Exception {
        mockMvc.perform(post("/company/edit")
                .with(csrf())
                .flashAttr("company", new Company()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/dashboard"));
    }

    @Test
    @WithMockUser(authorities = "COMPANY")
    void testListAvailablePositions() throws Exception {
        when(companyService.retrieveAvailablePositions()).thenReturn(List.of(new TraineeshipPosition()));

        mockMvc.perform(get("/company/availablepositions"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("positions"))
                .andExpect(view().name("company/availablepositions"));
    }

    @Test
    @WithMockUser(authorities = "COMPANY")
    void testDeletePosition() throws Exception {
        mockMvc.perform(post("/company/deleteposition/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/availablepositions"));

        verify(traineeshipPositionService).deleteById(1);
    }

    @Test
    @WithMockUser(authorities = "COMPANY")
    void testShowCreatePositionForm() throws Exception {
        mockMvc.perform(get("/company/createposition"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("position"))
                .andExpect(view().name("company/createposition"));
    }

    @Test
    @WithMockUser(authorities = "COMPANY")
    void testCreatePosition() throws Exception {
        when(committeeMemberService.getFirstAvailable()).thenReturn(new CommitteeMember());

        mockMvc.perform(post("/company/createposition")
                .with(csrf())
                .flashAttr("position", new TraineeshipPosition()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/availablepositions"));
    }

    @Test
    @WithMockUser(authorities = "COMPANY")
    void testEvaluateAssignedTraineeship() throws Exception {
        TraineeshipPosition position = new TraineeshipPosition();
        position.setCompleted(false);

        when(traineeshipPositionService.findById(1)).thenReturn(position);
        when(evaluationService.findByPositionAndType(position, EvaluationType.COMPANY)).thenReturn(null);

        mockMvc.perform(get("/company/evaluate/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("evaluation"))
                .andExpect(view().name("company/evaluationform"));
    }

    @Test
    @WithMockUser(authorities = "COMPANY")
    void testSubmitEvaluation() throws Exception {
        mockMvc.perform(post("/company/evaluate")
                .with(csrf())
                .flashAttr("evaluation", new Evaluation()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/dashboard"))
                .andExpect(flash().attribute("success", "Evaluation submitted successfully"));
    }

}
