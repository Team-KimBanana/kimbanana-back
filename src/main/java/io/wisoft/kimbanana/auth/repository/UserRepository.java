package io.wisoft.kimbanana.auth.repository;

import io.wisoft.kimbanana.auth.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    void save(User user);
}
