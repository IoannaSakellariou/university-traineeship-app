package com.traineeshipApp.controllers;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.traineeshipApp.domainmodel.Evaluation;
import com.traineeshipApp.domainmodel.EvaluationType;
import com.traineeshipApp.domainmodel.Professor;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.services.ProfessorService;
import com.traineeshipApp.services.TraineeshipPositionService;
import com.traineeshipApp.services.EvaluationService;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired 
    private ProfessorService professorService;
    
    @Autowired 
    private TraineeshipPositionService traineeshipPositionService;
    
    @Autowired 
    private EvaluationService evaluationService;

    // ---------------- DASHBOARD ----------------

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
    
        return "professor/dashboard";
    }

    // ---------------- PROFILE ----------------

    @GetMapping("/edit")
    public String editProfile(Model model) {
        Professor professor = professorService.getCurrentProfessor();
        model.addAttribute("professor", professor);
        return "professor/edit";
    }

    @PostMapping("/edit")
    public String saveProfile(@ModelAttribute("professor") Professor form, Model model) {
        try {
            professorService.updateCurrentProfessor(form);
            return "redirect:/professor/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error while saving profile.");
            return "professor/edit";
        }
    }

    // ---------------- SUPERVISED POSITIONS (US13) ----------------

    @GetMapping("/supervisedpositions")
    public String listSupervisedTraineeships(Model model) {
        List<TraineeshipPosition> positions = professorService.getSupervisedPositions();
        model.addAttribute("positions", positions);
        return "professor/supervisedpositions";
    }


    // ---------------- EVALUATION FORM (US14–US15) ----------------
   
    @GetMapping("/evaluate/{id}")
    public String evaluateAssignedPosition(@PathVariable("id") Integer id, Model model) {
        TraineeshipPosition position = traineeshipPositionService.findById(id);

        if (position.isCompleted()) {
            return "redirect:/professor/dashboard?error=completed";
        }

        Optional<Evaluation> existing = Optional.ofNullable(evaluationService.findByPositionAndType(position, EvaluationType.PROFESSOR));
        Evaluation evaluation = existing.orElseGet(() -> {
            Evaluation e = new Evaluation();
            e.setTraineeshipPosition(position);
            e.setEvaluationType(EvaluationType.PROFESSOR);
            return e;
        });

        model.addAttribute("evaluation", evaluation);
        return "professor/evaluationform";
    }
    
    @PostMapping("/evaluate")
    public String saveEvaluation(@ModelAttribute Evaluation evaluation) {
        TraineeshipPosition position = evaluation.getTraineeshipPosition();

        if (position.isCompleted()) {
            return "redirect:/professor/dashboard?error=completed";
        }

        evaluation.setEvaluationType(EvaluationType.PROFESSOR);
        evaluationService.saveOrUpdateEvaluation(evaluation);
        return "redirect:/professor/dashboard";
    }

}
