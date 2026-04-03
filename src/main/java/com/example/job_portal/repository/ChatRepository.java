package com.example.job_portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.job_portal.entity.ChatMessage;
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> 
    findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
        Long senderId, Long receiverId,
        Long receiverId2, Long senderId2
    );
}