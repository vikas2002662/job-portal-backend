package com.example.job_portal.dto;



public class AdminDashboardDTO {

    private Long totalUsers;
    private Long totalJobs;
    private Long totalApplications;

    public AdminDashboardDTO(Long u, Long j, Long a) {
        this.totalUsers = u;
        this.totalJobs = j;
        this.totalApplications = a;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public Long getTotalJobs() {
        return totalJobs;
    }

    public Long getTotalApplications() {
        return totalApplications;
    }
}

