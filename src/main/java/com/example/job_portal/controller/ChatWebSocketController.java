package com.example.job_portal.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.job_portal.entity.ChatMessage;
import com.example.job_portal.entity.User;
import com.example.job_portal.repository.ChatRepository;
import com.example.job_portal.repository.UserRepository;

@Controller
public class ChatWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatRepository chatRepo;

    @Autowired
    private UserRepository userRepo;

    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message, Principal principal) {

        // ✅ FIX 1: senderId ko JWT se set karo — frontend par trust mat karo
        User sender = userRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        message.setSenderId(sender.getId());

        // ✅ FIX 2: Timestamp set karo aur save karo
        message.setTimestamp(System.currentTimeMillis());
        chatRepo.save(message);

        // ✅ FIX 3: Receiver ka EMAIL use karo (Spring routing email se hoti hai, ID se nahi)
        User receiver = userRepo.findById(message.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        messagingTemplate.convertAndSendToUser(
                receiver.getEmail(),   // ✅ Yahi tha main bug — pehle .toString() tha ID ka
                "/queue/messages",
                message
        );
    }
}