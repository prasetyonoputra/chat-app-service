package com.kurupuxx.chat.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kurupuxx.chat.entities.MasterConfirmationStatus;

public interface MasterConfirmationStatusRepository extends JpaRepository<MasterConfirmationStatus, Long>{
    Optional<MasterConfirmationStatus> findByValue(String value);
}
