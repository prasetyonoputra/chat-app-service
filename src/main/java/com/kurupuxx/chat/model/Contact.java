package com.kurupuxx.chat.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idUserToAdd")
    private User userToAdd;
    
    @ManyToOne
    @JoinColumn(name = "idConfirmationStatus")
    private MasterConfirmationStatus confirmationStatus;

    private Date createdAt;
    private Date updatedAt;
    private String updatedBy;
}
