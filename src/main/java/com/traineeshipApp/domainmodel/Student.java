package com.traineeshipApp.domainmodel;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.*;



@Entity
@Table(name = "student")
public class Student{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	
	@Column(name="username")
	private String username;

	
	@Column(name = "studentname")
    private String studentName;
	
	@Column(name = "AM")
    private String AM;
	
	@Column(name = "avgGrade")
    private double avgGrade;
	
	@Column(name = "preferredLocation")
    private String preferredLocation;
	
	@Column(name = "interests")
    private String interests;
	
	@Column(name = "skills")
    private String skills;
	
	@Column(name = "lookingForTraineeship")
    private boolean lookingForTraineeship;
   
    @OneToOne
    @JoinColumn(name = "assigned_traineeship_id")
    private TraineeshipPosition assignedTraineeship;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }
    
    @ManyToMany(mappedBy = "applicants")
    private List<TraineeshipPosition> appliedPositions;

    
    public User getUser() {return user;}
  
	public Student(Integer id, String username, String studentName,
			String AM, double avgGrade,String preferredLocation, 
			String interests, String skills, boolean lookingForTraineeship, TraineeshipPosition assignedTraineeship){
		super();
		this.id = id;
		this.username = username;
		this.studentName = studentName;
		this.AM =AM;
		this.avgGrade = avgGrade;
		this.preferredLocation = preferredLocation;
		this.interests = interests;
		this.skills =skills;
		this.lookingForTraineeship = lookingForTraineeship;
		this.assignedTraineeship = assignedTraineeship;
	}
	
	public Student() {
		super();
	}
	
	public Integer getId() { return id; }
	
	public void setId(Integer id) {this.id = id;	}

	public List<TraineeshipPosition> getAppliedPositions() { return appliedPositions; }
	

	
    public void setAppliedPositionse(List<TraineeshipPosition> appliedPositions) { this.appliedPositions = appliedPositions; }
	
	
	public String getUsername() { return username; }
	

	
    public void setUsername(String username) { this.username = username; }
	
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getAM() {
        return AM;
    }

    public void setAM(String AM) {
        this.AM = AM;
    }
   
    public double getAvgGrade() {
        return avgGrade;
    }

    public void setAvgGrade(double avgGrade) {
        this.avgGrade = avgGrade;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }
    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public boolean isLookingForTraineeship() {
        return lookingForTraineeship;
    }

    public void setLookingForTraineeship(boolean lookingForTraineeship) {
        this.lookingForTraineeship = lookingForTraineeship;
    }


    public TraineeshipPosition getAssignedTraineeship() {
        return assignedTraineeship;
    }

    public void setAssignedTraineeship(TraineeshipPosition assignedTraineeship) {
        this.assignedTraineeship = assignedTraineeship;
    }
   

    
    public Set<String> getInterestsAsSet() {
        if (this.interests == null || this.interests.isBlank()) return Set.of();
        return Arrays.stream(this.interests.split(","))
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
    
    public Set<String> getPreferredLocationsAsSet() {
        if (this.preferredLocation == null || this.preferredLocation.isBlank()) return Set.of();
        return Arrays.stream(this.preferredLocation.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .collect(Collectors.toSet());
    }



}
