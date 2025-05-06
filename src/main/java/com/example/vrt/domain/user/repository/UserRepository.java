// UserRepository.java (인터페이스)
package com.example.vrt.domain.user.repository;

import com.example.vrt.domain.user.entity.User;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String id);
    boolean existsById(String id);
}