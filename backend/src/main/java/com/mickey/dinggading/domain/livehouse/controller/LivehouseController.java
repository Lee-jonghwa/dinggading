// 파일 경로: src/main/java/com/mickey/dinggading/domain/livehouse/controller/LivehouseController.java
package com.mickey.dinggading.domain.livehouse.controller;

import com.mickey.dinggading.domain.livehouse.dto.CreateLivehouseRequestDTO;
import com.mickey.dinggading.domain.livehouse.dto.LivehouseDTO;
import com.mickey.dinggading.domain.livehouse.dto.LivehouseSessionDTO;
import com.mickey.dinggading.domain.livehouse.dto.ParticipantDTO;
import com.mickey.dinggading.domain.livehouse.service.LivehouseService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/livehouses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 개발 환경에서만 사용. 실제 환경에서는 특정 도메인만 허용하도록 설정
public class LivehouseController {

    private final LivehouseService livehouseService;

    @GetMapping
    public ResponseEntity<Page<LivehouseDTO>> listLivehouses(Pageable pageable) {
        return ResponseEntity.ok(livehouseService.listLivehouses(pageable));
    }

    @GetMapping("/{livehouseId}")
    public ResponseEntity<LivehouseDTO> getLivehouse(@PathVariable Long livehouseId) {
        return ResponseEntity.ok(livehouseService.getLivehouse(livehouseId));
    }

    @PostMapping
    public ResponseEntity<LivehouseSessionDTO> createLivehouse(
            @Valid @RequestBody CreateLivehouseRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livehouseService.createLivehouse(requestDTO));
    }

    @PostMapping("/{livehouseId}/join")
    public ResponseEntity<LivehouseSessionDTO> joinLivehouse(@PathVariable Long livehouseId) {
        return ResponseEntity.ok(livehouseService.joinLivehouse(livehouseId));
    }

    @PostMapping("/{livehouseId}/leave/{participantId}")
    public ResponseEntity<Void> leaveLivehouse(@PathVariable Long livehouseId, @PathVariable Long participantId) {
        livehouseService.leaveLivehouse(livehouseId, participantId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{livehouseId}/close")
    public ResponseEntity<Void> closeLivehouse(
            @PathVariable Long livehouseId,
            @RequestParam Long participantId) {
        livehouseService.closeLivehouse(livehouseId, participantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{livehouseId}/participants")
    public ResponseEntity<List<ParticipantDTO>> listParticipants(@PathVariable Long livehouseId) {
        return ResponseEntity.ok(livehouseService.listParticipants(livehouseId));
    }
}