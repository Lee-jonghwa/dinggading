SET SQL_SAFE_UPDATES = 0;

-- 트랜잭션 시작
BEGIN;

-- 기존 멤버 ID 조회
SET @member1_id = (SELECT member_id
                   FROM member
                   WHERE username = 'guitar_master@gmail.com');
SET @member2_id = (SELECT member_id
                   FROM member
                   WHERE username = 'drum_king@gmail.com');
SET @member3_id = (SELECT member_id
                   FROM member
                   WHERE username = 'bass_pro@gmail.com');
SET @member4_id = (SELECT member_id
                   FROM member
                   WHERE username = 'vocal_queen@gmail.com');
SET @member5_id = (SELECT member_id
                   FROM member
                   WHERE username = 'melody_singer@gmail.com');
SET @member6_id = (SELECT member_id
                   FROM member
                   WHERE username = 'rock_star@gmail.com');
SET @member7_id = (SELECT member_id
                   FROM member
                   WHERE username = 'music_captain@gmail.com');
SET @member8_id = (SELECT member_id
                   FROM member
                   WHERE username = 'jazz_man@gmail.com');
SET @member9_id = (SELECT member_id
                   FROM member
                   WHERE username = 'pop_star@gmail.com');
SET @member10_id = (SELECT member_id
                    FROM member
                    WHERE username = 'music_genius@gmail.com');
SET @member11_id = (SELECT member_id
                    FROM member
                    WHERE username = 'stage_queen@gmail.com');
SET @member12_id = (SELECT member_id
                    FROM member
                    WHERE username = 'rhythm_king@gmail.com');

-- 밴드 ID 조회
SET @band1_id = (SELECT band_id
                 FROM band
                 WHERE name = '소리꾼들');
SET @band2_id = (SELECT band_id
                 FROM band
                 WHERE name = '블루노트');
SET @band3_id = (SELECT band_id
                 FROM band
                 WHERE name = '메탈헤드');
SET @band4_id = (SELECT band_id
                 FROM band
                 WHERE name = '어쿠스틱 소울');
SET @band5_id = (SELECT band_id
                 FROM band
                 WHERE name = '국악 프로젝트');
SET @band6_id = (SELECT band_id
                 FROM band
                 WHERE name = '클래식 팝스');

-- band_member 더미 데이터 생성
INSERT INTO `band_member` (`band_id`, `created_at`, `updated_at`, `member_id`, `instrument`)
VALUES
    -- 소리꾼들 멤버
    (@band1_id, '2025-04-01 10:05:00', '2025-04-01 10:05:00', @member1_id, 'GUITAR'), -- 기타킹은 소리꾼들의 밴드마스터이자 기타리스트
    (@band1_id, '2025-04-01 10:08:00', '2025-04-01 10:08:00', @member2_id, 'DRUM'),   -- 드럼왕은 소리꾼들의 드러머

    -- 블루노트 멤버
    (@band2_id, '2025-04-01 10:15:00', '2025-04-01 10:15:00', @member2_id, 'DRUM'),   -- 드럼왕은 블루노트의 밴드마스터이자 드러머
    (@band2_id, '2025-04-01 10:18:00', '2025-04-01 10:18:00', @member8_id, 'VOCAL'),  -- 재즈맨은 블루노트의 보컬

    -- 메탈헤드 멤버
    (@band3_id, '2025-04-01 10:25:00', '2025-04-01 10:25:00', @member3_id, 'BASS'),   -- 베이싱은 메탈헤드의 밴드마스터이자 베이시스트
    (@band3_id, '2025-04-01 10:28:00', '2025-04-01 10:28:00', @member6_id, 'GUITAR'), -- 록스타는 메탈헤드의 기타리스트
    (@band3_id, '2025-04-01 10:31:00', '2025-04-01 10:31:00', @member9_id, 'VOCAL'),  -- 팝스타는 메탈헤드의 보컬

    -- 어쿠스틱 소울 멤버
    (@band4_id, '2025-04-01 10:35:00', '2025-04-01 10:35:00', @member4_id, 'VOCAL'),  -- 보컬여신은 어쿠스틱 소울의 밴드마스터이자 보컬
    (@band4_id, '2025-04-01 10:38:00', '2025-04-01 10:38:00', @member7_id, 'BASS'),   -- 음악대장은 어쿠스틱 소울의 베이시스트

    -- 국악 프로젝트 멤버
    (@band5_id, '2025-04-01 10:45:00', '2025-04-01 10:45:00', @member5_id, 'VOCAL');
