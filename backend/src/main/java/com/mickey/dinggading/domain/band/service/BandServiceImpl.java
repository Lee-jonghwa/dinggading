package com.mickey.dinggading.domain.band.service;

import com.mickey.dinggading.domain.band.repository.BandMemberRepository;
import com.mickey.dinggading.domain.band.repository.BandRepository;
import com.mickey.dinggading.domain.band.repository.ContactRepository;
import com.mickey.dinggading.domain.member.repository.MemberRepository;
import com.mickey.dinggading.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BandServiceImpl implements BandService {

    private final BandRepository bandRepository;
    private final BandMemberRepository bandMemberRepository;
    private final ContactRepository contactRepository;
    private final MemberRepository memberRepository;

    @Override
    public BandDTO createBand(CreateBandRequest request) {
        return null;
    }
}