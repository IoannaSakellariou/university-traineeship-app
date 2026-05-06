package com.traineeshipApp.domainmodel;


import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@SuppressWarnings("serial")
public class User implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Enumerated(EnumType.STRING)
    @Column(name="role")
    private Role role;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Student student;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Professor professor;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Company company;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private CommitteeMember committeeMember;


	
	public User() {
		super();
		
	}
	

	public Integer getId() {
		return this.id;
	}
	

	public  int setId(Integer id) {
		return this.id;
	}

	
	public Student getStudent() {
	    return student;
	}
	public void setStudent(Student student) {
	    this.student = student;
	}

	public Professor getProfessor() {
	    return professor;
	}
	public void setProfessor(Professor professor) {
	    this.professor = professor;
	}

	public Company getCompany() {
	    return company;
	}
	public void setCompany(Company company) {
	    this.company = company;
	}

	public CommitteeMember getCommitteeMember() {
	    return committeeMember;
	}
	public void setCommitteeMember(CommitteeMember committeeMember) {
	    this.committeeMember = committeeMember;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		 SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
	     return Collections.singletonList(authority);
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	

	
	
}
