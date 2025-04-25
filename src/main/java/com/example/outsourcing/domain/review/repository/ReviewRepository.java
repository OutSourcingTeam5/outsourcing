package com.example.outsourcing.domain.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outsourcing.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	@Query("""
		    select r from Review r
		    join fetch r.order o
		    join fetch o.user u
		    join o.store s
		    where s.id = :storeId
		      and r.rating between :start and :end
		      and (r.isDeleted is null or r.isDeleted = false)
		    order by r.createdAt desc
		""")
	List<Review> findAllByStoreIdAndRatingRangeWithUser(
		@Param("storeId") Long storeId,
		@Param("start") Integer startRating,
		@Param("end") Integer endRating
	);

}
