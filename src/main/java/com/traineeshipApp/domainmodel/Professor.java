package com.traineeshipApp.domainmodel;



import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.*;

@Entity
@Table(name = "professors")
public class Professor{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	
	@Column(name="username")
    private String username;
	
	
	@Column(name="professorName")
    private String professorName;
	
	@Column(name="interests")
    private String interests;

    @OneToMany(mappedBy = "supervisor")
    private List<TraineeshipPosition> supervisedPositions;
    
    @OneToOne
	@JoinColumn(name = "user_id")
	private User user;
    
    public Professor(Integer id, String username, String professorName, String interests, User user, List<TraineeshipPosition> supervisedPositions) {
        super();
    	this.id = id;
        this.username = username;
        this.professorName = professorName;
        this.interests = interests;
        this.user = user;
        this.supervisedPositions = supervisedPositions;
    }
    
    public Professor() {
    	super() ;
    }
    
    public String getUsername() { return username; }
	

	
    public void setUsername(String username) { this.username = username; }
	


    public void setId(Integer id) {
		this.id = id;
	}
	    
	public Integer getId() {return id;}

    
	public void setUser(User user) {
		this.user = user;
	}
	    
	public User getUser() {return user;}
    
   
    
	public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    
    public List<TraineeshipPosition> getSupervisedPositions() {
        return supervisedPositions;
    }

    public void setSupervisedPositions(List<TraineeshipPosition> supervisedPositions) {
        this.supervisedPositions = supervisedPositions;
    }
    
    public int getSupervisedCount() {
        return supervisedPositions != null ? supervisedPositions.size() : 0;
    }
    
    public Set<String> getInterestsAsSet() {
        if (this.interests == null || this.interests.isBlank()) return Set.of();
        return Arrays.stream(this.interests.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .collect(Collectors.toSet());
    }



  
}
