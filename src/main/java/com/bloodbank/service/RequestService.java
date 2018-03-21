package com.bloodbank.service;

import java.util.List;

import com.bloodbank.model.Request;

public interface RequestService {
	public void saveRequest(Request request);
	public List<Request> findByStatus(int status);
}
