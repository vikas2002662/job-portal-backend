package com.example.job_portal.dto;

import lombok.Getter;

@Getter
public class EmployerDashboardDTO {

    private Long totalJobs;
    private Long totalApplications;

    public EmployerDashboardDTO(Long totalJobs, Long totalApplications) {
        this.totalJobs = totalJobs;
        this.totalApplications = totalApplications;
    }

    // getters
}
