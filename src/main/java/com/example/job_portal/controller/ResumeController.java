package com.example.job_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.example.job_portal.entity.Resume;
import com.example.job_portal.entity.User;
import com.example.job_portal.repository.ResumeRepository;
import com.example.job_portal.repository.UserRepository;
import com.example.job_portal.service.ResumeService;

@RestController
@RequestMapping("/resume")
public class ResumeController {

        @Autowired
        private ResumeService service;

        @Autowired
        private UserRepository userRepo;

        @Autowired
        private ResumeRepository resumeRepo;

        @PostMapping("/upload")
        public String upload(@RequestParam("file") MultipartFile file,
                        Authentication auth) {

                String email = auth.getName();
                return service.uploadResume(file, email);
        }

        @GetMapping("/download")
        public ResponseEntity<String> download(Authentication auth) {

                String email = auth.getName();

                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Resume resume = resumeRepo.findByUser(user)
                                .orElseThrow(() -> new RuntimeException("Resume not found"));

                return ResponseEntity.ok(resume.getFilePath());
        }

        @GetMapping("/view/{userId}")
        public ResponseEntity<String> viewResume(@PathVariable Long userId) {

                User user = userRepo.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Resume resume = resumeRepo.findByUser(user)
                                .orElseThrow(() -> new RuntimeException("Resume not found"));

                return ResponseEntity.ok(resume.getFilePath());
        }
}