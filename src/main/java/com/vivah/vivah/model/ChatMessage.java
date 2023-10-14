package com.vivah.vivah.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
@Data
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    
    @ManyToOne
    private Userchat sender; // Use Userchat as the type
    
    @ManyToOne
    private Userchat receiver; // Use Userchat as the type
    
    private LocalDateTime timestamp;
    // Other message properties
}