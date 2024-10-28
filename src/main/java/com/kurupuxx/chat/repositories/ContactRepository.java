package com.kurupuxx.chat.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kurupuxx.chat.entities.Contact;
import com.kurupuxx.chat.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("SELECT c.acceptor FROM Contact c WHERE c.requestor = :requestor")
    List<User> findAllContact(User requestor);

    @Query("SELECT c FROM Contact c WHERE c.requestor = :requestor AND c.acceptor = :acceptor")
    Optional<Contact> findContactByAcceptorAndReceptor(User requestor, User acceptor);
}