package com.example.outsourcing.domain.store.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.domain.store.dto.request.StoreRequestDto;
import com.example.outsourcing.domain.store.dto.response.StoreSaveResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreUpdateResponseDto;
import com.example.outsourcing.domain.store.service.StoreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeservice;

	@PostMapping
	public CommonResponse<StoreSaveResponseDto> saveStore(@RequestAttribute("userId") Long userId,
		@Valid @RequestBody StoreRequestDto dto) {

		return CommonResponse.created(storeservice.saveStore(userId, dto));
	}

	@PatchMapping("/{storeId}")
	public CommonResponse<StoreUpdateResponseDto> updateStore(@RequestAttribute("userId") Long userId,
		@PathVariable Long storeId, @Valid @RequestBody StoreRequestDto dto) {

		return CommonResponse.ok(storeservice.updateStore(userId, storeId, dto));
	}

	@DeleteMapping("{storeId}")
	public CommonResponse<Void> deleteStore(@RequestAttribute("userId") Long userId, @PathVariable Long storeId) {

		storeservice.delete(userId, storeId);

		return CommonResponse.ok();
	}

}
