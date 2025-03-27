package com.mickey.dinggading.domain.band.service;

import com.mickey.dinggading.base.status.ErrorStatus;
import com.mickey.dinggading.domain.band.converter.BandConverter;
import com.mickey.dinggading.domain.band.model.entity.Band;
import com.mickey.dinggading.domain.band.repository.BandMemberRepository;
import com.mickey.dinggading.domain.band.repository.BandRepository;
import com.mickey.dinggading.domain.band.repository.ContactRepository;
import com.mickey.dinggading.domain.member.model.entity.Member;
import com.mickey.dinggading.domain.member.repository.MemberRepository;
import com.mickey.dinggading.exception.ExceptionHandler;
import com.mickey.dinggading.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
    public Band createBand(CreateBandRequest request, UUID memberId) {

        Member master = memberRepository.findById(memberId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Band band = BandConverter.toBand(request, master.getMemberId());
        bandRepository.save(band);

        return band;
    }
}