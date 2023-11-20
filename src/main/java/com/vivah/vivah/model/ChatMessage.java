package com.vivah.vivah.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
@Data
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
    

    private String content;
    
    @ManyToOne
    private Userchat userId;

    @ManyToOne
    private Userchat sender;

    @ManyToOne
    private Userchat receiver;


    private LocalDateTime timestamp;
    // Other message properties
}