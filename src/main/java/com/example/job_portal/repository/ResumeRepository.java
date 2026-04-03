package com.example.job_portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.job_portal.entity.Resume;
import com.example.job_portal.entity.User;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByUser(User user);


}
