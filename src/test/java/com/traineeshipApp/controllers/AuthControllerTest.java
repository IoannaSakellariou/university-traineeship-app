package com.traineeshipApp.controllers;

import com.traineeshipApp.domainmodel.Role;
import com.traineeshipApp.domainmodel.User;
import com.traineeshipApp.mappers.CommitteeMemberMapper;
import com.traineeshipApp.mappers.CompanyMapper;
import com.traineeshipApp.mappers.ProfessorMapper;
import com.traineeshipApp.mappers.StudentMapper;
import com.traineeshipApp.services.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.ui.Model;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private StudentMapper studentMapper;
    
    @MockBean
    private CompanyMapper companyMapper;
    
    @MockBean
    private ProfessorMapper professorMapper;
    
    @MockBean
    private CommitteeMemberMapper committeeMemberMapper;
    

    @MockBean
    private UserService userService;

    @MockBean
    private Model model;

    @MockBean
    private Authentication authentication;

    @Test
    void testRegisterPageLoads() throws Exception {
    	mockMvc.perform(post("/save")
    	        .param("username", "newuser")
    	        .param("password", "1234")
    	        .param("role", "STUDENT")
    	        .with(csrf())) 
    			.andExpect(status().isOk());

    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("pass123");
        user.setRole(Role.STUDENT);

        when(userService.isUserPresent(any())).thenReturn(false);

        mockMvc.perform(post("/save")
                        .flashAttr("user", user)
                        .with(csrf())) 
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().attributeExists("successMessage"));
    }

    @Test
    void testRegisterUserAlreadyExists() throws Exception {
        User user = new User();
        user.setUsername("existing");
        user.setPassword("pass123");
        user.setRole(Role.STUDENT);

        when(userService.isUserPresent(any())).thenReturn(true);

        mockMvc.perform(post("/save")
                        .flashAttr("user", user)
        				.with(csrf())) 
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().attributeExists("successMessage"));
    }

    @Test
    void testRegisterUserMissingFields() throws Exception {
        User user = new User();

        mockMvc.perform(post("/save")
                        .flashAttr("user", user)
                        .with(csrf())) 
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    @WithMockUser(username = "studentuser", authorities = {"STUDENT"})
    void testLoginRedirectsToStudentDashboard() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student/dashboard"));
    }
    
    @Test
    @WithMockUser(username = "companytuser", authorities = {"COMPANY"})
    void testLoginRedirectsToCompanyDashboard() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/dashboard"));
    }
    
    @Test
    @WithMockUser(username = "professoruser", authorities = {"PROFESSOR"})
    void testLoginRedirectsToProfessorDashboard() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professor/dashboard"));
    }
    
    @Test
    @WithMockUser(username = "committeememberuser", authorities = {"COMMITTEE_MEMBER"})
    void testLoginRedirectsToCommitteeMemberDashboard() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committeemember/dashboard"));
    }

    @Test
    void testLoginReturnsLoginPageForAnonymous() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }
}
