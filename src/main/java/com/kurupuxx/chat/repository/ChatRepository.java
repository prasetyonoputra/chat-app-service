package com.kurupuxx.chat.repository;

import com.kurupuxx.chat.model.Chat;
import com.kurupuxx.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE (c.userSender = :userSender AND c.userReceiver = :userReceiver) OR (c.userSender = :userReceiver AND c.userReceiver = :userSender)")
    List<Chat> findByUserSenderAndUserReceiver(User userSender, User userReceiver);
}
