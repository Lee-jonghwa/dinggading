package com.mickey.dinggading.domain.band.converter;

import com.mickey.dinggading.domain.band.model.entity.Band;
import com.mickey.dinggading.model.BandDTO;

public class BandConverter {

    public static BandDTO toBandDTO(Band band, int memberCount) {
        return BandDTO.builder()
            .bandId(band.getBandId())
            .name(band.getName())
            .bandMasterId(band.getBandMaster().getMemberId())
            .description(band.getDescription())
            .sigun(band.getSigun())
            .tags(band.getTags())
            .profileUrl(band.getProfileUrl())
            .maxSize(band.getMaxSize())
            .jobOpening(band.isJobOpening())
            .memberCount(memberCount)
            .build();
    }
}
