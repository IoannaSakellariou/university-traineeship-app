package com.traineeshipApp.controllers;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;


import com.traineeshipApp.domainmodel.Role;
import com.traineeshipApp.domainmodel.User;
import com.traineeshipApp.mappers.CommitteeMemberMapper;
import com.traineeshipApp.mappers.CompanyMapper;
import com.traineeshipApp.mappers.ProfessorMapper;
import com.traineeshipApp.mappers.StudentMapper;
import com.traineeshipApp.services.UserService;
import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.Professor;
import com.traineeshipApp.domainmodel.Company;
import com.traineeshipApp.domainmodel.CommitteeMember;


@Controller
public class AuthController {
    @Autowired
    UserService userService;
    
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ProfessorMapper professorMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CommitteeMemberMapper committeeMemberMapper;

    @RequestMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            String role = authentication.getAuthorities().iterator().next().getAuthority();

            switch (role) {
                case "STUDENT":
                	System.out.println("edo");
                    return "redirect:/student/dashboard";
                case "PROFESSOR":
                    return "redirect:/professor/dashboard";
                case "COMPANY":
                    return "redirect:/company/dashboard";
                case "COMMITTEE_MEMBER":
                    return "redirect:/committeemember/dashboard";
                case "ADMIN":
                    return "redirect:/admin/dashboard";
                case "USER":
                    return "redirect:/user/dashboard";
                default:
                    return "redirect:/";
            }
            
        }
        

        return "auth/login";
    }

    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());  
        return "auth/register"; 
    }

    @RequestMapping("/save")
    public String registerUser(@ModelAttribute("user") User user, Model model) {

        if (userService.isUserPresent(user)) {
            model.addAttribute("successMessage", "User already registered! Please log in.");
            return "auth/login";
        }

        String selectedRole = user.getRole() != null ? user.getRole().toString() : null;

        if (selectedRole == null || selectedRole.isEmpty()) {
            model.addAttribute("errorMessage", "Role is required!");
            return "auth/register";
        }

        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            model.addAttribute("errorMessage", "All fields are required.");
            return "auth/register";
        }

        userService.saveUser(user);

        
        switch (user.getRole()) {
            case STUDENT -> {
                Student student = new Student();
                student.setUser(user);  
                student.setUsername(user.getUsername());
                studentMapper.save(student);
                System.out.println("Student saved: " + student.getUsername());
            }
            case PROFESSOR -> {
                Professor professor = new Professor();
                professor.setUser(user);
                professor.setUsername(user.getUsername());
                professorMapper.save(professor);
            }
            case COMPANY -> {
                Company company = new Company();
                company.setUser(user);
                company.setUsername(user.getUsername());
                companyMapper.save(company);
            }
            case COMMITTEE_MEMBER -> {
                CommitteeMember committeeMember = new CommitteeMember();
                committeeMember.setUser(user);
                committeeMember.setUsername(user.getUsername());
                committeeMemberMapper.save(committeeMember);
            }default -> {
                System.out.println("No specific entity for role: " + user.getRole());
            }
        }


        model.addAttribute("successMessage", "User registered successfully! You can now log in.");
        return "auth/login";
    }
}
