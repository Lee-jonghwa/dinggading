package com.mickey.dinggading.domain.member.controller;

import com.mickey.dinggading.api.MemberApi;
import com.mickey.dinggading.api.NotificationApi;
import com.mickey.dinggading.domain.member.service.NotificationService;
import com.mickey.dinggading.model.NotificationDTO;
import com.mickey.dinggading.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationApi {

    private final NotificationService notificationService;
    private final SecurityUtil securityUtil;

    @GetMapping(value = "/api/notifications/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        // 토큰에서 사용자 ID 추출
        UUID memberId = securityUtil.getCurrentMemberId();
        return notificationService.subscribe(memberId);
    }

    /**
     * POST /api/notifications/accept/{notificationId} : 알림 수락
     * 밴드 가입 요청 등의 알림을 수락합니다.
     *
     * @param notificationId 알림 ID (required)
     * @return NotificationDTO 알림 수락 성공 (status code 200)
     * or 인증되지 않은 요청입니다. (status code 401)
     * or 권한이 없는 요청입니다. (status code 403)
     * or 요청한 리소스를 찾을 수 없습니다. (status code 404)
     * @deprecated
     */
    @Override
    public ResponseEntity<?> acceptNotification(Long notificationId) {
        return null;
    }

    /**
     * DELETE /api/notifications/{notificationId} : 알림 삭제
     * 특정 알림을 삭제합니다.
     *
     * @param notificationId 알림 ID (required)
     * @return Void 알림 삭제 성공 (status code 204)
     * or 인증되지 않은 요청입니다. (status code 401)
     * or 권한이 없는 요청입니다. (status code 403)
     * or 요청한 리소스를 찾을 수 없습니다. (status code 404)
     * @deprecated
     */
    @Override
    public ResponseEntity<?> deleteNotification(Long notificationId) {
        return null;
    }

    /**
     * GET /api/notifications : 알림 목록 조회
     * 현재 로그인한 사용자의 알림 목록을 조회합니다.
     *
     * @param pageable 페이징 정보
     * @return 알림 목록 (status code 200)
     */
    @Override
    public ResponseEntity<?> getNotifications(Pageable pageable) {
        return getNotifications(pageable, null);
    }

    @GetMapping("/api/notifications")
    public ResponseEntity<?> getNotifications(Pageable pageable,
                                              @RequestParam(required = false) Boolean unreadOnly) {
        UUID memberId = securityUtil.getCurrentMemberId();
        Page<NotificationDTO> notifications = notificationService.getNotifications(memberId, pageable, unreadOnly);
        return ResponseEntity.ok(notifications);
    }

    /**
     * PUT /api/notifications/{notificationId}/read : 알림 읽음 처리
     * 특정 알림을 읽음 상태로 변경합니다.
     *
     * @param notificationId 알림 ID (required)
     * @return NotificationDTO 알림 읽음 처리 성공 (status code 200)
     * or 인증되지 않은 요청입니다. (status code 401)
     * or 알림 읽음 처리 권한이 없습니다. (status code 403)
     * or 알림을 찾을 수 없습니다. (status code 404)
     */
    @Override
    public ResponseEntity<?> markNotificationAsRead(Long notificationId) {
        return null;
    }

    /**
     * POST /api/notifications/reject/{notificationId} : 알림 거절
     * 밴드 가입 요청 등의 알림을 거절합니다.
     *
     * @param notificationId 알림 ID (required)
     * @return NotificationDTO 알림 거절 성공 (status code 200)
     * or 인증되지 않은 요청입니다. (status code 401)
     * or 권한이 없는 요청입니다. (status code 403)
     * or 요청한 리소스를 찾을 수 없습니다. (status code 404)
     * @deprecated
     */
    @Override
    public ResponseEntity<?> rejectNotification(Long notificationId) {
        return null;
    }
}