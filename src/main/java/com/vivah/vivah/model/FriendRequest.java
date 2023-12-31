//package com.vivah.vivah.model;
//
//import com.vivah.vivah.modeltwo.User;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.Data;
//
//@Entity
//@Data
//public class FriendRequest {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "sender_id")
//    private User sender;
//
//    @ManyToOne
//    @JoinColumn(name = "receiver_id")
//    private User receiver;
//
//    @Enumerated(EnumType.STRING)
//    private RequestStatus status;
//
//}
