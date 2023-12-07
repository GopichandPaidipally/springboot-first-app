package com.springboot.springbootproductapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.springbootproductapp.model.ApprovalQueue;

public interface ApprovalQueueRespository extends JpaRepository<ApprovalQueue, Long>{

	void save(Optional<ApprovalQueue> existingApproval);
	

}
