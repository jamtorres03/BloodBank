package com.bloodbank.service;

import com.bloodbank.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
}
