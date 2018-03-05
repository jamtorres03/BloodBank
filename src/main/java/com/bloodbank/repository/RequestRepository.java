package com.bloodbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bloodbank.model.Request;

@Repository("requestRepository")
public interface RequestRepository extends JpaRepository<Request, Long> {
}
