package com.mickey.dinggading.domain.oauth.service;

import com.mickey.dinggading.domain.member.model.entity.Member;
import com.mickey.dinggading.domain.member.repository.MemberRepository;
import com.mickey.dinggading.domain.oauth.MemberPrincipal;
import com.mickey.dinggading.domain.oauth.provider.GoogleOAuth2UserInfo;
import com.mickey.dinggading.util.GenerateRandomNickname;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    // http://localhost:8080/oauth2/authorization/google

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        log.info("loadUser: {}", oAuth2User);
        try {
            return processOAuth2User(oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    // 구글에서 받아온 정보를 DB 에 저장
    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        log.info("processOAuth2User: {}", oAuth2User);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        GoogleOAuth2UserInfo googleOAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);

        // 회원 가입
        Member member = memberRepository.findByUsername(googleOAuth2UserInfo.getEmail())
                .orElseGet(() -> register(googleOAuth2UserInfo));

        // 업데이트
        member = updateMember(member, googleOAuth2UserInfo);

        log.info("updateMember: {}", member);
        return MemberPrincipal.create(member, attributes);
    }



    // 사용자 DB 저장
    private Member register(GoogleOAuth2UserInfo oAuth2UserInfo) {
        log.info("register: {}", oAuth2UserInfo);
        String nickname = GenerateRandomNickname.generateRandomNickname();

        Member member = Member.builder()
                .id(UUID.randomUUID())
                .username(oAuth2UserInfo.getEmail())
                .nickname(nickname) // 자동 생성
                .profileImgUrl(oAuth2UserInfo.getImageUrl())
                .build();
        return memberRepository.save(member);
    }

    private Member updateMember(Member existingMember, GoogleOAuth2UserInfo oAuth2UserInfo) {
        log.info("updateMember: {}", existingMember);
        existingMember.setProfileImgUrl(oAuth2UserInfo.getImageUrl());
        return memberRepository.save(existingMember);
    }
}
