package com.example.outsourcing.domain.menu.entity;

import com.example.outsourcing.domain.store.entity.Store;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	private String name;

	private int price;

	private String description;

	@Enumerated(EnumType.STRING)
	private Status status = Status.ACTIVE;

	public enum Status {
		ACTIVE, DELETED
	}

	public Menu(Store store, String name, int price, String description) {
		this.store = store;
		this.name = name;
		this.price = price;
		this.description = description;
		this.status = Status.ACTIVE;
	}

	public void update(String name, int price, String description) {
		this.name = name;
		this.price = price;
		this.description = description;
	}

	public void delete() {
		this.status = Status.DELETED;
	}

}
