package com.example.outsourcing.domain.store.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcing.common.exception.CustomException;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.Category;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.exception.StoreErrorCode;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DummyDataInitializer {

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final MenuRepository menuRepository;

	@Transactional
	public void init() {

		User owner = userRepository.findById(12L).orElseThrow(() -> new CustomException(StoreErrorCode.USER_NOT_FOUND));

		// 더미 가게들
		List<Store> stores = List.of(
			new Store("치킨천국", "09:00", "21:00", 10000, StoreStatus.PREPARING, Category.KOREAN, owner),
			new Store("돈부리상점", "10:00", "22:00", 12000, StoreStatus.PREPARING, Category.JAPANESE, owner),
			new Store("마라탕마을", "13:42", "23:00", 9000, StoreStatus.PREPARING, Category.CHINESE, owner),
			new Store("타코야끼집", "08:00", "20:00", 11000, StoreStatus.PREPARING, Category.WESTERN, owner),
			new Store("브런치카페", "07:00", "14:00", 15000, StoreStatus.PREPARING, Category.DESSERT, owner),

			new Store("곱창명가", "15:00", "01:00", 13000, StoreStatus.PREPARING, Category.KOREAN, owner),
			new Store("우동나라", "11:00", "21:00", 8000, StoreStatus.PREPARING, Category.JAPANESE, owner),
			new Store("마라공방", "10:00", "22:00", 8500, StoreStatus.PREPARING, Category.CHINESE, owner),
			new Store("햄버거킹덤", "10:30", "23:30", 10000, StoreStatus.PREPARING, Category.WESTERN, owner),
			new Store("디저트천국", "08:00", "19:00", 9500, StoreStatus.PREPARING, Category.DESSERT, owner),

			new Store("순대의달인", "09:00", "21:00", 7000, StoreStatus.PREPARING, Category.KOREAN, owner),
			new Store("텐동야끼", "10:30", "20:30", 11000, StoreStatus.PREPARING, Category.JAPANESE, owner),
			new Store("짜장타임", "11:00", "22:00", 9000, StoreStatus.PREPARING, Category.CHINESE, owner),
			new Store("피자러버", "12:00", "23:59", 16000, StoreStatus.PREPARING, Category.WESTERN, owner),
			new Store("스윗베이커리", "07:00", "17:00", 8000, StoreStatus.PREPARING, Category.DESSERT, owner),

			new Store("감자탕장인", "06:00", "18:00", 14000, StoreStatus.PREPARING, Category.KOREAN, owner),
			new Store("규동하우스", "10:00", "22:00", 12000, StoreStatus.PREPARING, Category.JAPANESE, owner),
			new Store("양꼬치세상", "14:00", "23:30", 10000, StoreStatus.PREPARING, Category.CHINESE, owner),
			new Store("버거몽", "11:00", "23:00", 9500, StoreStatus.PREPARING, Category.WESTERN, owner),
			new Store("초코팬", "09:00", "18:00", 7000, StoreStatus.PREPARING, Category.DESSERT, owner)

		);

		storeRepository.saveAll(stores);
		storeRepository.flush();

		// 각 가게당 메뉴 3개씩
		List<Menu> menus = new ArrayList<>();

		for (Store store : stores) {
			menus.add(new Menu(store, store.getName() + " 메뉴1", 5000, "맛있는 메뉴1"));
			menus.add(new Menu(store, store.getName() + " 메뉴2", 7000, "맛있는 메뉴2"));
			menus.add(new Menu(store, store.getName() + " 메뉴3", 9000, "맛있는 메뉴3"));
		}

		menuRepository.saveAll(menus);

	}

}
