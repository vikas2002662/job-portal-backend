package com.example.job_portal.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.job_portal.dto.AdminDashboardDTO;
import com.example.job_portal.dto.EmployerDashboardDTO;
import com.example.job_portal.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService service;

    @GetMapping("/employer")
    public EmployerDashboardDTO employerDashboard(Authentication auth) {
        return service.getEmployerDashboard(auth.getName());
    }
    
    @GetMapping("/admin")
    public AdminDashboardDTO adminDashboard() {
        return service.getAdminDashboard();
    }
    
    @GetMapping("/analytics/jobs-location")
    public Map<String, Long> jobsByLocation() {
        return service.getJobsByLocation();
    }

    @GetMapping("/analytics/applications-job")
    public Map<String, Long> applicationsPerJob() {
        return service.getApplicationsPerJob();
    }


}
