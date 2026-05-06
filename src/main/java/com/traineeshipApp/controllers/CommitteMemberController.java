package com.traineeshipApp.controllers;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.traineeshipApp.domainmodel.CommitteeMember;
import com.traineeshipApp.domainmodel.Evaluation;
import com.traineeshipApp.domainmodel.EvaluationType;
import com.traineeshipApp.domainmodel.Professor;
import com.traineeshipApp.domainmodel.Student;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.mappers.StudentMapper;
import com.traineeshipApp.mappers.TraineeshipPositionMapper;
import com.traineeshipApp.search.PositionsSearchFactory;
import com.traineeshipApp.services.CommitteeMemberService;
import com.traineeshipApp.services.EvaluationService;
import com.traineeshipApp.services.ProfessorService;
import com.traineeshipApp.services.StudentService;
import com.traineeshipApp.services.TraineeshipPositionService;


import org.springframework.ui.Model;

@PreAuthorize("hasAuthority('ROLE_COMMITTEE_MEMBER')")
@Controller
@RequestMapping("/committeemember")
public class CommitteMemberController {
	
	
	@Autowired
	CommitteeMemberService committeeMemberService;
	
	@Autowired
	StudentService studentService;
	
	@Autowired
	StudentMapper studentMapper;
	
	@Autowired
	ProfessorService professorService;
	
	@Autowired
	EvaluationService evaluationService;
	
	@Autowired
	TraineeshipPositionService traineeshipPositionService;
	
	@Autowired
	TraineeshipPositionMapper traineeshipPositionMapper;
	
	@Autowired
	PositionsSearchFactory positionsSearchFactory;

    // ---------------- DASHBOARD ----------------

    @GetMapping("/dashboard")
    public String getCommitteeDashboard() {
        return "committeemember/dashboard";
    }

   

    // ---------------- APPLICATIONS / US16 ----------------
    @GetMapping("/applications")
    public String listTraineeshipApplications(Model model) {
        CommitteeMember member = committeeMemberService.getCurrentCommitteeMember();
        List<Student> uniqueStudents = committeeMemberService.findStudentsWithApplications(member);
        model.addAttribute("students", uniqueStudents);

        return "committeemember/applications";
    }

    // ---------------- SUGGEST POSITIONS / US17 ----------------
    
    @GetMapping("/suggestpositions")
    public String listStudentsWithoutPosition(Model model) {
        List<Student> students = studentService.findStudentsWithoutAssignedPosition();
        model.addAttribute("students", students);
        return "committeemember/suggestpositions";  // ΝΕΟ HTML
    }

    
    @GetMapping("/suggestpositions/{studentId}")
    public String findPositions(@PathVariable Integer studentId,
                                   @RequestParam(defaultValue = "interests") String strategy,
                                   Model model) {
    	Student student = studentService.findById(studentId);

        List<TraineeshipPosition> suggestions = positionsSearchFactory.create(strategy).search(studentId);

        List<TraineeshipPosition> filtered = suggestions.stream()
                .filter(pos -> student.getAppliedPositions().contains(pos))
                .toList();

        model.addAttribute("studentId", studentId);
        model.addAttribute("studentUsername", student.getUsername());
        model.addAttribute("positions", filtered);
        model.addAttribute("currentStrategy", strategy);

        return "committeemember/suggestpositions";
    }

    // ---------------- ACCEPT / REJECT / US18 ----------------
    
