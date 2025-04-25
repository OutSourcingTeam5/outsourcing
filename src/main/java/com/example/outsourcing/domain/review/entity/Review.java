package com.example.outsourcing.domain.review.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.domain.order.entity.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private Integer rating;

	private Boolean isDeleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	public Review() {
	}

	public Review(String content, Integer rating, Boolean isDeleted, Order order) {
		this.content = content;
		this.rating = rating;
		this.isDeleted = isDeleted;
		this.order = order;
	}

}
