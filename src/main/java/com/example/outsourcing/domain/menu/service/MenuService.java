package com.example.outsourcing.domain.menu.service;

import com.example.outsourcing.common.exception.CustomException;
import com.example.outsourcing.domain.menu.dto.MenuCreateRequest;
import com.example.outsourcing.domain.menu.dto.MenuResponse;
import com.example.outsourcing.domain.menu.dto.MenuResultResponse;
import com.example.outsourcing.domain.menu.dto.MenuUpdateRequest;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.exception.MenuErrorCode;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;

	/**
	 * 메뉴를 생성하고 가게 존재 여부 확인 및 로그인 사용자와 해당 가게 오너의 일치 여부 확인 후 메뉴 엔티티 생성 및 저장
	 */
	public MenuResultResponse create(MenuCreateRequest request, Long currentUserId) {
		Store store = storeRepository.findById(request.getStoreId())
				.orElseThrow(() -> new CustomException(MenuErrorCode.STORE_NOT_FOUND));

		if (!store.getUser().getId().equals(currentUserId)) {
			throw new CustomException(MenuErrorCode.FORBIDDEN);
		}

		Menu menu = new Menu(store, request.getName(), request.getPrice(),
				request.getDescription());
		Menu saved = menuRepository.save(menu);

		return new MenuResultResponse(saved.getId(), "메뉴가 등록되었습니다.");
	}

	/**
	 * 가게의 메뉴 목록 조회
	 */

	public List<MenuResponse> getMenusByStore(Long storeId) {
		List<Menu> menus = menuRepository.findByStatusAndStoreId(Menu.Status.ACTIVE, storeId);
		return menus.stream().map(MenuResponse::from).toList();
	}

	/**
	 * 삭제된 메뉴는 수정이 불가능하며 사용자가 가게의 오너일 경우에만 수정가능.
	 */
	@Transactional
	public MenuResultResponse update(Long menuId, MenuUpdateRequest request, Long currentUserId) {
		Menu menu = menuRepository.findById(menuId)
				.orElseThrow(() -> new CustomException(MenuErrorCode.MENU_NOT_FOUND));

		if (menu.getStatus() == Menu.Status.DELETED) {
			throw new CustomException(MenuErrorCode.ALREADY_DELETED_MENU);
		}

		if (!menu.getStore().getUser().getId().equals(currentUserId)) {
			throw new CustomException(MenuErrorCode.FORBIDDEN);
		}

		menu.update(request.getName(), request.getPrice(), request.getDescription());
		return new MenuResultResponse(menu.getId(), "메뉴가 수정되었습니다.");
	}

	/**
	 * 삭제된 메뉴는 중복삭제가 불가능하며 사용자가 가게의 오너일 경우에만 삭제가능.(soft delete)
	 */
	@Transactional
	public MenuResultResponse delete(Long menuId, Long currentUserId) {
		Menu menu = menuRepository.findById(menuId)
				.orElseThrow(() -> new CustomException(MenuErrorCode.MENU_NOT_FOUND));

		if (menu.getStatus() == Menu.Status.DELETED) {
			throw new CustomException(MenuErrorCode.ALREADY_DELETED_MENU);
		}

		if (!menu.getStore().getUser().getId().equals(currentUserId)) {
			throw new CustomException(MenuErrorCode.FORBIDDEN);
		}

		menu.delete();
		return new MenuResultResponse(menu.getId(), "메뉴가 삭제되었습니다.");
	}
}
