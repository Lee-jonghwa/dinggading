package com.mickey.dinggading.domain.record.service;

import com.mickey.dinggading.base.status.ErrorStatus;
import com.mickey.dinggading.domain.member.model.entity.Member;
import com.mickey.dinggading.domain.member.repository.MemberRepository;
import com.mickey.dinggading.domain.membermatching.repository.AttemptRepository;
import com.mickey.dinggading.domain.memberrank.model.Attempt;
import com.mickey.dinggading.domain.record.RecordConverter;
import com.mickey.dinggading.domain.record.model.ChallengeType;
import com.mickey.dinggading.domain.record.model.Record;
import com.mickey.dinggading.domain.record.repository.AudioRecordRepository;
import com.mickey.dinggading.exception.ExceptionHandler;
import com.mickey.dinggading.infra.minio.AudioFileService;
import com.mickey.dinggading.model.RecordCreateRequestDTO;
import com.mickey.dinggading.model.RecordDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final AudioRecordRepository audioRecordRepository;
    private final MemberRepository memberRepository;
    private final AttemptRepository attemptRepository;
    private final AudioFileService audioFileService;
    private final RecordConverter recordConverter;

    /**
     * 새로운 녹음을 생성합니다.
     *
     * @param recordInfo 녹음 정보
     * @param audioFile  오디오 파일
     * @return 생성된 녹음 정보
     */
    @Transactional
    public RecordDTO createRecord(RecordCreateRequestDTO recordInfo, MultipartFile audioFile) {
        if (audioFile == null || audioFile.isEmpty()) {
            throw new ExceptionHandler(ErrorStatus.INVALID_AUDIO_FILE);
        }

        // 멤버 조회
        Member member = memberRepository.findById(recordInfo.getMemberId())
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 도전 정보 조회 (없을 수 있음)
        Attempt attempt = null;
        if (recordInfo.getAttemptId() != null) {
            attempt = attemptRepository.findById(recordInfo.getAttemptId())
                    .orElseThrow(() -> new ExceptionHandler(ErrorStatus.ATTEMPT_NOT_FOUND));
        }

        // 오디오 파일 업로드
        String recordUrl = audioFileService.uploadAudioFile(
                audioFile,
                recordInfo.getDtype().getValue(),
                recordInfo.getMemberId().toString()
        );

        // 레코드 엔티티 생성
        ChallengeType challengeType = ChallengeType.valueOf(recordInfo.getDtype().name());
        Record record = Record.createRecord(
                member,
                attempt,
                challengeType,
                recordInfo.getTitle(),
                recordUrl
        );

        // 저장 및 변환
        Record savedRecord = audioRecordRepository.save(record);
        return recordConverter.toDto(savedRecord);
    }

    /**
     * 특정 ID의 녹음을 조회합니다.
     *
     * @param recordId 녹음 ID
     * @return 녹음 정보
     */
    @Transactional(readOnly = true)
    public RecordDTO getRecord(Long recordId) {
        Record record = audioRecordRepository.findById(recordId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.RECORD_NOT_FOUND));
        return recordConverter.toDto(record);
    }

    /**
     * 특정 도전에 대한 녹음을 조회합니다.
     *
     * @param attemptId 도전 ID
     * @return 녹음 정보
     */
    @Transactional(readOnly = true)
    public RecordDTO getAttemptRecord(Long attemptId) {
        Record record = audioRecordRepository.findByAttemptRecordId(attemptId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.RECORD_NOT_FOUND));
        return recordConverter.toDto(record);
    }

    /**
     * 녹음 목록을 조회합니다. 필터링 옵션을 제공합니다.
     *
     * @param memberId 멤버 ID (선택적)
     * @param dtype    녹음 유형 (선택적)
     * @param pageable 페이징 정보
     * @return 녹음 목록
     */
    @Transactional(readOnly = true)
    public Page<RecordDTO> getRecords(UUID memberId, String dtype, Pageable pageable) {
        ChallengeType challengeType = dtype != null ? ChallengeType.valueOf(dtype) : null;

        Page<Record> recordPage;
        if (memberId != null && challengeType != null) {
            recordPage = audioRecordRepository.findByMember_MemberIdAndDtype(memberId, challengeType, pageable);
        } else if (memberId != null) {
            recordPage = audioRecordRepository.findByMember_MemberId(memberId, pageable);
        } else if (challengeType != null) {
            recordPage = audioRecordRepository.findByDtype(challengeType, pageable);
        } else {
            recordPage = audioRecordRepository.findAll(pageable);
        }

        return recordPage.map(recordConverter::toDto);
    }

    /**
     * 특정 회원의 녹음 목록을 조회합니다.
     *
     * @param memberId 멤버 ID
     * @param dtype    녹음 유형 (선택적)
     * @param pageable 페이징 정보
     * @return 멤버별 녹음 목록
     */
    @Transactional(readOnly = true)
    public Page<RecordDTO> getMemberRecords(UUID memberId, String dtype, Pageable pageable) {
        // 멤버 존재 여부 확인
        if (!memberRepository.existsById(memberId)) {
            throw new ExceptionHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        Page<Record> recordPage;
        if (dtype != null) {
            ChallengeType challengeType = ChallengeType.valueOf(dtype);
            recordPage = audioRecordRepository.findByMember_MemberIdAndDtype(memberId, challengeType, pageable);
        } else {
            recordPage = audioRecordRepository.findByMember_MemberId(memberId, pageable);
        }

        return recordPage.map(recordConverter::toDto);
    }
}