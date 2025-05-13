package com.dannyPlaygroud.noteAnywhere.domain.user;

import com.dannyPlaygroud.noteAnywhere.domain.user.dto.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController는 사용자 관련 요청을 처리하는 REST API 컨트롤러입니다.
 * @RestController: JSON 형태로 응답을 반환하는 컨트롤러임을 명시
 * @RequestMapping("/users"): 모든 요청은 /users 경로를 기준으로 처리
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor // final 필드를 생성자로 주입 (Lombok)
public class UserController {

    private final UserService userService;

    /**
     * [GET] /user
     * 전체 사용자 목록을 조회합니다.
     * soft delete된 사용자는 포함되지 않습니다.
     *
     * @return 삭제되지 않은 사용자 리스트 (200 OK)
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * [GET] /users/{id}
     * 특정 사용자 ID로 사용자 정보를 조회합니다.
     * soft delete된 사용자는 조회되지 않습니다.
     *
     * @param id 사용자 ID (PathVariable)
     * @return 해당 사용자 정보 (200 OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * [POST] /users
     * 새로운 사용자를 생성합니다.
     *
     * @param request 사용자 생성 요청 DTO (RequestBody)
     * @return 생성된 사용자 정보 (200 OK)
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    /**
     * [DELETE] /users/{id}
     * 사용자를 soft delete 처리합니다.
     * 실제 삭제하지 않고 deletedAt 필드만 갱신합니다.
     *
     * @param id 사용자 ID (PathVariable)
     * @return 204 No Content (성공적으로 삭제됨)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 응답 반환
    }
}
