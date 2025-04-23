package com.example.outsourcing.domain.test.repository;

import com.example.outsourcing.domain.test.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
