package com.dannyPlaygroud.noteAnywhere.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity // 이 클래스는 JPA가 관리하는 엔티티이며, DB의 테이블과 매핑됩니다.
@Table(name = "user") // 이 엔티티는 user라는 이름의 테이블과 매핑됩니다.
@Getter // 모든 필드의 Getter 메서드를 Lombok이 자동 생성해줍니다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 생성하되, 외부에서 직접 생성하지 못하게 보호합니다.
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자를 Lombok이 생성해줍니다.
@Builder // 빌더 패턴으로 객체를 생성할 수 있게 합니다.
public class User {

    @Id // 기본키(primary key)로 지정합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL의 AUTO_INCREMENT 전략으로 ID를 생성합니다.
    private Long userId;

    // 사용자 이름 (null 불가 조건 없음, 기본 문자열)
    private String userName;

    // 프로필 이미지 URL
    private String userProfile;

    // 가입 시각 (엔티티 저장 시 자동 설정됨)
    private LocalDateTime createdAt;

    // 탈퇴 시각 (soft delete에 사용)
    private LocalDateTime deletedAt;

    @PrePersist // 엔티티가 저장되기 전에 실행되는 메서드
    public void onCreate() {
        this.createdAt = LocalDateTime.now(); // 최초 생성 시 현재 시간으로 createdAt 설정
    }
}