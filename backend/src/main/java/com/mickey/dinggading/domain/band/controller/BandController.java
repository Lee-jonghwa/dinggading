package com.mickey.dinggading.domain.band.controller;

import com.mickey.dinggading.api.BandApi;
import com.mickey.dinggading.domain.band.service.BandService;
import com.mickey.dinggading.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BandController implements BandApi {

    private final BandService bandService;

    @Override
    public ResponseEntity<?> createBand(CreateBandRequest createBandRequest) {

        BandDTO bandDTO = bandService.createBand(createBandRequest);
        return ResponseEntity.ok(bandDTO);
    }

    @Override
    public ResponseEntity<?> deleteBand(Long bandId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getBand(Long bandId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getBands() {
        return null;
    }

    @Override
    public ResponseEntity<?> searchBands(String keyword, String sigun, Genre genre, Boolean jobOpening) {
        return null;
    }

    @Override
    public ResponseEntity<?> transferBandMaster(Long bandId, TransferBandMasterRequest transferBandMasterRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateBand(Long bandId, UpdateBandRequest updateBandRequest) {
        return null;
    }
}
