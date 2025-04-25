package com.example.outsourcing.domain.store.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.domain.review.service.ReviewService;
import com.example.outsourcing.domain.store.dto.request.StoreRequestDto;
import com.example.outsourcing.domain.store.dto.request.StoreUpdateRequestDto;
import com.example.outsourcing.domain.store.dto.response.SliceResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreReviewsResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreSaveResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreSingleResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreUpdateResponseDto;
import com.example.outsourcing.domain.store.dto.response.StoreWithdrawResponseDto;
import com.example.outsourcing.domain.store.service.StoreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;
	private final ReviewService reviewService;

	@PostMapping
	public CommonResponse<StoreSaveResponseDto> saveStore(@RequestAttribute("userId") Long userId,
		@Valid @RequestBody StoreRequestDto dto) {

		return CommonResponse.created(storeService.saveStore(userId, dto));
	}

	@GetMapping("/{storeId}")
	public CommonResponse<StoreSingleResponseDto> findSingleStore(
		@RequestAttribute("userId") Long userId,
		@PathVariable("storeId") Long storeId) {

		return CommonResponse.ok(storeService.findSingleStore(storeId));
	}

	@GetMapping
	public CommonResponse<SliceResponseDto<StoreResponseDto>> findAllStore(@RequestAttribute("userId") Long userId,
		@RequestParam(value = "page", defaultValue = "1") int page,
		@RequestParam(required = false) String nameSearch) {

		SliceResponseDto<StoreResponseDto> sliceResponseDto = storeService.findAllStore(page, nameSearch);

		return CommonResponse.ok(sliceResponseDto);
	}

	@PatchMapping("/{storeId}")
	public CommonResponse<StoreUpdateResponseDto> updateStore(@RequestAttribute("userId") Long userId,
		@PathVariable("storeId") Long storeId, @Valid @RequestBody StoreUpdateRequestDto dto) {

		return CommonResponse.ok(storeService.updateStore(userId, storeId, dto));
	}

	@DeleteMapping("{storeId}")
	public CommonResponse<StoreWithdrawResponseDto> deleteStore(@RequestAttribute("userId") Long userId,
		@PathVariable("storeId") Long storeId) {

		return CommonResponse.ok(storeService.delete(userId, storeId));
	}

	@GetMapping("/{storeId}/reviews")
	public CommonResponse<StoreReviewsResponseDto> findReviews(@RequestAttribute("userId") Long userId,
		@PathVariable Long storeId,
		@RequestParam(value = "startRating", required = true) Integer startRating,
		@RequestParam(value = "endRating", required = true) Integer endRating) {

		return CommonResponse.ok(reviewService.findReviews(userId, storeId, startRating, endRating));
	}

}
