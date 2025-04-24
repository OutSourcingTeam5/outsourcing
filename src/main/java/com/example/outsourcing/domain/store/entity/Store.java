package com.example.outsourcing.domain.store.entity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.enums.Category;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "stores")
@DynamicUpdate
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private LocalTime openTime;

	@Column(nullable = false)
	private LocalTime closeTime;

	@Column(nullable = false)
	private Integer minOrderPrice;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StoreStatus storeStatus;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "store")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Menu> menus = new ArrayList<>();

	public Store() {

	}

	public Store(String name, String openTime, String closeTime, Integer minOrderPrice, StoreStatus storeStatus,
		Category category, User user) {
		this.name = name;
		this.openTime = LocalTime.parse(openTime, DateTimeFormatter.ofPattern("HH:mm"));
		this.closeTime = LocalTime.parse(closeTime, DateTimeFormatter.ofPattern("HH:mm"));
		this.minOrderPrice = minOrderPrice;
		this.storeStatus = storeStatus;
		this.category = category;
		this.user = user;
	}

	public void update(StoreUpdateRequestDto dto) {
		if (dto.getName() != null) {
			this.name = dto.getName();
		}
		if (dto.getOpenTime() != null) {
			this.openTime = LocalTime.parse(dto.getOpenTime(), DateTimeFormatter.ofPattern("HH:mm"));
		}
		if (dto.getCloseTime() != null) {
			this.closeTime = LocalTime.parse(dto.getCloseTime(), DateTimeFormatter.ofPattern("HH:mm"));
		}
		if (dto.getMinOrderPrice() != null) {
			this.minOrderPrice = dto.getMinOrderPrice();

		}
		if (dto.getCategory() != null) {
			this.category = Category.valueOf(dto.getCategory());
		}
	}

	public void delete(StoreStatus storeStatus) {
		this.storeStatus = storeStatus;
	}

}
