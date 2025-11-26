package io.wisoft.kimbanana.auth.repository;

import io.wisoft.kimbanana.auth.entity.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(String userId);
    int save(User user);
    int deleteById(String id);
}
