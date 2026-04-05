package com.example.job_portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.job_portal.entity.Resume;
import com.example.job_portal.entity.User;
import com.example.job_portal.repository.ResumeRepository;
import com.example.job_portal.repository.UserRepository;
import com.example.job_portal.service.ResumeService;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;

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
                        Authentication auth) throws IOException {

                String email = auth.getName();
                return service.uploadResume(file, email);
        }

        @GetMapping("/download")
        public ResponseEntity<Resource> download(Authentication auth) throws IOException {

                String email = auth.getName();

                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Resume resume = resumeRepo.findByUser(user)
                                .orElseThrow(() -> new RuntimeException("Resume not found"));

                Path path = Paths.get(resume.getFilePath());
                Resource resource = new UrlResource(path.toUri());

                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=" + resume.getFileName())
                                .body(resource);
        }

        @GetMapping("/view/{userId}")
        public ResponseEntity<Resource> viewResume(@PathVariable Long userId) throws IOException {

                User user = userRepo.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Resume resume = resumeRepo.findByUser(user)
                                .orElseThrow(() -> new RuntimeException("Resume not found"));

                Path path = Paths.get(resume.getFilePath());
                Resource resource = new UrlResource(path.toUri());

                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                                "inline; filename=" + resume.getFileName())
                                .body(resource);
        }

}
