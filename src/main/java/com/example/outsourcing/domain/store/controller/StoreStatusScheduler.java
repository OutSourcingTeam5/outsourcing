package com.example.outsourcing.domain.store.controller;

import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreStatusScheduler {

	private final StoreRepository storeRepository;

	@Transactional
	@Scheduled(cron = "0 * * * * *")
	public void updateStoreStatus() {

		LocalTime now = LocalTime.now();
		LocalTime minusRange = now.minusMinutes(5);
		LocalTime plusRange = now.plusMinutes(5);

		List<Store> stores = storeRepository.findStoreBetweenRange(minusRange, plusRange);

		for (Store store : stores) {

			if (store.getStoreStatus() == StoreStatus.CLOSED) {
				continue;
			}

			if ((now.isAfter(store.getOpenTime()) && now.isBefore(store.getCloseTime()))) {
				store.updateStoreStatus(StoreStatus.OPEN);
			} else {
				store.updateStoreStatus(StoreStatus.PREPARING);
			}
		}

	}
}


