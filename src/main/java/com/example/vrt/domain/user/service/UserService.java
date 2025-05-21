package com.example.vrt.domain.user.service;

import com.example.vrt.domain.user.dto.UserDTO;
import com.example.vrt.domain.user.entity.User;
import com.example.vrt.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void register(UserDTO dto) {
        User user = User.builder()
                .userId(dto.getId())
                .name(dto.getName())
                .build();
        userRepository.save(user);
    }

    public boolean login(UserDTO dto) {
        return userRepository.existsById(dto.getId());
    }


}