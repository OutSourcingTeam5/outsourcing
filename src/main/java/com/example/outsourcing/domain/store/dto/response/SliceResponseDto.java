package com.example.outsourcing.domain.store.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class SliceResponseDto<T> {

	private final List<T> storeList;
	private final int currentPage;
	private final int size;
	private final boolean first;
	private final boolean last;

	public SliceResponseDto(List<T> storeList, int currentPage, int size, boolean first, boolean last) {
		this.storeList = storeList;
		this.currentPage = currentPage;
		this.size = size;
		this.first = first;
		this.last = last;
	}
}
