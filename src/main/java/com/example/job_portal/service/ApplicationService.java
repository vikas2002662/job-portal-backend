package com.example.job_portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_portal.entity.Application;
import com.example.job_portal.entity.ApplicationStatus;
import com.example.job_portal.entity.Job;
import com.example.job_portal.entity.User;
import com.example.job_portal.exception.DuplicateApplicationException;
import com.example.job_portal.repository.ApplicationRepository;
import com.example.job_portal.repository.JobRepository;
import com.example.job_portal.repository.UserRepository;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository appRepo;

    @Autowired
    private JobRepository jobRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    public Application applyJob(Long jobId, String userEmail) {

        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (appRepo.existsByJobAndJobSeeker(job, user)) {
            throw new DuplicateApplicationException("Already applied!");
        }

        Application app = new Application();
        app.setJob(job);
        app.setJobSeeker(user);

        Application savedApp = appRepo.save(app);

        // 🔥 EMAIL SAFE BLOCK
        try {

            // 📧 Email to Job Seeker
            String subject1 = "Application Submitted Successfully";

            String message1 =
                    "Dear " + user.getName() + ",\n\n" +

                    "You have successfully applied for the job.\n\n" +

                    "Job Details:\n" +
                    "Job Title: " + job.getTitle() + "\n" +
                    "Location: " + job.getLocation() + "\n" +
                    "Salary: ₹" + job.getSalary() + "\n\n" +

                    "Your application has been sent to the employer.\n" +
                    "You will receive further updates once the employer reviews your application.\n\n" +

                    "Thank you for using Job Portal.\n\n" +

                    "Best Regards,\n" +
                    "Job Portal Team";

            emailService.sendEmail(user.getEmail(), subject1, message1);


            // 📧 Email to Employer
            String subject2 = "New Applicant for Your Job Posting";

            String message2 =
                    "Dear Employer,\n\n" +

                    "A new candidate has applied for your job posting.\n\n" +

                    "Job Details:\n" +
                    "Job Title: " + job.getTitle() + "\n" +
                    "Location: " + job.getLocation() + "\n\n" +

                    "Applicant Details:\n" +
                    "Name: " + user.getName() + "\n" +
                    "Email: " + user.getEmail() + "\n\n" +

                    "Please login to your Job Portal dashboard to review the candidate's profile and resume.\n\n" +

                    "Best Regards,\n" +
                    "Job Portal System";

            emailService.sendEmail(job.getEmployer().getEmail(), subject2, message2);

        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }

        return savedApp;
    }


    public List<Application> getApplicationsForEmployer(String email) {

        User employer = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return appRepo.findByJobEmployer(employer);
    }

    public List<Application> getApplicantsByJob(Long jobId) {

        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return appRepo.findByJob(job);
    }

    public Application updateStatus(Long id, String status) {

        Application app = appRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(ApplicationStatus.valueOf(status));

        try {

            String subject = "Application Status Update";

            String message =
                    "Dear " + app.getJobSeeker().getName() + ",\n\n" +

                    "Your job application status has been updated.\n\n" +

                    "Job Details:\n" +
                    "Job Title: " + app.getJob().getTitle() + "\n" +
                    "Company: " + app.getJob().getEmployer().getName() + "\n\n" +

                    "Current Status: " + status + "\n\n" +

                    "Please login to your Job Portal dashboard for more details.\n\n" +

                    "Best Regards,\n" +
                    "Job Portal Team";

            emailService.sendEmail(
                    app.getJobSeeker().getEmail(),
                    subject,
                    message
            );

        } catch (Exception e) {
            System.out.println("Email failed");
        }

        return appRepo.save(app);
    }

    public List<Application> getApplicationsForUser(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return appRepo.findByJobSeeker(user);
    }
}