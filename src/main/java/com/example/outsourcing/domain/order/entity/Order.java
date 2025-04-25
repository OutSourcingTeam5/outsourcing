package com.example.outsourcing.domain.order.entity;

import com.example.outsourcing.common.entity.BaseEntity;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.user.entity.User;

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
import lombok.Setter;

@Getter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	@Column(nullable = false)
	private OrderStatus orderStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;

	public Order() {

	}

	public Order(OrderStatus orderStatus, User user, Store store, Menu menu) {
		this.orderStatus = orderStatus;
		this.user = user;
		this.store = store;
		this.menu = menu;
	}

	public Order(Long id, OrderStatus orderStatus, User user, Store store, Menu menu) {
		this.id = id;
		this.orderStatus = orderStatus;
		this.user = user;
		this.store = store;
		this.menu = menu;
	}

}
