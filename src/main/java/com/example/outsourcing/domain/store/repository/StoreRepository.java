package com.example.outsourcing.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.user.entity.User;

public interface StoreRepository extends JpaRepository<Store, Long> {

	long countByUser(User user);
}
