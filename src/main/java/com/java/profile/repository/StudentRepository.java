package com.java.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.profile.model.Student;

public interface StudentRepository extends JpaRepository<Student,String> {
    
}
