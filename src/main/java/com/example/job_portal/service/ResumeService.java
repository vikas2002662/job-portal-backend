package com.example.job_portal.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.job_portal.entity.Resume;
import com.example.job_portal.entity.User;
import com.example.job_portal.repository.ResumeRepository;
import com.example.job_portal.repository.UserRepository;

@Service
public class ResumeService {

	private final String UPLOAD_DIR = "D:/job-portal/uploads/";


    @Autowired
    private ResumeRepository resumeRepo;

    @Autowired
    private UserRepository userRepo;

    public String uploadResume(MultipartFile file, String email) throws IOException {

        // 🔒 STEP 1: Validate file (ADD HERE)
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

        // 👇 Existing logic continues
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fileName = System.currentTimeMillis() + "_" + originalName;
        String filePath = UPLOAD_DIR + fileName;

        File dest = new File(filePath);

        // (Optional but important) create folder if not exists
        dest.getParentFile().mkdirs();

        file.transferTo(dest);

        Resume resume = resumeRepo.findByUser(user)
                .orElse(new Resume());

        resume.setFileName(fileName);
        resume.setFilePath(filePath);
        resume.setFileType(file.getContentType());
        resume.setUser(user);

        resumeRepo.save(resume);

        return "Resume uploaded successfully";
    }

}
