package com.example.job_portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.job_portal.entity.Application;
import com.example.job_portal.entity.Job;
import com.example.job_portal.entity.User;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByJobAndJobSeeker(Job job, User user);

    List<Application> findByJobEmployer(User employer);
    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.employer = :employer")
    Long countApplicationsByEmployer(User employer);
    
    @Query("SELECT COUNT(a) FROM Application a")
    Long countApplications();
    
    @Query("SELECT a.job.title, COUNT(a) FROM Application a GROUP BY a.job.title")
    List<Object[]> applicationsPerJob();
    
    List<Application> findByJob(Job job);
    
    List<Application> findByJobSeeker(User user);

}
