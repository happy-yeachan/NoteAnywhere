package com.dannyPlaygroud.noteAnywhere.domain.user.dto;

/**
 * 사용자를 생성할 때 클라이언트로부터 전달받는 요청 데이터를 담는 DTO입니다.
 *
 * 이 클래스는 주로 POST /users API의 요청 바디를 매핑하는 데 사용되며,
 * userName과 userProfile 필드를 포함합니다.
 *
 * Java 16 이상의 record 문법을 사용하여 생성자, getter, equals, hashCode 등을 자동으로 생성합니다.
 */
public record UserCreateRequest(
        String userName,      // 사용자 이름
        String userProfile    // 프로필 이미지 URL
) {}