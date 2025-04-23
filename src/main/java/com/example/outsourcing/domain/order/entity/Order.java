package com.example.outsourcing.domain.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String orderStatus;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long menuId;

	@Column(nullable = false)
	private Long storeId;

	public Order() {

	}

	public Order(String orderStatus, Long userId, Long menuId, Long storeId) {
		this.orderStatus = orderStatus;
		this.userId = userId;
		this.menuId = menuId;
		this.storeId = storeId;
	}
}
