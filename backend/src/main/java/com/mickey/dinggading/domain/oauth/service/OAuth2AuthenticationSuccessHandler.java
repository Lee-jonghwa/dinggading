package com.mickey.dinggading.domain.oauth.service;

import com.mickey.dinggading.base.status.ErrorStatus;
import com.mickey.dinggading.domain.member.model.entity.Member;
import com.mickey.dinggading.domain.member.model.entity.Token;
import com.mickey.dinggading.domain.member.repository.MemberRepository;
import com.mickey.dinggading.domain.oauth.MemberPrincipal;
import com.mickey.dinggading.util.JWTUtil;
import com.mickey.dinggading.exception.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException {

        log.info("onAuthenticationSuccess 호출");
        MemberPrincipal userPrincipal = (MemberPrincipal) authentication.getPrincipal();
        Member member = memberRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.MEMBER_NOT_FOUND));
        
        // 토큰 생성
        Token jwtToken = jwtUtil.createAccessToken(member);

        memberRepository.save(member);

        response.setHeader("Authorization", "Bearer " + jwtToken.getAccessToken());

        // 토큰과 사용자 정보를 함께 전달
        // URL 파라미터 인코딩
        String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/login/oauth2/code/google")
                .queryParam("token", jwtToken.getAccessToken())
                .queryParam("loginId", member.getUsername())
                .queryParam("nickname", member.getNickname())
                .queryParam("profileImg", member.getProfileImgUrl() != null ? member.getProfileImgUrl() : "")
                .build()
                .encode()  // 한 번만 인코딩
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
