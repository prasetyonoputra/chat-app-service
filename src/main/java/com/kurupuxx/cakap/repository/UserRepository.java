package com.kurupuxx.cakap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kurupuxx.cakap.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}