    @PostMapping("/accept/{positionId}/{studentId}")
    public String acceptStudent(@PathVariable Integer positionId,
                                @PathVariable Integer studentId,
                                RedirectAttributes redirectAttributes) {
        try {
            traineeshipPositionService.assignStudentToPosition(positionId, studentId);
                 
            redirectAttributes.addFlashAttribute("success", "The student was successfully assigned to the position.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "The student already has an active traineeship.");
        }
        return "redirect:/committeemember/applications";
    }

    // ---------------- ASSIGN SUPERVISOR / US19 ----------------
    
    @GetMapping("/assignform")
    public String listPositionsWithoutSupervisor(Model model) {
        List<TraineeshipPosition> positions = traineeshipPositionService.findAssignedWithoutSupervisor();
        model.addAttribute("positions", positions);
        return "committeemember/assignform";
    }
    
    @GetMapping("/pendingsupervisors")
    public String viewPositionsPendingSupervisor(Model model) {
        List<TraineeshipPosition> pendingPositions = traineeshipPositionService.findAssignedWithoutSupervisor();
        model.addAttribute("positions", pendingPositions);
        return "committeemember/pendingsupervisors";
    }
    
    @GetMapping("/assign/{positionId}/{studentId}")
    public String showAssignForm(@PathVariable Integer positionId,
                                 @PathVariable Integer studentId,
                                 @RequestParam(defaultValue = "interests") String strategy,
                                 Model model) {
        
        List<Professor> professors = committeeMemberService.getSuggestedProfessors(positionId, strategy);
        

        model.addAttribute("positionId", positionId);
        model.addAttribute("studentId", studentId);
        model.addAttribute("strategy", strategy);
        model.addAttribute("professors", professors);

        return "committeemember/assignform";
    }
  
    @PostMapping("/assign")
    public String assignSupervisor(@RequestParam("positionId") Integer positionId,
                                            @RequestParam("studentId") Integer studentId,
                                            @RequestParam("professorId") Integer professorId) {

        TraineeshipPosition position = traineeshipPositionService.findById(positionId);

        Professor professor = professorService.findById(professorId);

        position.setSupervisor(professor);
        position.setAssigned(true); 

        traineeshipPositionService.save(position);

        return "redirect:/committeemember/inprogress"; 
    }
    
    @GetMapping("/inprogress")
    public String listAssignedTraineeships(Model model) {
        List<TraineeshipPosition> inProgressPositions = committeeMemberService.findInProgressTraineeships();

        if (inProgressPositions == null || inProgressPositions.isEmpty()) {
            return "committeemember/nopositionsinprogress"; 
        }

        model.addAttribute("positions", inProgressPositions);
        return "committeemember/inprogress"; 
    }



    // ---------------- COMPLETE / US21 ----------------
    
    @GetMapping("/evaluate/{id}")
    public String viewEvaluations(@PathVariable("id") Integer id, Model model) {
        TraineeshipPosition position = traineeshipPositionService.findById(id);
        List<Evaluation> evaluations = evaluationService.findByTraineeshipPosition(position); 
        model.addAttribute("position", position);
        model.addAttribute("evaluations", evaluations);
        return "committeemember/evaluationreview";
    }
    
    @PostMapping("/complete/{id}")
    public String completeAssignedTraineeship(@PathVariable Integer id,
                                              @RequestParam("pass") boolean isPass,
                                              RedirectAttributes redirectAttributes) {
        TraineeshipPosition position = traineeshipPositionService.findById(id);

        Evaluation hasCompanyEval = evaluationService.findByPositionAndType(position, EvaluationType.COMPANY);
        Evaluation hasProfessorEval = evaluationService.findByPositionAndType(position, EvaluationType.PROFESSOR);

        if (hasCompanyEval == null || hasProfessorEval == null) {
            redirectAttributes.addFlashAttribute("error", "Cannot complete. Both evaluations are required.");
            return "redirect:/committeemember/inprogress";
        }

        position.setCompleted(true);
        position.setPassFailGrade(isPass);  // <--- ΠΟΛΥ ΣΗΜΑΝΤΙΚΟ
        traineeshipPositionService.save(position);

        redirectAttributes.addFlashAttribute("success", "Traineeship marked as completed (" + (isPass ? "PASS" : "FAIL") + ").");
        return "redirect:/committeemember/inprogress";
    }
    
    @GetMapping("/history")
    public String viewHistory(Model model) {
    	List<TraineeshipPosition> completed = traineeshipPositionService.getAllCompleted();
    	model.addAttribute("positions", completed);
        return "committeemember/history";
    }
}
