package com.example.outsourcing.domain.store.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcing.domain.menu.dto.MenuResponse;
import com.example.outsourcing.domain.store.dto.request.StoreRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.SliceResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreSaveResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreSingleResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreUpdateResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreWithdrawResponseDto;
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

		long storeCount = storeRepository.countByUserAndStoreStatus(user, StoreStatus.OPEN);

		if (storeCount == 3) {
			throw new StoreException(StoreErrorCode.STORE_LIMIT_REACHED);
		}

		//결과

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

	@Transactional(readOnly = true)
	public StoreSingleResponseDto findSingleStore(Long storeId) {

		Store store = storeRepository.findStoreByIdWithMenus(storeId)
			.orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

		if (StoreStatus.CLOSED.equals(store.getStoreStatus())) {
			throw new StoreException(StoreErrorCode.STORE_NOT_FOUND);
		}

		List<MenuResponse> menuResponseList = store.getMenus()
			.stream()
			.map(menu -> new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getDescription()))
			.toList();

		return new StoreSingleResponseDto(store, menuResponseList);
	}

	@Transactional
	public StoreUpdateResponseDto updateStore(Long userId, Long storeId, StoreUpdateRequestDto dto) {

		User user = checkOwnerOrThrow(userId);

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

		checkOwnerForStore(user, store);

		store.update(dto);

		return new StoreUpdateResponseDto(store);
	}

	@Transactional
	public StoreWithdrawResponseDto delete(Long userId, Long storeId) {

		User user = checkOwnerOrThrow(userId);

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

		checkOwnerForStore(user, store);

		store.delete(StoreStatus.CLOSED);

		return new StoreWithdrawResponseDto(store,
			"가게 폐업 처리 되었습니다.");
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

	public SliceResponseDto<StoreResponseDto> findAllStore(int page, String nameSearch) {

		int adjustedPage = (page > 0) ? page - 1 : 0;
		Pageable pageable = PageRequest.of(adjustedPage, 10);

		// Slice<StoreResponseDto> allStore = storeRepository.findAllStore(pageable, nameSearch);

		storeRepository.findAllstores(nameSearch, pageable);

		return null;
	}
}
