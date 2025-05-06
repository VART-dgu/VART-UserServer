package com.example.vrt.domain.user.controller;

import com.example.vrt.domain.user.dto.UserDTO;
import com.example.vrt.domain.user.service.UserService;
import com.example.vrt.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody UserDTO dto) {
        userService.register(dto);
        ApiResponse<String> response = new ApiResponse<>(
                true,
                "COMMON200",
                "회원가입에 성공하였습니다.",
                ""
        );
        return ResponseEntity.ok(response);
    }
}