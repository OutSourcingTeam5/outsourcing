package com.example.outsourcing.domain.search.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.entity.Store;

public interface SearchRepository extends JpaRepository<Store, Long> {

	@Query(
		"select distinct new com.example.outsourcing.domain.store.dto.response.StoreResponseDto(s.id,s.name,s.minOrderPrice) "
			+ "from Store s "
			+ "left join s.menus m "
			+ "where lower(s.name) like lower(concat('%', :keyword ,'%'))  "
			+ "or lower(m.name) like lower(concat('%',:keyword,'%')) "
			+ "or lower(s.category) like lower(concat('%',:categoryKeyword,'%')) ")
	Slice<StoreResponseDto> searchStores(@Param("keyword") String keyword,
		@Param("categoryKeyword") String categoryKeyword, Pageable pageable);

}
