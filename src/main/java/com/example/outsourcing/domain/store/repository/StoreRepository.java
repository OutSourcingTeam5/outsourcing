package com.example.outsourcing.domain.store.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.user.entity.User;

public interface StoreRepository extends JpaRepository<Store, Long> {

	long countByUser(User user);

	@Query("select distinct s "
		+ "from Store s "
		+ "left join fetch s.menus m "
		+ "where s.id = :storeId")
	Optional<Store> findStoreByIdWithMenus(@Param("storeId") Long storeId);

	long countByUserAndStoreStatus(User user, StoreStatus storeStatus);

	@Query("select s from Store s "
		+ "where (:nameSearch is null or s.name like "
		+ "CONCAT('%', :nameSearch, '%'))")
	Slice<Store> findAllstores(@Param("nameSearch") String nameSearch, Pageable pageable);
}
