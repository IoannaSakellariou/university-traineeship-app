package com.traineeshipApp.domainmodel;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "companies")
public class Company {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 
	
	
	@Column(name="username")
	String username;
	
	@Column(name="companyname")
	String companyName;
	
	@Column(name="companylocation")
	String companyLocation;
	
	
	@OneToMany(mappedBy = "company")
	List<TraineeshipPosition> positions;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	public Company(String username, String companyName, String companyLocation, User user, List<TraineeshipPosition> positions) {
	    super();
		this.username = username;
	    this.companyName = companyName;
	    this.companyLocation = companyLocation;
	    this.user = user;
	    this.positions = positions;
	}
	
	public Company() {
	    super();
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
	  
	
	public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public List<TraineeshipPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<TraineeshipPosition> positions) {
        this.positions = positions;
    }
}
	

