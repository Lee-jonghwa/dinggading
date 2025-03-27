package com.mickey.dinggading.domain.band.model.entity;

import com.mickey.dinggading.base.BaseEntity;
import com.mickey.dinggading.domain.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Band extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_id")
    private Long bandId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "band_master_id", nullable = false)
    private UUID bandMasterId;

    @Column(name = "description")
    private String description;

    @Column(name = "sigun", nullable = false)
    private String sigun;

    @Column(name = "tags")
    private String tags;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "max_size")
    private Integer maxSize;

    @Column(name = "job_opening", nullable = false)
    private boolean jobOpening;

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BandMember> members = new ArrayList<>();
}