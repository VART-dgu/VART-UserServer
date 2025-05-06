package com.example.vrt.domain.user.repository;

import com.example.vrt.domain.user.entity.User;

public interface UserRepository {
    void save(User user);
    User findById(String id);
}