package com.example.outsourcing.domain.test.service;

import com.example.outsourcing.domain.test.dto.request.TestReqeustDto;
import com.example.outsourcing.domain.test.dto.response.TestResponseDto;
import com.example.outsourcing.domain.test.entity.Test;
import com.example.outsourcing.domain.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;


    public TestResponseDto saveTest(Long id, TestReqeustDto testReqeustDto) {

        Test test = new Test(id,testReqeustDto.getName());

        testRepository.save(test);

        return new TestResponseDto(test.getId(), test.getName());
    }
}
