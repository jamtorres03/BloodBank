package com.bloodbank.service;

import java.util.Date;
import java.util.UUID;

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
		request.setStatus(1);
		request.setRequestDate(new Date());
		request.setUuid(UUID.randomUUID().toString());
		requestRepository.save(request);
	}

}
