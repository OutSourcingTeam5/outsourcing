package com.example.outsourcing.domain.store.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.outsourcing.domain.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class StoreControllerTest {

	@InjectMocks
	StoreController storeController;

	@Mock
	StoreService storeService;

	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void beforeEach() {
		mockMvc = MockMvcBuilders.standaloneSetup(storeController).build();
	}

	@Test
	@DisplayName("가게 생성")
	void saveStoreTest() throws Exception {

	}

	@Test
	void findSingleStore() {
	}

	@Test
	void findAllStore() {
	}

	@Test
	void updateStore() {
	}

	@Test
	void deleteStore() {
	}
}