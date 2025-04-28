package com.example.outsourcing.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

import com.example.outsourcing.domain.menu.dto.MenuCreateRequest;
import com.example.outsourcing.domain.menu.dto.MenuUpdateRequest;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.menu.service.MenuService;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.Category;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import com.example.outsourcing.domain.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private StoreRepository storeRepository;

	@InjectMocks
	private MenuService menuService;

	@Test
	@DisplayName("메뉴 생성 성공")
	void create_success() {

		Long currentUserId = 1L;
		MenuCreateRequest request = new MenuCreateRequest(1L, "불고기버거", 8000, "순쇠고기패티에 특제불고기소스");

		User user = User.builder()
				.email("test@test.com")
				.name("홍길동")
				.build();
		ReflectionTestUtils.setField(user, "id", 1L);

		Store store = new Store("수제버거가게", "09:00", "22:00", 10000, StoreStatus.OPEN,
				Category.WESTERN,
				user);

		given(storeRepository.findById(request.getStoreId())).willReturn(Optional.of(store));
		given(menuRepository.save(any(Menu.class))).willAnswer(
				invocation -> invocation.getArgument(0));

		var result = menuService.create(request, currentUserId);

		assertThat(result).isNotNull();
		assertThat(result.getMessage()).isEqualTo("메뉴가 등록되었습니다.");
	}

	@Test
	@DisplayName("메뉴 수정 성공")
	void update_success() {
		// given
		Long currentUserId = 1L;
		Long menuId = 1L;
		MenuUpdateRequest request = new MenuUpdateRequest("치즈 불고기버거", 8500, "불고기버거에 치즈의 풍미까지");

		Store store = mock(Store.class);
		User user = mock(User.class);

		given(store.getUser()).willReturn(user);
		given(user.getId()).willReturn(1L);

		Menu menu = new Menu(store, "불고기버거", 8000, "순쇠고기패티에 특제불고기소스");

		given(menuRepository.findById(menuId)).willReturn(Optional.of(menu)); // 이거 추가!

		var response = menuService.update(menuId, request, currentUserId);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("메뉴가 수정되었습니다.");
	}


	@Test
	@DisplayName("메뉴 삭제 성공")
	void delete_success() {

		Long currentUserId = 1L;
		Long menuId = 1L;

		Store store = mock(Store.class);
		User user = mock(User.class);

		given(store.getUser()).willReturn(user);
		given(user.getId()).willReturn(1L);

		Menu menu = new Menu(store, "불고기버거", 8000, "순쇠고기패티에 특제불고기소스");

		given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

		var response = menuService.delete(menuId, currentUserId);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("메뉴가 삭제되었습니다.");
	}
}
