package com.example.job_portal.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.job_portal.entity.Resume;
import com.example.job_portal.entity.User;
import com.example.job_portal.repository.ResumeRepository;
import com.example.job_portal.repository.UserRepository;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private Cloudinary cloudinary;

    public String uploadResume(MultipartFile file, String email) {

        // 🔒 Validation
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        if (file.getContentType() == null ||
                !file.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Only PDF allowed");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.toLowerCase().endsWith(".pdf")) {
            throw new RuntimeException("Only PDF files allowed");
        }

        // 👇 User fetch
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            // 🚀 Cloudinary Upload (SIGNED)
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "folder", "resumes"));

            String fileUrl = uploadResult.get("secure_url").toString();

            // 💾 Save in DB
            Resume resume = resumeRepo.findByUser(user)
                    .orElse(new Resume());

            resume.setFileName(originalName);
            resume.setFilePath(fileUrl);
            resume.setFileType(file.getContentType());
            resume.setUser(user);

            resumeRepo.save(resume);

            return fileUrl;

        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }
}