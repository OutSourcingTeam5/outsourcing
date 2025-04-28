package com.example.outsourcing.domain.store.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

	/**
	 * 가게 생성
	 * - 사장만 가게를 생성할 수 있다.
	 * @author 조아현
	 * @Param userId
	 * @Param StoreRequestDto
	 * @Return StoreSaveResponseDto
	 * @since 2025 04 23
	 */
	@Transactional
	public StoreSaveResponseDto saveStore(Long userId, StoreRequestDto dto) {

		User user = checkOwnerOrThrow(userId);

		long storeCount = storeRepository.countByUserAndStoreStatus(user, StoreStatus.OPEN);

		if (storeCount == 3) {
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

	/**
	 * 가게 단건 조회
	 * - 가게의 메뉴 목록도 함께 조회
	 * @author 조아현
	 * @Param storeId
	 * @Return StoreSingleResponseDto
	 * @since 2025 04 25
	 */
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

	/**
	 * 가게 목록 조회
	 * - 페이지네이션과 가게명 검색이 가능하다
	 * @author 조아현
	 * @Param page
	 * @Param nameSearch
	 * @Return SliceResponseDto < StoreResponseDto>
	 * @since 2025 04 25
	 */
	@Transactional(readOnly = true)
	public SliceResponseDto<StoreResponseDto> findAllStore(int page, String nameSearch) {

		int adjustedPage = (page > 0) ? page - 1 : 0;
		Pageable pageable = PageRequest.of(adjustedPage, 10);

		Slice<Store> allstores = storeRepository.findAllstores(nameSearch, StoreStatus.OPEN, pageable);

		List<StoreResponseDto> storeDtoList = allstores.stream()
			.map(store -> new StoreResponseDto(store.getId(), store.getName(), store.getMinOrderPrice()))
			.toList();

		return new SliceResponseDto<>(
			storeDtoList,
			allstores.getNumber(),
			allstores.getSize(),
			allstores.isFirst(),
			allstores.isLast());
	}

	/**
	 * 가게 수정
	 * @author 조아현
	 * @Param userId
	 * @Param storeId
	 * @Param StoreUpdateRequestDto
	 * @Return CommonResponse<StoreUpdateResponseDto>
	 * @since 2025 04 25
	 */
	@Transactional
	public StoreUpdateResponseDto updateStore(Long userId, Long storeId, StoreUpdateRequestDto dto) {

		User user = checkOwnerOrThrow(userId);

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

		checkOwnerForStore(user, store);

		store.update(dto);

		return new StoreUpdateResponseDto(store);
	}

	/**
	 * 가게 삭제
	 * @author 조아현
	 * @Param userId
	 * @Param storeId
	 * @Return CommonResponse<StoreWithdrawResponseDto>
	 * @since 2025 04 25
	 */
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

	/**
	 * userId 검증 메서드
	 * - 비로그인 유저, 유저 역할(사장,일반) 검증
	 * @author 조아현
	 * @Param userId
	 * @Return User
	 * @since 2025 04 25
	 */
	private User checkOwnerOrThrow(Long userId) {

		if (userId == null) {
			throw new StoreException(StoreErrorCode.STORE_USER_NOT_LOGIN);
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new StoreException(StoreErrorCode.USER_NOT_FOUND));

		if (!Role.OWNER.equals(user.getRole())) {
			throw new StoreException(StoreErrorCode.STORE_FORBIDDEN_NORMAL);
		}

		return user;
	}

	/**
	 * user 검증 메서드
	 * - 가게 사장인지 확인
	 * @author 조아현
	 * @Param User
	 * @Param Store
	 * @since 2025 04 25
	 */
	private void checkOwnerForStore(User user, Store store) {

		if (!user.getId().equals(store.getUser().getId())) {
			throw new StoreException(StoreErrorCode.STORE_FORBIDDEN);
		}
	}

}
