// 파일 경로: src/main/java/com/mickey/dinggading/domain/livehouse/service/LivehouseService.java
package com.mickey.dinggading.domain.livehouse.service;

import com.mickey.dinggading.domain.livehouse.dto.CreateLivehouseRequestDTO;
import com.mickey.dinggading.domain.livehouse.dto.LivehouseDTO;
import com.mickey.dinggading.domain.livehouse.dto.LivehouseSessionDTO;
import com.mickey.dinggading.domain.livehouse.dto.ParticipantDTO;
import com.mickey.dinggading.domain.livehouse.entity.Livehouse;
import com.mickey.dinggading.domain.livehouse.entity.Participant;
import com.mickey.dinggading.domain.livehouse.repository.LivehouseRepository;
import com.mickey.dinggading.domain.livehouse.repository.ParticipantRepository;
import io.openvidu.java.client.Connection;
import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.ConnectionType;
import io.openvidu.java.client.MediaMode;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.RecordingMode;
import io.openvidu.java.client.Session;
import io.openvidu.java.client.SessionProperties;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LivehouseService {

    private final LivehouseRepository livehouseRepository;
    private final ParticipantRepository participantRepository;
    private final NicknameService nicknameService;

    private OpenVidu openVidu;

    @Value("${openvidu.url}")
    private String OPENVIDU_URL;

    @Value("${openvidu.secret}")
    private String OPENVIDU_SECRET;

    @PostConstruct
    public void init() {
        this.openVidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    @Transactional(readOnly = true)
    public Page<LivehouseDTO> listLivehouses(Pageable pageable) {
        return livehouseRepository.findByStatus(Livehouse.LivehouseStatus.ACTIVE, pageable)
                .map(LivehouseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public LivehouseDTO getLivehouse(Long livehouseId) {
        Livehouse livehouse = livehouseRepository.findById(livehouseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "라이브하우스를 찾을 수 없습니다."));
        return LivehouseDTO.fromEntity(livehouse);
    }

    @Transactional
    public LivehouseSessionDTO createLivehouse(CreateLivehouseRequestDTO requestDTO) {
        try {
            // OpenVidu 세션 생성
            String sessionId = createOpenViduSession();

            // 랜덤 닉네임 생성
            String nickname = nicknameService.generateRandomNickname();

            // 임시 호스트 ID 생성 (실제 서비스에서는 인증된 사용자 ID 사용)
            Long hostId = ThreadLocalRandom.current().nextLong(1000, 10000);

            // 라이브하우스 엔티티 생성
            Livehouse livehouse = Livehouse.builder()
                    .title(requestDTO.getTitle())
                    .description(requestDTO.getDescription())
                    .hostId(hostId)
                    .hostNickname(nickname)
                    .sessionId(sessionId)
                    .createdAt(LocalDateTime.now())
                    .maxParticipants(requestDTO.getMaxParticipants())
                    .status(Livehouse.LivehouseStatus.ACTIVE)
                    .build();

            livehouseRepository.save(livehouse);

            // 호스트를 첫 번째 참여자로 등록
            Participant host = Participant.builder()
                    .livehouse(livehouse)
                    .nickname(nickname)
                    .isHost(true)
                    .joinedAt(LocalDateTime.now())
                    .build();

            participantRepository.save(host);

            // OpenVidu 토큰 생성
            String token = createConnectionToken(sessionId);

            return LivehouseSessionDTO.builder()
                    .livehouseId(livehouse.getLivehouseId())
                    .sessionId(sessionId)
                    .token(token)
                    .participantId(host.getParticipantId())
                    .nickname(nickname)
                    .build();

        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "OpenVidu 서버 오류", e);
        }
    }

    // LivehouseService.java의 joinLivehouse 메서드 수정
    @Transactional
    public LivehouseSessionDTO joinLivehouse(Long livehouseId) {
        Livehouse livehouse = livehouseRepository.findById(livehouseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "라이브하우스를 찾을 수 없습니다."));

        if (livehouse.getStatus() == Livehouse.LivehouseStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "종료된 라이브하우스입니다.");
        }

        if (livehouse.getParticipantCount() >= livehouse.getMaxParticipants()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "라이브하우스가 가득 찼습니다.");
        }

        try {
            // 랜덤 닉네임 생성
            String nickname = nicknameService.generateRandomNickname();

            // 참여자 등록
            Participant participant = Participant.builder()
                    .livehouse(livehouse)
                    .nickname(nickname)
                    .isHost(false)
                    .joinedAt(LocalDateTime.now())
                    .build();

            participantRepository.save(participant);

            // OpenVidu 세션을 가져오거나 생성
            String sessionId = livehouse.getSessionId();
            Session session = null;

            // 1. 기존 세션 사용 시도
            try {
                session = openVidu.getActiveSession(sessionId);
                System.out.println("기존 세션을 찾았습니다: " + sessionId);
            } catch (Exception e) {
                System.out.println("기존 세션을 찾을 수 없습니다: " + e.getMessage());
                session = null; // 명시적으로 null 설정
            }

            // 2. 세션이 없으면 새로 생성
            if (session == null) {
                System.out.println("새 세션을 생성합니다.");
                SessionProperties properties = new SessionProperties.Builder()
                        .mediaMode(MediaMode.ROUTED)
                        .recordingMode(RecordingMode.MANUAL)
                        .build();

                session = openVidu.createSession(properties);

                // 세션 ID 업데이트
                String newSessionId = session.getSessionId();
                System.out.println("새 세션 ID: " + newSessionId);
                livehouse.setSessionId(newSessionId);
                livehouseRepository.save(livehouse);
                sessionId = newSessionId; // sessionId 변수 업데이트
            }

            // 3. 연결 속성 설정 및 데이터 포함
            System.out.println("세션 " + sessionId + "에 대한 연결을 생성합니다.");
            ConnectionProperties properties = new ConnectionProperties.Builder()
                    .type(ConnectionType.WEBRTC)
                    .role(OpenViduRole.PUBLISHER)
                    .data(String.format("{\"nickname\": \"%s\"}", nickname))
                    .build();

            // 4. 세션에 대한 연결 생성
            Connection connection = session.createConnection(properties);
            String token = connection.getToken();
            System.out.println("생성된 토큰: " + token.substring(0, 20) + "...");

            return LivehouseSessionDTO.builder()
                    .livehouseId(livehouse.getLivehouseId())
                    .sessionId(sessionId)
                    .token(token)
                    .participantId(participant.getParticipantId())
                    .nickname(nickname)
                    .build();

        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            e.printStackTrace(); // 스택 트레이스 출력
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "OpenVidu 서버 오류: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void leaveLivehouse(Long livehouseId, Long participantId) {
        Livehouse livehouse = livehouseRepository.findById(livehouseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "라이브하우스를 찾을 수 없습니다."));

        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "참여자를 찾을 수 없습니다."));

        if (!participant.getLivehouse().getLivehouseId().equals(livehouseId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 라이브하우스의 참여자가 아닙니다.");
        }

        // 참여자 삭제
        participantRepository.delete(participant);

        // 방장이 나갈 경우 라이브하우스 종료
        if (participant.isHost()) {
            closeLivehouse(livehouseId, participantId);
        }
    }

    @Transactional
    public void closeLivehouse(Long livehouseId, Long participantId) {
        Livehouse livehouse = livehouseRepository.findById(livehouseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "라이브하우스를 찾을 수 없습니다."));

        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "참여자를 찾을 수 없습니다."));

        if (!participant.isHost()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "라이브하우스 종료 권한이 없습니다.");
        }

        try {
            // OpenVidu 세션 종료
            Session session = openVidu.getActiveSession(livehouse.getSessionId());
            if (session != null) {
                session.close();
            }

            // 라이브하우스 상태 변경
            livehouse.close();
            livehouseRepository.save(livehouse);

        } catch (OpenViduJavaClientException | OpenViduHttpException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "OpenVidu 서버 오류", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ParticipantDTO> listParticipants(Long livehouseId) {
        Livehouse livehouse = livehouseRepository.findById(livehouseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "라이브하우스를 찾을 수 없습니다."));

        return participantRepository.findByLivehouse(livehouse).stream()
                .map(ParticipantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private String createOpenViduSession() throws OpenViduJavaClientException, OpenViduHttpException {
        // 세션 속성 설정 - RecordingMode.MANUAL로 통일
        SessionProperties properties = new SessionProperties.Builder()
                .mediaMode(MediaMode.ROUTED)
                .recordingMode(RecordingMode.MANUAL)
                .build();

        // 세션 생성
        Session session = openVidu.createSession(properties);
        System.out.println("새 세션 생성: " + session.getSessionId());
        return session.getSessionId();
    }

    private String createConnectionToken(String sessionId) throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openVidu.getActiveSession(sessionId);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "세션을 찾을 수 없습니다.");
        }

        ConnectionProperties properties = new ConnectionProperties.Builder()
                .type(ConnectionType.WEBRTC)
                .role(OpenViduRole.PUBLISHER)
                .build();

        Connection connection = session.createConnection(properties);
        return connection.getToken();
    }
}