package com.traineeshipApp.domainmodel;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class CommitteeMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

	
    @Column(name = "username")
    private String username;
    
    
    @OneToMany(mappedBy = "committeeMember")
    private List<TraineeshipPosition> positions; 

    
    @OneToOne
	@JoinColumn(name = "user_id")
	private User user;
    
    public CommitteeMember(Integer id, String username, User user, List<Student> appliedStudents, List<TraineeshipPosition> positions) {
        super();
        this.id = id;
  
        this.username = username;
        this.user = user;
        this.positions = positions;
    }
    
    public CommitteeMember() {
       super();
    }



	public void setUser(User user) {
		this.user = user;
	}
	    
	public User getUser() {return user;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    

   public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
}
