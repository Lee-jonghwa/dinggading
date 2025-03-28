package com.mickey.dinggading.domain.member.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "Record")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", nullable = false)
    @Comment("CHALLENGE, LIVE_HOUSE 등 어떤 항목에 대한 녹음인지")
    private RecordType recordType;
    
    @Column(name = "title", nullable = false)
    @Comment("#월 #일 {악기} 도전 기록")
    private String title;
    
    @Column(name = "record_url", nullable = false)
    @Comment("내 노래 음성파일 저장된 서버 url")
    private String recordUrl;
    
    public enum RecordType {
        PRACTICE, RANK, LIVE_HOUSE
    }
}