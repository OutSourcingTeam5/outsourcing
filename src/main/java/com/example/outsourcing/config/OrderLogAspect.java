package com.example.outsourcing.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.outsourcing.domain.order.dto.response.OrderResponseDto;
import com.example.outsourcing.domain.order.dto.response.OrderStatusChangeResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class OrderLogAspect {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@AfterReturning(
		pointcut = "execution(* com.example.outsourcing.domain.order.service.OrderService.createOrder(..))",
		returning = "responseDto"
	)
	public void logOrderCreate(JoinPoint joinPoint, OrderResponseDto responseDto) {
		String now = LocalDateTime.now().format(formatter);
		log.info("[ORDER CREATED] 시간: {}, 가게ID: {}, 주문ID: {}", now, responseDto.getStoreId(), responseDto.getId());
	}

	@AfterReturning(
		pointcut = "execution(* com.example.outsourcing.domain.order.service.OrderService.updateOrderStatus(..))",
		returning = "responseDto"
	)
	public void logOrderStatusChange(JoinPoint joinPoint, OrderStatusChangeResponseDto responseDto) {
		String now = LocalDateTime.now().format(formatter);
		log.info("[ORDER STATUS CHANGED] 시간: {}, 가게ID: {}, 주문ID: {}", now, responseDto.getStoreId(),
			responseDto.getOrderId());
	}

}
