package com.example.outsourcing.domain.store.dto.response;

import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.Category;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreSaveResponseDto {

    private final Long id;

    private final String name;

    private final LocalTime openTime;

    private final LocalTime closeTime;

    private final Integer minOrderPrice;

    private final StoreStatus storeStatus;

    private final Category category;

    public StoreSaveResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.minOrderPrice = store.getMinOrderPrice();
        this.storeStatus = store.getStoreStatus();
        this.category = store.getCategory();

    }
}
