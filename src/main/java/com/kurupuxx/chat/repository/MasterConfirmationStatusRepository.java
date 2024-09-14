package com.kurupuxx.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kurupuxx.chat.model.MasterConfirmationStatus;

public interface MasterConfirmationStatusRepository extends JpaRepository<MasterConfirmationStatus, Long>{
    
}
