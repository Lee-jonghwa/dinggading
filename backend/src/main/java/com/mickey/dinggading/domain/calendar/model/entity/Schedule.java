package com.mickey.dinggading.domain.calendar.model.entity;

import com.mickey.dinggading.domain.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Table(name = "schedule")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="calendar_id")
    private Calendar calendar;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "time", nullable = false)
    private LocalDate time;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "sticker")
    private String sticker;

    @Column(name = "created_by")
    private UUID createdBy;
}