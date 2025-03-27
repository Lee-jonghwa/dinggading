package com.mickey.dinggading.domain.band.converter;

import com.mickey.dinggading.domain.band.model.entity.Band;
import com.mickey.dinggading.model.CreateBandRequest;

import java.util.UUID;

public class BandConverter {

    public static Band toBand(CreateBandRequest request, UUID masterId) {
        return Band.builder()
                .name(request.getName())
                .bandMasterId(masterId)
                .description(request.getDescription())
                .sigun(request.getSigun())
                .tags(request.getTags())
                .profileUrl(request.getProfileUrl())
                .maxSize(request.getMaxSize())
                .jobOpening(request.getJobOpening())
                .build();
    }
}
