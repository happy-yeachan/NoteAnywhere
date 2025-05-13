package com.dannyPlaygroud.noteAnywhere.domain.user;

import com.dannyPlaygroud.noteAnywhere.domain.user.dto.UserCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;
import java.util.List;

/**
 * UserService는 사용자와 관련된 비즈니스 로직을 처리하는 계층입니다.
 * - @Service: 해당 클래스를 서비스 빈으로 등록
 * - @RequiredArgsConstructor: final 필드 기반 생성자 자동 주입 (Lombok)
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 전체 사용자 목록을 조회합니다.
     * deletedAt이 null인 사용자만 필터링하여 반환합니다.
     * soft delete된 사용자는 포함되지 않습니다.
     *
     * @return 삭제되지 않은 사용자 목록
     */
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getDeletedAt() == null)
                .toList();
    }

    /**
     * 특정 사용자 ID로 사용자 정보를 조회합니다.
     * soft delete가 적용되지 않은 사용자만 조회 대상입니다.
     *
     * @param id 사용자 ID
     * @return 해당 사용자 객체
     * @throws IllegalArgumentException 사용자가 존재하지 않을 경우 예외 발생
     */
    public User getUserById(Long id) {
        return userRepository.findByUserIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."));
    }

    /**
     * 새로운 사용자를 생성합니다.
     * Builder 패턴을 활용해 User 엔티티 객체를 생성하고 저장합니다.
     *
     * @param request 사용자 생성 요청 DTO
     * @return 생성된 사용자 객체
     */
    @Transactional
    public User createUser(UserCreateRequest request) {
        return userRepository.save(
                User.builder()
                        .userName(request.userName())
                        .userProfile(request.userProfile())
                        .build()
        );
    }

    /**
     * 사용자를 soft delete 처리합니다.
     * 실제로 데이터를 삭제하지 않고, deletedAt 필드에 현재 시각을 기록합니다.
     * → 추후 getAllUsers() 등에서 필터링되어 조회되지 않도록 함
     *
     * @param id 삭제할 사용자 ID
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));

        if (user.getDeletedAt() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 삭제된 사용자입니다.");
        }

        // 기존 필드를 복사하여 deletedAt만 추가된 새 객체로 재저장
        userRepository.save(
                User.builder()
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .userProfile(user.getUserProfile())
                        .createdAt(user.getCreatedAt())
                        .deletedAt(LocalDateTime.now())
                        .build()
        );
    }
}