package com.mickey.dinggading.domain.livehouse.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "livehouses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long livehouseId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Long hostId;

    @Column(nullable = false)
    private String hostNickname;

    @Column(nullable = false)
    private String sessionId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LivehouseStatus status;

    // 연관관계 설정
    @OneToMany(mappedBy = "livehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    // 참여자 수를 반환하는 메서드
    public int getParticipantCount() {
        return participants.size();
    }

    // 라이브하우스 상태를 변경하는 메서드
    public void close() {
        this.status = LivehouseStatus.CLOSED;
    }

    public enum LivehouseStatus {
        ACTIVE, CLOSED
    }
}