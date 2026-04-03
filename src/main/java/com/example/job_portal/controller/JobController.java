package com.example.job_portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.job_portal.entity.Job;
import com.example.job_portal.entity.User;
import com.example.job_portal.repository.UserRepository;
import com.example.job_portal.service.JobService;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService service;

    @Autowired
    private UserRepository userRepo;

    // ✅ CREATE JOB
    @PostMapping
    public Job createJob(@RequestBody Job job, Authentication auth) {

        String email = auth.getName();

        User employer = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        job.setEmployer(employer);

        return service.createJob(job);
    }

    // ✅ GET ALL JOBS
    @GetMapping
    public List<Job> getAllJobs() {
        return service.getAllJobs();
    }

    // ✅ GET JOB BY ID
    @GetMapping("/{id}")
    public Job getJob(@PathVariable Long id) {
        return service.getJobById(id);
    }

    // ✅ EMPLOYER JOBS (🔥 IMPORTANT)
    @GetMapping("/my-jobs")
    public List<Job> getMyJobs(Authentication auth) {
        return service.getJobsByEmployer(auth.getName());
    }

    // ✅ SEARCH
    @GetMapping("/search")
    public Page<Job> searchJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double salary,
            @RequestParam int page,
            @RequestParam int size) {

        return service.searchJobs(location, salary, page, size);
    }

    // ✅ PAGINATION
    @GetMapping("/page")
    public Page<Job> getJobs(
            @RequestParam int page,
            @RequestParam int size) {

        return service.getJobs(page, size);
    }

    // ✅ SORTING
    @GetMapping("/sorted")
    public Page<Job> getSortedJobs(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy) {

        return service.getJobsSorted(page, size, sortBy);
    }
    
    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id,
                         @RequestBody Job job,
                         Authentication auth) {

        return service.updateJob(id, job, auth.getName());
    }
    
    @DeleteMapping("/{id}")
    public String deleteJob(@PathVariable Long id,
                            Authentication auth) {

        service.deleteJob(id, auth.getName());
        return "Job deleted successfully";
    }
}