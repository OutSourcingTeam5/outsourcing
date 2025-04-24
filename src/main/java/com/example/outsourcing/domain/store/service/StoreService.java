package com.example.outsourcing.domain.store.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcing.domain.store.dto.request.StoreRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.StoreSaveResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreUpdateResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.Category;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.exception.StoreErrorCode;
import com.example.outsourcing.domain.store.exception.StoreException;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.Role;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final UserRepository userRepository;

	@Transactional
	public StoreSaveResponseDto saveStore(Long userId, StoreRequestDto dto) {

		User user = checkOwnerOrThrow(userId);

		long storeCount = storeRepository.countByUser(user);

		if (storeCount >= 3) {
			throw new StoreException(StoreErrorCode.STORE_LIMIT_REACHED);
		}

		Store store = new Store(
			dto.getName(),
			dto.getOpenTime(),
			dto.getCloseTime(),
			dto.getMinOrderPrice(),
			StoreStatus.OPEN,
			Category.valueOf(dto.getCategory()),
			user);

		storeRepository.save(store);

		return new StoreSaveResponseDto(store);
	}

	@Transactional
	public StoreUpdateResponseDto updateStore(Long userId, Long storeId, StoreUpdateRequestDto dto) {

		System.out.println("💡 updateStore 진입!");

		User user = checkOwnerOrThrow(userId);

		System.out.println("userId = " + userId);
		System.out.println("userRole = " + user.getRole());

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

		checkOwnerForStore(user, store);

		store.update(dto);

		return new StoreUpdateResponseDto(store);
	}

	@Transactional
	public void delete(Long userId, Long storeId) {

		User user = checkOwnerOrThrow(userId);

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

		checkOwnerForStore(user, store);

		store.updateStoreStatus(StoreStatus.CLOSED);
	}

	private User checkOwnerOrThrow(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new StoreException(StoreErrorCode.USER_NOT_FOUND));

		System.out.println(user.getRole());

		if (!Role.OWNER.equals(user.getRole())) {
			throw new StoreException(StoreErrorCode.STORE_FORBIDDEN_NORMAL);
		}

		return user;
	}

	private void checkOwnerForStore(User user, Store store) {

		if (!user.getId().equals(store.getUser().getId())) {
			throw new StoreException(StoreErrorCode.STORE_FORBIDDEN);
		}
	}

}
