package com.traineeshipApp.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.services.StudentService;
import com.traineeshipApp.services.TraineeshipPositionService;

@PreAuthorize("hasRole('STUDENT')")
@RequestMapping("/student")
@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TraineeshipPositionService traineeshipPositionService;

    @GetMapping("/dashboard")
    public String getStudentDashboard(Model model) {
        Student student = studentService.getCurrentStudent();
        TraineeshipPosition position = student.getAssignedTraineeship(); 
        
        model.addAttribute("position", position);
        return "student/dashboard";
    }



    @GetMapping("/edit")
    public String editProfile(Model model) {
        model.addAttribute("student", getCurrentStudent());
        return "student/edit";
    }

    @PostMapping("/edit")
    public String saveProfile(@ModelAttribute("student") Student studentForm, Model model) {
        try {
            studentService.updateCurrentStudent(studentForm);
            return "redirect:/student/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Σφάλμα κατά την αποθήκευση του προφίλ.");
            return "student/edit";
        }
    }
    
    @GetMapping("/availablepositions")
    public String listAvailablePositions(Model model) {
        model.addAttribute("positions", studentService.getAvailablePositionsForCurrentStudent());
        return "student/availablepositions";
    }


    @PostMapping("/apply/{positionId}")
    public String applyForPosition(@PathVariable Integer positionId, Principal principal) {
        studentService.applyForPosition(principal.getName(), positionId);
        return "redirect:/student/availablepositions";
    }

    
    @GetMapping("/myapplications")
    public String viewApplications(Model model) {
        List<TraineeshipPosition> positions = studentService.getApplicationsForCurrentStudent();
        model.addAttribute("positions", positions);
        return "student/myapplications";
    }
    
    @GetMapping("/history")
    public String viewHistory(Model model) {
        List<TraineeshipPosition> completed = studentService.getCompletedTraineeships();
        model.addAttribute("positions", completed);
        return "student/history";
    }

    @GetMapping("/logbook")
    public String viewLogbook(Model model) {
        Optional<TraineeshipPosition> positionOpt = traineeshipPositionService.findOptionalByStudent(getCurrentStudent());
        
        if (positionOpt.isEmpty()) {
            return "student/no-position";
        }
        TraineeshipPosition position = positionOpt.get();

        if (position.isCompleted()) {
        	return "student/no-position";
        }
        model.addAttribute("position", position);
        return "student/logbook";
    }

    
    @PostMapping("/logbook")
    public String updateLogbook(@RequestParam("logbookText") String logbookText) {
        studentService.appendToLogbook(logbookText);
        return "redirect:/student/logbook";
    }


    private Student getCurrentStudent() {
        return studentService.getCurrentStudent();
    }
}
