package com.example.outsourcing.domain.menu.controller;

import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.domain.menu.dto.MenuCreateRequest;
import com.example.outsourcing.domain.menu.dto.MenuResponse;
import com.example.outsourcing.domain.menu.dto.MenuResultResponse;
import com.example.outsourcing.domain.menu.dto.MenuUpdateRequest;
import com.example.outsourcing.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

	private final MenuService menuService;

	// 메뉴 생성 API
	@PostMapping
	public ResponseEntity<CommonResponse<MenuResultResponse>> createMenu(
			@RequestBody @Valid MenuCreateRequest request
	) {
		Long currentUserId = 1L; // 임시 하드코딩 (인증 연동 시 수정 예정)
		MenuResultResponse response = menuService.create(request, currentUserId);
		return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.created(response));
	}

	// 가게 메뉴 조회 API
	@GetMapping("/stores/{storeId}/menus")
	public ResponseEntity<CommonResponse<List<MenuResponse>>> getMenusByStore(
			@PathVariable Long storeId
	) {
		List<MenuResponse> menus = menuService.getMenusByStore(storeId);
		return ResponseEntity.ok(CommonResponse.ok(menus));
	}

	// 메뉴 수정 API
	@PutMapping("/{menuId}")
	public ResponseEntity<CommonResponse<MenuResultResponse>> updateMenu(
			@PathVariable Long menuId,
			@RequestBody @Valid MenuUpdateRequest request
	) {
		Long currentUserId = 1L;
		MenuResultResponse response = menuService.update(menuId, request, currentUserId);
		return ResponseEntity.ok(CommonResponse.ok(response));
	}

	// 메뉴 삭제 API
	@DeleteMapping("/{menuId}")
	public ResponseEntity<CommonResponse<MenuResultResponse>> deleteMenu(
			@PathVariable Long menuId
	) {
		Long currentUserId = 1L;
		MenuResultResponse response = menuService.delete(menuId, currentUserId);
		return ResponseEntity.ok(CommonResponse.ok(response));
	}
}
