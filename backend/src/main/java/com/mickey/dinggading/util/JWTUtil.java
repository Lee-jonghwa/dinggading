package com.mickey.dinggading.util;

import com.mickey.dinggading.base.status.ErrorStatus;
import com.mickey.dinggading.domain.member.model.entity.Member;
import com.mickey.dinggading.domain.member.model.entity.Token;
import com.mickey.dinggading.exception.ExceptionHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JWTUtil {
    //
    @Value("#{${jwt.access-token.expiretime} * 60 * 1000}")
    private int tokenExpireTime;

    @Value("${jwt.secret-key}")
    private String secretKey;

    // 토큰 발행해주기
    public Token createAccessToken(Member member) {
        return Token.builder()
                .accessToken(
                        create(member, tokenExpireTime)
                )
                .build();
    }

    // 토큰에서 사용자 Id 추출
    public UUID getMemberId(String header) {
        String token = getToken(header);
        Jws<Claims> claims = getClaimsJws(token);

        Map<String, Object> value = claims.getPayload();
        log.info("value : {}", value);

        return UUID.fromString(value.get("id").toString());
    }

    // 토큰에서 사용자 로그인이메일 추출
    public String getUsername(String header) {
        String token = getToken(header);
        Jws<Claims> claims = getClaimsJws(token);

        Map<String, Object> value = claims.getPayload();
        log.info("value : {}", value);

        return value.get("username").toString();
    }

    public void checkTokenValidation(String header) {
        if (header == null) throw new ExceptionHandler(ErrorStatus.TOKEN_NOT_FOUND);
        String token = getToken(header);
        Jws<Claims> claimsJws = getClaimsJws(token);
        claimsJws.getPayload().forEach((key1, value1) -> log.info("key : {}, value : {}", key1, value1));
    }

    // 토큰 검증
    private static String getToken(String header) {
        StringTokenizer st = new StringTokenizer(header);
        String token = st.nextToken();

        return "Bearer".equals(token) ? st.nextToken() : token;
    }

    //	Token 발급
    private String create(Member member, long expireTime) {
        SecretKey key = getSecretKey();
        Map<String, String> headers = new HashMap<>();
        headers.put("typ", "JWT");

        String accessToken = Jwts.builder()
                .header()
                .add(headers)
                .and()
                .subject("accessToken")
                .claim("username", member.getUsername()) // 이메일 정보 = 로그인 아이디 를 토큰에 저장
                .claim("id", member.getMemberId().toString()) // 사용자 ID 를 토큰에 저장
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, Jwts.SIG.HS256)
                .compact();

        return accessToken;
    }

    // 토큰 유효성 검사
    private Jws<Claims> getClaimsJws(String token) {
        JwtParser parser = getParser();
        Jws<Claims> claimsJws;

        try {
            claimsJws = parser.parseSignedClaims(token);
        } catch (Exception e) {
            log.error("토큰이 유효하지 않습니다.");
            ErrorStatus errorStatus = switch (e.getClass().getSimpleName()) {
                case "ExpiredJwtException" -> ErrorStatus.TOKEN_EXPIRED_ERROR;
                case "UnsupportedJwtException" -> ErrorStatus.TOKEN_UNSUPPORTED_ERROR;
                case "MalformedJwtException" -> ErrorStatus.TOKEN_MALFORMED_ERROR;
                case "SignatureException" -> ErrorStatus.TOKEN_SIGNATURE_ERROR;
                case "IllegalArgumentException" -> ErrorStatus.TOKEN_ILLEGAL_ARGUMENT_ERROR;
                default -> ErrorStatus.TOKEN_ERROR; // 알 수 없는 예외 처리
            };
            throw new ExceptionHandler(errorStatus);
        }
        return claimsJws;
    }

    // 시크릿 키 가져오기
    private SecretKey getSecretKey() {
        byte[] secretKeyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

    private JwtParser getParser() {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build();
    }

}
