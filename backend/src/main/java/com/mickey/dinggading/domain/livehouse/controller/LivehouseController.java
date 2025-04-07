// 파일 경로: src/main/java/com/mickey/dinggading/domain/livehouse/controller/LivehouseController.java
package com.mickey.dinggading.domain.livehouse.controller;

import com.mickey.dinggading.domain.livehouse.dto.CreateLivehouseRequestDTO;
import com.mickey.dinggading.domain.livehouse.dto.LivehouseDTO;
import com.mickey.dinggading.domain.livehouse.dto.LivehouseSessionDTO;
import com.mickey.dinggading.domain.livehouse.dto.ParticipantDTO;
import com.mickey.dinggading.domain.livehouse.service.LivehouseService;
import com.mickey.dinggading.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/livehouses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 개발 환경에서만 사용. 실제 환경에서는 특정 도메인만 허용하도록 설정
public class LivehouseController {

    private final LivehouseService livehouseService;
    private final SecurityUtil securityUtil;

    @GetMapping
    public ResponseEntity<Page<LivehouseDTO>> listLivehouses(Pageable pageable) {
        log.info("listLivehouses 호출");
        return ResponseEntity.ok(livehouseService.listLivehouses(pageable));
    }

    @GetMapping("/{livehouseId}")
    public ResponseEntity<LivehouseDTO> getLivehouse(@PathVariable Long livehouseId) {
        log.info("getLivehouse - /livehouseId 호출");
        return ResponseEntity.ok(livehouseService.getLivehouse(livehouseId));
    }

    @PostMapping
    public ResponseEntity<LivehouseSessionDTO> createLivehouse(
        @Valid @RequestBody CreateLivehouseRequestDTO requestDTO) {
        log.info("createLivehouse 호출");

        UUID memberId = securityUtil.getCurrentMemberId();

        LivehouseSessionDTO livehouse = livehouseService.createLivehouse(requestDTO, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(livehouse);
    }

    @PostMapping("/{livehouseId}/join")
    public ResponseEntity<LivehouseSessionDTO> joinLivehouse(@PathVariable Long livehouseId) {
        log.info("joinLivehouse - /{livehouseId}/join 호출");

        UUID memberId = securityUtil.getCurrentMemberId();

        return ResponseEntity.ok(livehouseService.joinLivehouse(livehouseId, memberId));
    }

    @PostMapping("/{livehouseId}/leave/{participantId}")
    public ResponseEntity<Void> leaveLivehouse(@PathVariable Long livehouseId, @PathVariable Long participantId) {
        log.info("leaveLivehouse - /{livehouseId}/leave/{participantId 호출");
        livehouseService.leaveLivehouse(livehouseId, participantId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{livehouseId}/close")
    public ResponseEntity<Void> closeLivehouse(
        @PathVariable Long livehouseId,
        @RequestParam Long participantId) {
        log.info("closeLivehouse - /{livehouseId}/close 호출");
        livehouseService.closeLivehouse(livehouseId, participantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{livehouseId}/participants")
    public ResponseEntity<List<ParticipantDTO>> listParticipants(@PathVariable Long livehouseId) {
        log.info("listParticipants - /livehouseId/participants 호출");
        return ResponseEntity.ok(livehouseService.listParticipants(livehouseId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<LivehouseDTO>> searchLivehouses(@RequestParam("keyword") String keyword, Pageable pageable) {
        log.info("searchLivehouses - /search/keyword 호출");
        return ResponseEntity.ok(livehouseService.searchLivehouses(keyword, pageable));
    }
}