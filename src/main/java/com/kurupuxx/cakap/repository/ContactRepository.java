package com.kurupuxx.cakap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurupuxx.cakap.model.Contact;
import com.kurupuxx.cakap.model.User;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("SELECT c.userToAdd FROM Contact c WHERE c.user = :user")
    List<User> findAllContact(User user);

    @Query("SELECT c.userToAdd FROM Contact c WHERE c.user = :user AND c.userToAdd = :userToAdd")
    Optional<User> findContactByUserAndUserToAdd(User user, User userToAdd);
}