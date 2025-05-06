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
        User user = new User(dto.getId(), dto.getName());
        userRepository.save(user);
    }
}