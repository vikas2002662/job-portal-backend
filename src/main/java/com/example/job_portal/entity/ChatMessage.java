package com.example.job_portal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long receiverId;

    private String text;

    private Long timestamp; // ✅ ADD THIS

    // getters & setters
}