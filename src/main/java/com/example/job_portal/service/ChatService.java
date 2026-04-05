package com.example.job_portal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_portal.entity.ChatMessage;
import com.example.job_portal.repository.ChatRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository repo;

    // ✅ SAVE MESSAGE
    public ChatMessage saveMessage(ChatMessage msg) {

        msg.setTimestamp(System.currentTimeMillis());

        return repo.save(msg);
    }

    // ✅ GET FULL CHAT HISTORY
    public List<ChatMessage> getMessages(Long user1, Long user2) {

        return repo
                .findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
                        user1, user2,
                        user1, user2);
    }
}