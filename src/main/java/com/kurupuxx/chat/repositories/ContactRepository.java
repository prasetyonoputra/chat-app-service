package com.kurupuxx.chat.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurupuxx.chat.entities.Contact;
import com.kurupuxx.chat.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("SELECT c.userToAdd FROM Contact c WHERE c.user = :user")
    List<User> findAllContact(User user);

    @Query("SELECT c FROM Contact c WHERE c.user = :user AND c.userToAdd = :userToAdd")
    Optional<Contact> findContactByUserAndUserToAdd(User user, User userToAdd);
}