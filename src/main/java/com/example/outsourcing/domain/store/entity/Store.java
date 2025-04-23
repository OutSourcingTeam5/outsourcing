package com.example.outsourcing.domain.store.entity;


import com.example.outsourcing.domain.store.enums.Category;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import com.example.outsourcing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;


@Getter
@Entity
@Table(name="stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private Integer minOrderPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Store() {

    }

    public Store(String name, LocalTime openTime, LocalTime closeTime, Integer minOrderPrice, StoreStatus storeStatus, Category category, User user) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;
        this.storeStatus = storeStatus;
        this.category = category;
        this.user = user;
    }
}
