package com.example.outsourcing.domain.order.entity;

import com.example.outsourcing.common.entity.BaseEntity;

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
public class Order extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private OrderStatus orderStatus;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long menuId;

	@Column(nullable = false)
	private Long storeId;

	public Order() {

	}

	public Order(OrderStatus orderStatus, Long userId, Long storeId, Long menuId) {
		this.orderStatus = orderStatus;
		this.userId = userId;
		this.storeId = storeId;
		this.menuId = menuId;
	}
}
