package com.example.job_portal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import jakarta.persistence.Index;


@Entity
@Table(
	    name = "applications",
	    uniqueConstraints = {
	        // 🔥 Prevent duplicate apply at DB level
	        @UniqueConstraint(columnNames = {"job_id", "job_seeker_id"})
	    },
	    indexes = {
	        @Index(name = "idx_job", columnList = "job_id"),
	        @Index(name = "idx_user", columnList = "job_seeker_id")
	    }
	)
@Data
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_seeker_id")
    private User jobSeeker;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

   
    
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.APPLIED;
}