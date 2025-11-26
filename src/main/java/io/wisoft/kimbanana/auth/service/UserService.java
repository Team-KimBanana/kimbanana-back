package io.wisoft.kimbanana.auth.service;

import io.wisoft.kimbanana.auth.dto.response.UserInfoResponse;
import io.wisoft.kimbanana.auth.entity.User;
import io.wisoft.kimbanana.auth.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    /**
     * 이메일로 사용자 조회
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

    }

    /**
     * ID 로 사용자 조회
     */
    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다"));
    }


    /**
     * 사용자 정보 조회 (DTO 변환)
     */
    public UserInfoResponse getUserInfo(String userId) {
        User user = findById(userId);
        return new UserInfoResponse(user.getId(), user.getEmail(), user.getName());
    }


    /**
     * 회원가입 - 사용자 생성
     */
    public Integer createUser(String email, String name, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다");
        }

        String id = "u_" + UUID.randomUUID();

        User user = new User(
                id,
                email,
                name,
                encoder.encode(password),
                "kimbanana",
                "jwt_kimbanana"
        );

        return userRepository.save(user);
    }

    /**
     * 비밀번호 검증
     */
    public boolean isPasswordMatch(User user, String rawPassword) {
        return encoder.matches(rawPassword, user.getPassword());
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public void deleteUser(final String userId) {
        findById(userId);

        // 2. 삭제
        int result = userRepository.deleteById(userId);

        if (result == 0) {
            throw new IllegalStateException("사용자 삭제에 실패했습니다");
        }
    }
}
