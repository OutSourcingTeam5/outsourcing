package com.example.outsourcing.domain.search.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.example.outsourcing.domain.store.entity.Store;

@Repository
public interface SearchRepositoryCustom {
	Slice<Store> searchStores(String keyword, Pageable pageable);
}
