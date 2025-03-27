package com.mickey.dinggading.domain.band.controller;

import com.mickey.dinggading.api.BandApi;
import com.mickey.dinggading.domain.band.model.entity.Band;
import com.mickey.dinggading.domain.band.service.BandService;
import com.mickey.dinggading.domain.member.model.entity.Member;
import com.mickey.dinggading.model.*;
import com.mickey.dinggading.util.JWTUtil;
import com.mickey.dinggading.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/band")
public class BandController implements BandApi {

    private final BandService bandService;
    private final JWTUtil jwtUtil;
    private final SecurityUtil securityUtil;

    @Override
    public ResponseEntity<?> addBandMember(Long bandId, AddBandMemberRequest addBandMemberRequest) {
        return null;
    }

    @Override
    @PostMapping("/createBand")
    public ResponseEntity<?> createBand(CreateBandRequest createBandRequest) {

        UUID memberId = securityUtil.getCurrentMemberId();
        log.info("현재 로그인한 사용자 ID: {}", memberId);

        Band band = bandService.createBand(createBandRequest, memberId);
        return ResponseEntity.ok(band);
    }

    @Override
    public ResponseEntity<?> createBandContact(Long bandId, CreateBandContactRequest createBandContactRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteBand(Long bandId) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteBandContact(Long bandId, Long contactId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getBand(Long bandId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getBandContacts(Long bandId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getBandMembers(Long bandId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getBands() {
        return null;
    }

    @Override
    public ResponseEntity<?> removeBandMember(Long bandId, UUID memberId) {
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

    @Override
    public ResponseEntity<?> updateBandContact(Long bandId, Long contactId, CreateBandContactRequest createBandContactRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateBandMemberInstrument(Long bandId, UUID memberId, UpdateBandMemberInstrumentRequest updateBandMemberInstrumentRequest) {
        return null;
    }
}
