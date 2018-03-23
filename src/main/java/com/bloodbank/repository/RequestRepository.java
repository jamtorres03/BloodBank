package com.bloodbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bloodbank.model.Request;

@Repository("requestRepository")
public interface RequestRepository extends JpaRepository<Request, Long> {
	List<Request> findByStatus(int status);
	Request findByUuid(String uuid);
	
	@Query("select r from Request r inner join r.requestedBy ar where ar.id = :id")
	List<Request> findByRequestedBy(@Param("id") int id);
	
	@Query("select r from Request r inner join r.acceptedBy ar where ar.id = :id")
	List<Request> findByAcceptedBy(@Param("id") int id);
}
