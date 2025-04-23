package com.example.outsourcing.domain.store.controller;

import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.domain.store.dto.request.StoreRequestDto;
import com.example.outsourcing.domain.store.dto.response.StoreSaveResponseDto;
import com.example.outsourcing.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeservice;

    @PostMapping
    public CommonResponse<StoreSaveResponseDto> saveStore( @Valid @RequestBody StoreRequestDto dto) {

        Long id = 1L;

        return CommonResponse.created(storeservice.saveStore(id, dto));
    }


}
