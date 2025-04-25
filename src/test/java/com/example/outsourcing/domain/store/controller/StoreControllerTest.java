package com.example.outsourcing.domain.store.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.outsourcing.domain.auth.jwt.JwtProvider;
import com.example.outsourcing.domain.menu.dto.MenuResponse;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.store.dto.request.StoreRequestDto;
import com.example.outsourcing.domain.store.dto.response.StoreSingleResponseDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.Category;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.store.service.StoreService;
import com.example.outsourcing.domain.user.entity.Role;
import com.example.outsourcing.domain.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class StoreControllerTest {

	@InjectMocks
	private StoreController storeController;

	@Mock
	private StoreService storeService;

	@Mock
	private JwtProvider jwtProvider;

	private ObjectMapper objectMapper = new ObjectMapper();

	private MockMvc mockMvc;

	@BeforeEach
	public void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(storeController).build();

	}

	@Test
	@DisplayName("가게 생성 성공")
	void saveStoreTest() throws Exception {

		//given
		User user = User.builder()
			.email("user1@example.com")
			.password("Aa1234!@")
			.name("홍길동")
			.role(Role.OWNER)
			.build();

		Store store = new Store("기존이름", "09:00", "18:00", 10000, StoreStatus.CLOSED, Category.WESTERN, user);

		StoreRequestDto dto = new StoreRequestDto("칙스칙스", "01:00", "12:00", 15000, "WESTERN");

		//when, then
		mockMvc.perform(
				post("/api/stores")
					.content(objectMapper.writeValueAsString(dto))
					.contentType(MediaType.APPLICATION_JSON)
					.requestAttr("userId", 1L)
			)
			.andDo(print())
			.andExpect(status().isOk());

	}

	@Test
	@DisplayName("가게 단건 조회 성공")
	void findSingleStore() throws Exception {

		// given
		Long storeId = 1L;

		User user = User.builder()
			.email("user1@example.com")
			.password("Aa1234!@")
			.name("홍길동")
			.role(Role.OWNER)
			.build();

		Store store = new Store("칙스칙스", "01:00", "12:00", 15000, StoreStatus.OPEN, Category.WESTERN, user);
		Store store2 = new Store("타코카페", "07:00", "14:00", 15000, StoreStatus.OPEN, Category.DESSERT, user);

		Menu menu1 = new Menu(store, "돈까스정식", 4500, "바삭한 돈까스와 밥, 국 포함");
		Menu menu2 = new Menu(store, "돈까스정식", 4500, "바삭한 돈까스와 밥, 국 포함");
		Menu menu3 = new Menu(store, "돈까스정식", 4500, "바삭한 돈까스와 밥, 국 포함");

		List<MenuResponse> menuResponses = List.of(MenuResponse.from(menu1), MenuResponse.from(menu2),
			MenuResponse.from(menu3));

		StoreSingleResponseDto responseDto = new StoreSingleResponseDto(
			store, menuResponses);

		given(storeService.findSingleStore(eq(storeId)))
			.willReturn(responseDto);

		//when, then
		mockMvc.perform(
				get("/api/stores/{storeId}", storeId)
					.requestAttr("userId", 1L)
			)
			.andDo(print())
			.andExpect(status().isOk());

	}

	@Test
	void findAllStore() {
	}

	@Test
	void updateStore() {
	}

	@Test
	void deleteStore() {
	}
}