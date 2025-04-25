package com.example.outsourcing.domain.store.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.outsourcing.domain.menu.dto.MenuResponse;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
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

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private MenuRepository menuRepository;

	@InjectMocks
	private StoreService storeService;

	@Test
	@DisplayName("가게 생성 테스트")
	public void saveStore_success() {

		//given
		Long userId = 1L;
		StoreRequestDto request = new StoreRequestDto("칙스칙스", "01:00", "12:00", 15000, "WESTERN");
		User user = User.builder()
			.email("user1@example.com")
			.password("Aa1234!@")
			.name("홍길동")
			.role(Role.OWNER)
			.build();

		Store store = new Store(
			request.getName(),
			request.getOpenTime(),
			request.getCloseTime(),
			request.getMinOrderPrice(),
			StoreStatus.OPEN,
			Category.WESTERN,
			user);

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(storeRepository.save(any())).willReturn(store);

		//when
		StoreSaveResponseDto result = storeService.saveStore(userId, request);

		//then
		assertNotNull(result);

	}

	@Test
	@DisplayName("가게 단건 조회 테스트")
	public void findSingleStore_success() {

		//given
		Long storeId = 1L;

		User user = User.builder()
			.email("user1@example.com")
			.password("Aa1234!@")
			.name("홍길동")
			.role(Role.OWNER)
			.build();

		Store store = new Store("칙스칙스", "01:00", "12:00", 15000, StoreStatus.OPEN, Category.WESTERN, user);

		Menu menu1 = new Menu(store, "돈까스정식", 4500, "바삭한 돈까스와 밥, 국 포함");
		Menu menu2 = new Menu(store, "돈까스정식", 4500, "바삭한 돈까스와 밥, 국 포함");
		Menu menu3 = new Menu(store, "돈까스정식", 4500, "바삭한 돈까스와 밥, 국 포함");

		List<Menu> menus = List.of(menu1, menu2, menu3);

		List<MenuResponse> menuList = menus.stream().map(MenuResponse::from).collect(Collectors.toList());

		StoreSingleResponseDto storeSingleResponseDto = new StoreSingleResponseDto(store, menuList);

		given(storeRepository.findStoreByIdWithMenus(anyLong())).willReturn(Optional.of(store));

		//when
		StoreSingleResponseDto result = storeService.findSingleStore(storeId);

		//then
		assertNotNull(result);

	}

	@Test
	@DisplayName("가게 수정 테스트")
	public void updateStore_success() {

		Long userId = 1L;
		Long storeId = 1L;

		User user = User.builder()
			.email("user1@example.com")
			.password("Aa1234!@")
			.name("홍길동")
			.role(Role.OWNER)
			.build();

		ReflectionTestUtils.setField(user, "id", 1L);

		Store store = new Store("기존이름", "09:00", "18:00", 10000, StoreStatus.OPEN, Category.WESTERN, user);

		StoreUpdateRequestDto dto = new StoreUpdateRequestDto("수정이름", "10:00", "20:00", 12000, "WESTERN");

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

		StoreUpdateResponseDto response = storeService.updateStore(1L, 1L, dto);

		// then
		assertThat(response.getName()).isEqualTo("수정이름");

	}

	@Test
	@DisplayName("가게 삭제 테스트")
	public void delete() {
		//given
		Long userId = 1L;
		Long storeId = 1L;

		User user = User.builder()
			.email("user1@example.com")
			.password("Aa1234!@")
			.name("홍길동")
			.role(Role.OWNER)
			.build();

		ReflectionTestUtils.setField(user, "id", userId);

		Store store = new Store("기존이름", "09:00", "18:00", 10000, StoreStatus.OPEN, Category.WESTERN, user);

		ReflectionTestUtils.setField(store, "id", storeId);

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

		//when
		StoreWithdrawResponseDto result = storeService.delete(userId, storeId);

		assertThat(result.getMessage().equals("가게 폐업 처리 되었습니다."));
	}

	@Test
	@DisplayName("가게 전체 조회, 검색 테스트")
	public void findAllStore() {
		Long userId = 1L;
		Long storeId = 1L;

		User user = User.builder()
			.email("user1@example.com")
			.password("Aa1234!@")
			.name("홍길동")
			.role(Role.OWNER)
			.build();

		Store store4 = new Store("타코카페", "07:00", "14:00", 15000, StoreStatus.OPEN, Category.DESSERT, user);
		Store store5 = new Store("타코야끼야", "12:00", "21:00", 9000, StoreStatus.CLOSED, Category.WESTERN, user);

		Pageable pageable = PageRequest.of(0, 10);

		given(storeRepository.findAllstores(anyString(), eq(StoreStatus.OPEN), any(Pageable.class)))
			.willReturn(new SliceImpl<>(List.of(store4, store5)));

		// when
		SliceResponseDto<StoreResponseDto> allstores = storeService.findAllStore(1, "타코");

		// then
		assertEquals(2, allstores.getStoreList().size());

	}

	@Test
	@DisplayName("")
	public void ifOwnerHasMoreThan3StoresThanThrow() {

		//given
		Long userId = 1L;
		Long storeId = 1L;

		User user = User.builder()
			.email("user1@example.com")
			.password("Aa1234!@")
			.name("홍길동")
			.role(Role.OWNER)
			.build();

		StoreRequestDto dto = new StoreRequestDto("칙스칙스", "01:00", "12:00", 15000, "WESTERN");

		given(userRepository.findById(eq(1L))).willReturn(Optional.of(user));
		given(storeRepository.countByUserAndStoreStatus(eq(user), eq(StoreStatus.OPEN))).willReturn(3L);

		//when
		StoreException exception = assertThrows(StoreException.class, () -> {
			storeService.saveStore(1L, dto);
		});

		//then
		assertEquals(StoreErrorCode.STORE_LIMIT_REACHED.getMessage(), exception.getMessage());

	}

	@Test
	public void ifStoreStatusClosedThanThrow() {

		//given
		User user = User.builder()
			.email("user1@example.com")
			.password("Aa1234!@")
			.name("홍길동")
			.role(Role.OWNER)
			.build();

		Store store = new Store("기존이름", "09:00", "18:00", 10000, StoreStatus.CLOSED, Category.WESTERN, user);

		given(storeRepository.findStoreByIdWithMenus(anyLong())).willReturn(Optional.of(store));

		//when
		StoreException exception = assertThrows(StoreException.class, () -> {
			storeService.findSingleStore(anyLong());
		});

		//then
		assertEquals(StoreErrorCode.STORE_NOT_FOUND.getMessage(), exception.getMessage());

	}

}