package com.kurupuxx.cakap.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private String pathFile;

    @ManyToOne
    @JoinColumn(name = "userSenderId")
    private User userSender;
    @ManyToOne
    @JoinColumn(name = "userReceiverId")
    private User userReceiver;

    private Date createdAt;
    private Date updatedAt;
    private String updatedBy;
}
