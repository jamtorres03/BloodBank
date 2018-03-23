package com.bloodbank.service;

import java.util.List;

import com.bloodbank.model.Request;
import com.bloodbank.model.User;

public interface RequestService {
	public void saveRequest(Request request);
	public List<Request> findByStatus(int status);
	public Request findByUuid(String uuid);
	public List<Request> findByRequestedBy(User requestedBy);
	public List<Request> findByAcceptedBy(User acceptedBy);
}
