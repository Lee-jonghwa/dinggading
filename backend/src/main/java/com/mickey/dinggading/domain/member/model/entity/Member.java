package com.mickey.dinggading.domain.member.model.entity;

import com.mickey.dinggading.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends BaseEntity {
    @Id
    @Column(name="member_id")
    private UUID id;

    @Column(name="username")
    // "Google OAuth 로 받아온 이메일이 로그인 아이디"
    private String username;

    @Column(name="nickname")
    // 닉네임은 자동생성
    @Setter
    private String nickname;

    @Column(name="profile_img_url")
    @Setter
    private String profileImgUrl;

    @Column(name="token")
    @Setter
    private String token;
}
