package com.example.outsourcing.domain.test.controller;


import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.domain.test.dto.request.TestReqeustDto;
import com.example.outsourcing.domain.test.dto.response.TestResponseDto;
import com.example.outsourcing.domain.test.exception.TestErrorCode;
import com.example.outsourcing.domain.test.exception.TestException;
import com.example.outsourcing.domain.test.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping("/test")
    public CommonResponse<TestResponseDto> saveTest(Long id, @Valid @RequestBody TestReqeustDto testReqeustDto) {

        return CommonResponse.created(testService.saveTest(id, testReqeustDto));
    }

    @GetMapping("/exception")
    public CommonResponse<Void> throwTestException() {
        throw new TestException(TestErrorCode.FAILED);
    }

}
