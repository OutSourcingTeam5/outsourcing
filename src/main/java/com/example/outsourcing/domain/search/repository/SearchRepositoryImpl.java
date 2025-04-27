package com.example.outsourcing.domain.search.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.example.outsourcing.domain.menu.entity.QMenu;
import com.example.outsourcing.domain.store.entity.QStore;
import com.example.outsourcing.domain.store.entity.Store;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	QStore store = QStore.store;
	QMenu menu = QMenu.menu;

	@Override
	public Slice<Store> searchStores(String keyword, Pageable pageable) {

		// List<StoreResponseDto> dtoList = queryFactory
		// 	.selectDistinct()

		return null;
	}
}
