package com.traineeshipApp.controllers;


import com.traineeshipApp.domainmodel.CommitteeMember;
import com.traineeshipApp.domainmodel.Company;
import com.traineeshipApp.domainmodel.Evaluation;
import com.traineeshipApp.domainmodel.EvaluationType;
import com.traineeshipApp.domainmodel.TraineeshipPosition;
import com.traineeshipApp.services.CommitteeMemberService;
import com.traineeshipApp.services.CompanyService;
import com.traineeshipApp.services.EvaluationService;
import com.traineeshipApp.services.TraineeshipPositionService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@PreAuthorize("hasAuthority('COMPANY')")
@Controller
@RequestMapping("/company")
public class CompanyController {
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	TraineeshipPositionService traineeshipPositionService;
	
	@Autowired
	CommitteeMemberService committeeMemberService;
	
	@Autowired
	EvaluationService evaluationService;

	
	@GetMapping("/dashboard")
    public String getCompanyDashboard(Model model) {
   	 	return "company/dashboard";
    }

	  
	 @GetMapping("/edit")
	 public String editProfile(Model model, Authentication authentication) {
		Company company = companyService.getCurrentCompany();      
	    model.addAttribute("company", company);
	    return "company/edit";
	 }
	 
	
	  	
	 @PostMapping("/edit")
	 public String saveProfile(@ModelAttribute("company") Company companyForm, Model model) {
		 try {
			 companyService.updateCurrentCompany(companyForm);
	  	     return "redirect:/company/dashboard";
	  	 } catch (Exception e) {
	  	     model.addAttribute("error", "Error while saving profile..");
	  	     return "company/edit";
	  	 }
	 }
	 
	 @GetMapping("/availablepositions")
	 public String listAvailablePositions(Model model) {
	     List<TraineeshipPosition> positions = companyService.retrieveAvailablePositions()
	             .stream()
	             .filter(pos -> !pos.isAssigned())
	             .toList();

	     model.addAttribute("positions", positions);
	     return "company/availablepositions";
	 }

	 
	 @PostMapping("/deleteposition/{id}")
	 public String deletePosition(@PathVariable Integer id) {
	     traineeshipPositionService.deleteById(id);
	     return "redirect:/company/availablepositions";
	 }
	 
	 @GetMapping("/createposition")
	 public String showPositionForm(Model model) {
	     model.addAttribute("position", new TraineeshipPosition());
	     return "company/createposition"; 
	 }

	 @PostMapping("/createposition")
	 public String showPosition(@ModelAttribute("position") TraineeshipPosition position) {
	     CommitteeMember cm = committeeMemberService.getFirstAvailable();
	     companyService.createPositionForCurrentCompany(position, cm);
	     return "redirect:/company/availablepositions";
	 }

	 
	 @GetMapping("/assignedpositions")
	 public String listAssignedPositions(Model model, Principal principal) {
	     Company company = companyService.findByUsername(principal.getName());
	     List<TraineeshipPosition> assignedPositions = traineeshipPositionService.getAssignedPositionsByCompany(company);
	     model.addAttribute("positions", assignedPositions);
	     return "company/assignedpositions";
	 }
	 
	 @GetMapping("/evaluate/{positionId}")
	 public String evaluateAssignedTraineeship(@PathVariable Integer positionId, Model model) {
	     TraineeshipPosition position = traineeshipPositionService.findById(positionId);

	     if (position.isCompleted()) {
	         return "redirect:/company/dashboard?error=completed";
	     }
	     Optional<Evaluation> existingEval = Optional.ofNullable(
	         evaluationService.findByPositionAndType(position, EvaluationType.COMPANY)
	     );

	     Evaluation evaluation = existingEval.orElseGet(() -> {
	         Evaluation e = new Evaluation();
	         e.setTraineeshipPosition(position);
	         e.setEvaluationType(EvaluationType.COMPANY);
	         return e;
	     });

	     model.addAttribute("evaluation", evaluation);
	     return "company/evaluationform";
	 }
	 
	 @PostMapping("/evaluate")
	 public String submitEvaluation(@ModelAttribute Evaluation evaluation, RedirectAttributes redirectAttributes) {
	     evaluationService.saveOrUpdateEvaluation(evaluation);

	     redirectAttributes.addFlashAttribute("success", "Evaluation submitted successfully");
	     return "redirect:/company/dashboard";
	 }


}
