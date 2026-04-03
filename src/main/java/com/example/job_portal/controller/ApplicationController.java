package com.example.job_portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.job_portal.entity.Application;
import com.example.job_portal.service.ApplicationService;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService service;

    @PostMapping("/apply/{jobId}")
    public Application apply(@PathVariable Long jobId,
                            Authentication auth) {

        if (auth == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = auth.getName();

        return service.applyJob(jobId, email);
    }

    @GetMapping("/employer")
    public List<Application> getEmployerApps(Authentication auth) {
        return service.getApplicationsForEmployer(auth.getName());
    }
    
    @PutMapping("/status/{id}")
    public Application updateStatus(@PathVariable Long id,
                                    @RequestParam String status) {
        return service.updateStatus(id, status);
    }
    
    @GetMapping("/job/{jobId}")
    public List<Application> getApplicants(@PathVariable Long jobId) {
        return service.getApplicantsByJob(jobId);
    }
    @GetMapping("/my-applications")
    public List<Application> getMyApplications(Authentication auth) {
        return service.getApplicationsForUser(auth.getName());
    }
  
    
}
