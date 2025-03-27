package com.mickey.dinggading.domain.band.service;

import com.mickey.dinggading.domain.band.model.entity.Band;
import com.mickey.dinggading.domain.band.model.entity.BandMember;
import com.mickey.dinggading.model.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BandService {

    // 밴드 관련 기능
    Band createBand(CreateBandRequest request, UUID memberId);

    List<BandMemberDTO> addBandMember(UUID memberId, Long bandId, AddBandMemberRequest addBandMemberRequest);

    void deleteBand(Long bandId, UUID currentMemberId);

    PageBandDTO getAllBands(Pageable pageable);

    BandDTO getBandById(Long bandId);

    List<BandMemberDTO> getBandMembers(Long bandId, Pageable pageable);

    void removeBandMember(Long bandId, UUID memberIdToRemove, UUID requesterId);

    PageBandDTO searchBands(String keyword, String sigun, Boolean jobOpening, Pageable pageable);

    BandDTO updateBand(Long bandId, UpdateBandRequest updateBandRequest, UUID requesterId);

    ContactDTO updateBandContact(Long bandId, Long contactId, CreateBandContactRequest request, UUID memberId);

    BandMemberDTO updateBandMemberInstrument(Long bandId, UUID memberId, Instrument instrument, UUID requesterId);

    ContactDTO createBandContact(Long bandId, CreateBandContactRequest request, UUID memberId);

    void deleteBandContact(Long bandId, Long contactId, UUID memberId);

    List<ContactDTO> getBandContacts(Long bandId);

    BandDTO transferBandMaster(Long bandId, UUID newMasterId, UUID currentMasterId);
}