package com.traineeshipApp.services;

import com.traineeshipApp.domainmodel.User;

public interface UserService {
	public void saveUser(User user);
    public boolean isUserPresent(User user);
	public User findById(String username);
	public User findByUsername(String username);
	
}
