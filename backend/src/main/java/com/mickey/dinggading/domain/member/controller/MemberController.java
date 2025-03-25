package com.mickey.dinggading.domain.member.controller;

import com.mickey.dinggading.api.MemberApi;
import com.mickey.dinggading.model.UpdateMemberRequest;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public class MemberController implements MemberApi {
    @Override
    public ResponseEntity<?> getCurrentMember() {
        return null;
    }

    @Override
    public ResponseEntity<?> getMember(UUID memberId) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateFavoriteBand(Long bandId) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateMember(UUID memberId, UpdateMemberRequest updateMemberRequest) {
        return null;
    }
}
