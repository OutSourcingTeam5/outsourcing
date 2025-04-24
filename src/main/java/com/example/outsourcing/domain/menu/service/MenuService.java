package com.example.outsourcing.domain.menu.service;

import com.example.outsourcing.common.exception.CustomException;
import com.example.outsourcing.common.exception.ErrorCode;
import com.example.outsourcing.domain.menu.dto.MenuCreateRequest;
import com.example.outsourcing.domain.menu.dto.MenuResponse;
import com.example.outsourcing.domain.menu.dto.MenuResultResponse;
import com.example.outsourcing.domain.menu.dto.MenuUpdateRequest;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.repository.MenuRepository;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;


	public MenuResultResponse create(MenuCreateRequest request, Long currentUserId) {
		Store store = storeRepository.findById(request.getStoreId())
				.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		if (!store.getUser().getId().equals(currentUserId)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}

		Menu menu = new Menu(store, request.getName(), request.getPrice(),
				request.getDescription());
		Menu saved = menuRepository.save(menu);

		return new MenuResultResponse(saved.getId(), "메뉴가 등록되었습니다.");
	}

	public List<MenuResponse> getMenusByStore(Long storeId) {
		List<Menu> menus = menuRepository.findByStatusAndStoreId(Menu.Status.ACTIVE, storeId);
		return menus.stream().map(MenuResponse::from).toList();
	}

	public MenuResultResponse update(Long menuId, MenuUpdateRequest request, Long currentUserId) {
		Menu menu = menuRepository.findById(menuId)
				.orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

		if (menu.getStatus() == Menu.Status.DELETED) {
			throw new CustomException(ErrorCode.ALREADY_DELETED_MENU);
		}

		if (!menu.getStore().getUser().getId().equals(currentUserId)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}

		menu.update(request.getName(), request.getPrice(), request.getDescription());
		return new MenuResultResponse(menu.getId(), "메뉴가 수정되었습니다.");
	}

	public MenuResultResponse delete(Long menuId, Long currentUserId) {
		Menu menu = menuRepository.findById(menuId)
				.orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

		if (menu.getStatus() == Menu.Status.DELETED) {
			throw new CustomException(ErrorCode.ALREADY_DELETED_MENU);
		}

		if (!menu.getStore().getUser().getId().equals(currentUserId)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}

		menu.delete();
		return new MenuResultResponse(menu.getId(), "메뉴가 삭제되었습니다.");
	}
}
