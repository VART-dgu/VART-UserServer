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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> loginUser(@RequestBody UserDTO dto) {
        boolean exists = userService.login(dto);
        if (exists) {
            return ResponseEntity.ok(new ApiResponse<>(true, "COMMON200", "로그인에 성공하였습니다.", ""));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "USER404", "존재하지 않는 사용자입니다.", ""));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logoutUser(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "COMMON200", "로그아웃에 성공하였습니다.", ""));
    }
}