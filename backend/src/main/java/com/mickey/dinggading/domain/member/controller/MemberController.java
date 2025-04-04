package com.mickey.dinggading.domain.member.controller;

import com.mickey.dinggading.api.MemberApi;
import com.mickey.dinggading.domain.member.model.entity.Member;
import com.mickey.dinggading.domain.member.model.entity.Token;
import com.mickey.dinggading.domain.member.repository.MemberRepository;
import com.mickey.dinggading.model.UpdateMemberRequest;
import com.mickey.dinggading.util.GenerateRandomNickname;
import com.mickey.dinggading.util.JWTUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    @PostMapping("/api/users")
    public ResponseEntity<Token> testLogin() {
        String nickname = GenerateRandomNickname.generateRandomNickname();
        Member member = Member.createMember(nickname, nickname, null);
        Member save = memberRepository.save(member);
        return ResponseEntity.ok(jwtUtil.createAccessToken(save));
    }

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
