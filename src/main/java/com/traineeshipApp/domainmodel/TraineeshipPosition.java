package com.traineeshipApp.domainmodel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.*;

@Entity
public class TraineeshipPosition {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	private String title;
    private String description;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String topics;
    private String skills; 
    
    private boolean isAssigned = false;
    private String studentLogBook;
    private boolean passFailGrade;
    private boolean completed;
    
    @Column(name = "assigned_student_am")
    private String assignedStudentAM;

    

    @OneToOne
    @JoinColumn(name = "student_id")//foreign key
    private Student student; //assignedStudent

    @ManyToOne
    @JoinColumn(name = "supervisor_id") //foreign key
    private Professor supervisor;

    @ManyToOne
    @JoinColumn(name = "company_id") //foreign key
    private Company company;

  
    @OneToMany(mappedBy = "traineeshipPosition")
    private List<Evaluation> evaluations;
    
    
    @ManyToOne
    @JoinColumn(name = "committee_member_id")  
    private CommitteeMember committeeMember;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "position_applications",
        joinColumns = @JoinColumn(name = "position_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> applicants = new ArrayList<>();


    public CommitteeMember getCommitteeMember() {
        return committeeMember;
    }

    public void setCommitteeMember(CommitteeMember committeeMember) {
        this.committeeMember = committeeMember;
    }
    
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    
    public List<Student> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<Student> applicants) {
        this.applicants = applicants;
    }

   
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

  
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

 
    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    
    public String getStudentLogBook() {
        return studentLogBook;
    }

    public void setStudentLogBook(String studentLogBook) {
        this.studentLogBook = studentLogBook;
    }

   
    public boolean isPassFailGrade() {
        return passFailGrade;
    }

    public void setPassFailGrade(boolean passFailGrade) {
        this.passFailGrade = passFailGrade;
    }


    public Professor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Professor supervisor) {
        this.supervisor = supervisor;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    
 
    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
    
    public boolean isCompleted() {
    	return completed;
    }
    
    public void setCompleted(boolean completed) {
    	this.completed = completed;
    }
    
    public String getAssignedStudentAM() {
        return assignedStudentAM;
    }

    public void setAssignedStudentAM(String assignedStudentAM) {
        this.assignedStudentAM = assignedStudentAM;
    }

 
    
    public Set<String> getTopicsAsSet() {
        if (this.topics == null || this.topics.isBlank()) return Set.of();
        return Arrays.stream(this.topics.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .collect(Collectors.toSet());
    }

    public Set<String> getSkillsAsSet() {
        if (this.skills == null || this.skills.isBlank()) return Set.of();
        return Arrays.stream(this.skills.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .collect(Collectors.toSet());
    }

}
