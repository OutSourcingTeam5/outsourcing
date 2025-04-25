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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

	private final MenuService menuService;

	/**
	 * 메뉴를 생성하는 API. 로그인된 사용자의 ID를 기반으로, 해당 사용자가 소유한 가게에만 메뉴를 생성
	 */
	@PostMapping
	public CommonResponse<MenuResultResponse> createMenu(
			@RequestBody @Valid MenuCreateRequest request,
			@RequestAttribute("userId") Long currentUserId
	) {
		return CommonResponse.created(menuService.create(request, currentUserId));
	}

	/**
	 * 가게의 메뉴 목록을 조회하는 API. 메뉴는 활성상태인 경우에만 조회
	 */
	@GetMapping("/stores/{storeId}/menus")
	public CommonResponse<List<MenuResponse>> getMenusByStore(
			@PathVariable Long storeId
	) {
		return CommonResponse.ok(menuService.getMenusByStore(storeId));
	}

	/**
	 * 메뉴 정보를 수정하는 API. 메뉴가 소속된 가게의 오너와 로그인 사용자가 일치할 때만 수정가능
	 */

	@PutMapping("/{menuId}")
	public CommonResponse<MenuResultResponse> updateMenu(
			@PathVariable Long menuId,
			@RequestBody @Valid MenuUpdateRequest request,
			@RequestAttribute("userId") Long currentUserId
	) {
		return CommonResponse.ok(menuService.update(menuId, request, currentUserId));
	}

	/**
	 * 메뉴를 삭제하는 API. 실제 삭제가 아닌 Soft Delete 방식으로 상태를 deleted로 변경. 메뉴가 소속된 가게의 오너와 로그인 사용자가 일치할
	 * 때만삭제가능
	 */
	@DeleteMapping("/{menuId}")
	public CommonResponse<MenuResultResponse> deleteMenu(
			@PathVariable Long menuId,
			@RequestAttribute("userId") Long currentUserId
	) {
		return CommonResponse.ok(menuService.delete(menuId, currentUserId));
	}
}
