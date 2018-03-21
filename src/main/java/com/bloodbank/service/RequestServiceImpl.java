package com.bloodbank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bloodbank.model.Request;
import com.bloodbank.repository.RequestRepository;

@Service("requestService")
public class RequestServiceImpl implements RequestService {

	@Value("${REQUEST.STATUS.NEW}")
	private static String REQUEST_STATUS_NEW;
	
	@Autowired
	private RequestRepository requestRepository;
	
	@Override
	public void saveRequest(Request request) {
		requestRepository.save(request);
	}

	@Override
	public List<Request> findByStatus(int status) {
		return requestRepository.findByStatus(status);
	}
}
