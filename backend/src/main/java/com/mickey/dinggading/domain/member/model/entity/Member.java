package com.mickey.dinggading.domain.member.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import java.util.UUID;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "member_id")
    private UUID memberId;

    @Column(name = "username", unique = true, nullable = false)
    @Comment("사용자 로그인 아이디")
    private String username;

    @Column(name = "password", nullable = false)
    @Comment("비밀 번호")
    private String password;

    @Column(name = "nickname", nullable = false)
    @Comment("사용자 닉네임 (자동 생성)")
    private String nickname;

    @Column(name = "favorite_band_id")
    @Comment("즐겨찾기 된 밴드")
    private Long favoriteBandId;

    @Column(name = "profile_img_url", nullable = false)
    @Comment("프로필 이미지")
    private String profileImgUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성된 날자")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records = new ArrayList<>();

    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "followedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> sentNotifications = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> receivedNotifications = new ArrayList<>();

    // Update methods
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void updateFavoriteBand(Long favoriteBandId) {
        this.favoriteBandId = favoriteBandId;
    }
}