-- 멜로디는 국악 프로젝트의 밴드마스터이자 보컬

-- band_recruitment_instrument 더미 데이터 생성 (기존 band_recruitment에 필요한 악기 추가)
-- 먼저 이전 데이터가 있을 경우 삭제 (옵션)
DELETE
FROM band_recruitment_instrument
WHERE band_recruitment_id IN (1, 2, 3, 4, 5, 6);

INSERT INTO `band_recruitment_instrument` (`max_size`, `band_recruitment_id`, `created_at`, `updated_at`, `instrument`,
                                           `required_tier`)
VALUES
    -- 소리꾼들 기타리스트 모집 (recruitment_id = 1)
    (1, 1, '2025-04-02 11:05:00', '2025-04-02 11:05:00', 'GUITAR', 'GOLD'),

    -- 소리꾼들 베이시스트 모집 (recruitment_id = 2)
    (1, 2, '2025-04-02 11:35:00', '2025-04-02 11:35:00', 'BASS', 'SILVER'),

    -- 블루노트 보컬 모집 (recruitment_id = 3)
    (1, 3, '2025-04-02 12:05:00', '2025-04-02 12:05:00', 'VOCAL', 'PLATINUM'),

    -- 메탈헤드 드러머 모집 (recruitment_id = 4)
    (1, 4, '2025-04-02 12:35:00', '2025-04-02 12:35:00', 'DRUM', 'GOLD'),

    -- 어쿠스틱 소울 멤버 모집 (recruitment_id = 5) - 기타와 보컬 모두 모집
    (1, 5, '2025-04-02 13:05:00', '2025-04-02 13:05:00', 'GUITAR', 'SILVER'),
    (1, 5, '2025-04-02 13:06:00', '2025-04-02 13:06:00', 'VOCAL', 'GOLD'),

    -- 국악 프로젝트 단원 모집 (recruitment_id = 6)
    (2, 6, '2025-04-02 13:35:00', '2025-04-02 13:35:00', 'GUITAR', 'BRONZE'),
    (1, 6, '2025-04-02 13:36:00', '2025-04-02 13:36:00', 'DRUM', 'SILVER');

-- contact 더미 데이터 생성 (각 밴드의 SNS 연락처)
INSERT INTO `contact` (`band_id`, `created_at`, `updated_at`, `title`, `url`, `sns`)
VALUES
    -- 소리꾼들의 연락처
    (@band1_id, '2025-04-01 10:05:00', '2025-04-01 10:05:00', '소리꾼들 인스타그램', 'https://instagram.com/sorikkun',
     'INSTAGRAM'),
    (@band1_id, '2025-04-01 10:06:00', '2025-04-01 10:06:00', '소리꾼들 유튜브', 'https://youtube.com/@sorikkun', 'YOUTUBE'),

    -- 블루노트의 연락처
    (@band2_id, '2025-04-01 10:15:00', '2025-04-01 10:15:00', '블루노트 인스타그램', 'https://instagram.com/bluenote',
     'INSTAGRAM'),
    (@band2_id, '2025-04-01 10:16:00', '2025-04-01 10:16:00', '블루노트 X(트위터)', 'https://x.com/bluenote', 'X'),

    -- 메탈헤드의 연락처
    (@band3_id, '2025-04-01 10:25:00', '2025-04-01 10:25:00', '메탈헤드 인스타그램', 'https://instagram.com/metalhead',
     'INSTAGRAM'),
    (@band3_id, '2025-04-01 10:26:00', '2025-04-01 10:26:00', '메탈헤드 유튜브', 'https://youtube.com/@metalhead', 'YOUTUBE'),
    (@band3_id, '2025-04-01 10:27:00', '2025-04-01 10:27:00', '메탈헤드 웹사이트', 'https://metalhead.kr', 'CUSTOM'),

    -- 어쿠스틱 소울의 연락처
    (@band4_id, '2025-04-01 10:35:00', '2025-04-01 10:35:00', '어쿠스틱 소울 인스타그램', 'https://instagram.com/acousticsoul',
     'INSTAGRAM'),

    -- 국악 프로젝트의 연락처
    (@band5_id, '2025-04-01 10:45:00', '2025-04-01 10:45:00', '국악 프로젝트 유튜브', 'https://youtube.com/@kpproject',
     'YOUTUBE'),
    (@band5_id, '2025-04-01 10:46:00', '2025-04-01 10:46:00', '국악 프로젝트 페이스북', 'https://facebook.com/kpproject',
     'FACEBOOK');

-- 트랜잭션 커밋
COMMIT;