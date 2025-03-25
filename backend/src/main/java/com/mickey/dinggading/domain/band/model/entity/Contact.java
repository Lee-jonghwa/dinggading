package com.mickey.dinggading.domain.band.model.entity;

import com.mickey.dinggading.base.BaseEntity;
import com.mickey.dinggading.model.Sns;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Contact")
public class Contact extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long contactId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id", nullable = false)
    private Band band;

    @Enumerated(EnumType.STRING)
    @Column(name = "sns", nullable = false)
    private Sns sns;

    @Column(name = "title")
    private String title;

    @Column(name = "URL", nullable = false)
    private String url;

}