package com.example.job_portal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.example.job_portal.entity.Job;
import com.example.job_portal.repository.JobRepository;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepo;

    public Job createJob(Job job) {
        return jobRepo.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepo.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    // ✅ NEW METHOD (IMPORTANT)
    public List<Job> getJobsByEmployer(String email) {
        return jobRepo.findByEmployerEmail(email);
    }

    public Page<Job> getJobs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jobRepo.findAll(pageable);
    }

    public Page<Job> searchJobs(String location, Double salary, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (location != null && salary != null) {
            return jobRepo.findAll(
                    (root, query, cb) -> cb.and(
                            cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"),
                            cb.greaterThanOrEqualTo(root.get("salary"), salary)
                    ), pageable
            );
        }

        if (location != null) {
            return jobRepo.findByLocationContainingIgnoreCase(location, pageable);
        }

        if (salary != null) {
            return jobRepo.findBySalaryGreaterThanEqual(salary, pageable);
        }

        return jobRepo.findAll(pageable);
    }

    public Page<Job> getJobsSorted(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return jobRepo.findAll(pageable);
    }
    
    
    public Job updateJob(Long id, Job updatedJob, String email) {

        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // 🔐 Check ownership
        if (!job.getEmployer().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        job.setTitle(updatedJob.getTitle());
        job.setDescription(updatedJob.getDescription());
        job.setLocation(updatedJob.getLocation());
        job.setSalary(updatedJob.getSalary());

        return jobRepo.save(job);
    }
    
    public void deleteJob(Long id, String email) {

        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // 🔐 Check ownership
        if (!job.getEmployer().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        jobRepo.delete(job);
    }
}