package com.kurupuxx.cakap.repository;

import com.kurupuxx.cakap.model.Chat;
import com.kurupuxx.cakap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE c.userSender = :userSender AND c.userReceiver = :userReceiver")
    List<Chat> findByUserSenderAndUserReceiver(User userSender, User userReceiver);
}
