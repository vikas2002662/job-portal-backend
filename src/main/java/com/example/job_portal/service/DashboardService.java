package com.example.job_portal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.job_portal.dto.AdminDashboardDTO;
import com.example.job_portal.dto.EmployerDashboardDTO;
import com.example.job_portal.entity.User;
import com.example.job_portal.repository.ApplicationRepository;
import com.example.job_portal.repository.JobRepository;
import com.example.job_portal.repository.UserRepository;

@Service
public class DashboardService {

    @Autowired
    private JobRepository jobRepo;

    @Autowired
    private ApplicationRepository appRepo;

    @Autowired
    private UserRepository userRepo;

    public EmployerDashboardDTO getEmployerDashboard(String email) {

        User employer = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long jobs = jobRepo.countJobsByEmployer(employer);
        Long applications = appRepo.countApplicationsByEmployer(employer);

        return new EmployerDashboardDTO(jobs, applications);
    }
    
    public AdminDashboardDTO getAdminDashboard() {

        Long users = userRepo.countUsers();
        Long jobs = jobRepo.countJobs();
        Long apps = appRepo.countApplications();

        return new AdminDashboardDTO(users, jobs, apps);
    }
    
    public Map<String, Long> getJobsByLocation() {

        List<Object[]> result = jobRepo.jobsByLocation();
        Map<String, Long> map = new HashMap<>();

        for (Object[] obj : result) {
            map.put((String) obj[0], (Long) obj[1]);
        }

        return map;
    }

    public Map<String, Long> getApplicationsPerJob() {

        List<Object[]> result = appRepo.applicationsPerJob();
        Map<String, Long> map = new HashMap<>();

        for (Object[] obj : result) {
            map.put((String) obj[0], (Long) obj[1]);
        }

        return map;
    }

}