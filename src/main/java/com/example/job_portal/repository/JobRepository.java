package com.example.job_portal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.job_portal.entity.Job;
import com.example.job_portal.entity.User;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    List<Job> findByLocation(String location);

    Page<Job> findAll(Pageable pageable);

    Page<Job> findByLocationContainingIgnoreCase(String location, Pageable pageable);

    Page<Job> findBySalaryGreaterThanEqual(Double salary, Pageable pageable);

    // ✅ ADD THIS (VERY IMPORTANT)
    List<Job> findByEmployerEmail(String email);

    // ✅ Analytics
    @Query("SELECT COUNT(j) FROM Job j WHERE j.employer = :employer")
    Long countJobsByEmployer(User employer);

    @Query("SELECT COUNT(j) FROM Job j")
    Long countJobs();

    @Query("SELECT j.location, COUNT(j) FROM Job j GROUP BY j.location")
    List<Object[]> jobsByLocation();
}