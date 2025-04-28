package com.example.outsourcing.domain.menu;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.outsourcing.domain.menu.controller.MenuController;
import com.example.outsourcing.domain.menu.dto.MenuCreateRequest;
import com.example.outsourcing.domain.menu.dto.MenuResultResponse;
import com.example.outsourcing.domain.menu.dto.MenuUpdateRequest;
import com.example.outsourcing.domain.menu.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@ExtendWith(MockitoExtension.class)
class MenuControllerTest {

	@InjectMocks
	private MenuController menuController;

	@Mock
	private MenuService menuService;

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(menuController).build();
	}

	@Test
	@DisplayName("메뉴 생성 성공")
	void createMenu_success() throws Exception {

		MenuCreateRequest request = new MenuCreateRequest(1L, "불고기버거", 8000, "순쇠고기패티에 특제 불고기소스");

		given(menuService.create(any(MenuCreateRequest.class), anyLong()))
				.willReturn(new MenuResultResponse(1L, "메뉴가 등록되었습니다."));

		mockMvc.perform(post("/api/menus")
						.requestAttr("userId", 1L)
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("메뉴 수정 성공")
	void updateMenu_success() throws Exception {

		MenuUpdateRequest request = new MenuUpdateRequest("치즈 불고기버거", 8500, "불고기버거에 치즈의 풍미까지");

		given(menuService.update(anyLong(), any(MenuUpdateRequest.class), anyLong()))
				.willReturn(new MenuResultResponse(1L, "메뉴가 수정되었습니다."));

		mockMvc.perform(put("/api/menus/{menuId}", 1L)
						.requestAttr("userId", 1L)
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("메뉴 삭제 성공")
	void deleteMenu_success() throws Exception {

		given(menuService.delete(anyLong(), anyLong()))
				.willReturn(new MenuResultResponse(1L, "메뉴가 삭제되었습니다."));

		mockMvc.perform(delete("/api/menus/{menuId}", 1L)
						.requestAttr("userId", 1L))
				.andDo(print())
				.andExpect(status().isOk());
	}
}