package com.example.job_portal.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.job_portal.entity.Application;
import com.example.job_portal.entity.ChatMessage;
import com.example.job_portal.entity.User;
import com.example.job_portal.repository.ApplicationRepository;
import com.example.job_portal.repository.ChatRepository;
import com.example.job_portal.repository.UserRepository;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ApplicationRepository appRepo;

    @Autowired
    private UserRepository userRepo;

    // ✅ GET CONTACTS
    @GetMapping("/contacts")
    public List<User> getContacts(Principal principal) {

        User currentUser = userRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 👇 If EMPLOYER → show applicants
        if (currentUser.getRole().name().equals("EMPLOYER")) {
            return appRepo.findByJobEmployer(currentUser)
                    .stream()
                    .map(Application::getJobSeeker)
                    .distinct()
                    .toList();
        }

        // 👇 If JOB_SEEKER → show employers
        return appRepo.findByJobSeeker(currentUser)
                .stream()
                .map(app -> app.getJob().getEmployer())
                .distinct()
                .toList();
    }
    @Autowired
    private ChatRepository chatRepo;

    @GetMapping("/history/{userId}")
    public List<ChatMessage> getChatHistory(@PathVariable Long userId, Principal principal) {

        User currentUser = userRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return chatRepo.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
                currentUser.getId(), userId,
                currentUser.getId(), userId
        );
    }
}