-- 트랜잭션 시작
START TRANSACTION;

-- 1. song 테이블 더미 데이터
INSERT INTO `song` (`created_at`, `artist`, `description`, `title`, `youtube_url`)
VALUES ('2025-04-01 12:00:00', '넬(NELL)', '감성적인 록 발라드', '기억을 걷는 시간', 'https://www.youtube.com/watch?v=K72ZxP9ZAP4'),
       ('2025-04-01 12:10:00', '혁오(HYUKOH)', '인디 록 장르', '위잉위잉', 'https://www.youtube.com/watch?v=ql_OZX0yR1s'),
       ('2025-04-01 12:20:00', '버즈(BUZZ)', '록 발라드 명곡', '가시', 'https://www.youtube.com/watch?v=W0cs6ciCt_k'),
       ('2025-04-01 12:30:00', '데이식스(DAY6)', '밴드 록 장르', 'Zombie', 'https://www.youtube.com/watch?v=k8gx-C7GCGU'),
       ('2025-04-01 12:40:00', '악동뮤지션', '어쿠스틱 팝', '오랜 날 오랜 밤', 'https://www.youtube.com/watch?v=wEQpfil0IYA'),
       ('2025-04-01 12:50:00', '자우림', '록 발라드', '스물다섯, 스물하나', 'https://www.youtube.com/watch?v=LrB-fJn-3w4'),
       ('2025-04-01 13:00:00', '소방차', '클래식 록', '어젯밤이야기', 'https://www.youtube.com/watch?v=q17IYOZ6rxk'),
       ('2025-04-01 13:10:00', 'Queen', '록 클래식', 'Bohemian Rhapsody', 'https://www.youtube.com/watch?v=fJ9rUzIMcZQ'),
       ('2025-04-01 13:20:00', '아이유(IU)', '팝 발라드', '밤편지', 'https://www.youtube.com/watch?v=BzYnNdJhZQw'),
       ('2025-04-01 13:30:00', '이무진', '감성 발라드', '신호등', 'https://www.youtube.com/watch?v=SK6Sm2Ki9tI'),
       ('2025-04-01 13:40:00', '블루베리노트', '재즈 팝', '별이 빛나는 밤에', 'https://www.youtube.com/watch?v=5WQVg7s7HmA'),
       ('2025-04-01 13:50:00', '김동률', '팝 발라드', '아주 사적인 노래', 'https://www.youtube.com/watch?v=mk5fDV6cGQo'),
       ('2025-04-01 14:00:00', '잔나비', '인디 록', '주저하는 연인들을 위해', 'https://www.youtube.com/watch?v=5g4KsIizYhQ'),
       ('2025-04-01 14:10:00', '이센스(E SENS)', '힙합', 'The Anecdote', 'https://www.youtube.com/watch?v=sQ1xm30Eirw'),
       ('2025-04-01 14:20:00', '세븐틴(SEVENTEEN)', '밴드 사운드', 'Rock with you',
        'https://www.youtube.com/watch?v=WpuatuzSDK4');

-- 2. song_instrument_pack 테이블 더미 데이터
INSERT INTO `song_instrument_pack` (`created_at`, `pack_name`, `song_pack_instrument`, `song_pack_tier`)
VALUES
-- 기타 팩
('2025-04-01 15:00:00', '기타 입문자 팩', 'GUITAR', 'IRON'),
('2025-04-01 15:10:00', '기타 초보자 팩', 'GUITAR', 'BRONZE'),
('2025-04-01 15:20:00', '기타 중급자 팩', 'GUITAR', 'SILVER'),
('2025-04-01 15:30:00', '기타 상급자 팩', 'GUITAR', 'GOLD'),
('2025-04-01 15:40:00', '기타 프로 팩', 'GUITAR', 'PLATINUM'),
('2025-04-01 15:50:00', '기타 마스터 팩', 'GUITAR', 'DIAMOND'),

-- 베이스 팩
('2025-04-01 16:00:00', '베이스 입문자 팩', 'BASS', 'IRON'),
('2025-04-01 16:10:00', '베이스 초보자 팩', 'BASS', 'BRONZE'),
('2025-04-01 16:20:00', '베이스 중급자 팩', 'BASS', 'SILVER'),
('2025-04-01 16:30:00', '베이스 상급자 팩', 'BASS', 'GOLD'),
('2025-04-01 16:40:00', '베이스 프로 팩', 'BASS', 'PLATINUM'),
('2025-04-01 16:50:00', '베이스 마스터 팩', 'BASS', 'DIAMOND'),

-- 드럼 팩
('2025-04-01 17:00:00', '드럼 입문자 팩', 'DRUM', 'IRON'),
('2025-04-01 17:10:00', '드럼 초보자 팩', 'DRUM', 'BRONZE'),
('2025-04-01 17:20:00', '드럼 중급자 팩', 'DRUM', 'SILVER'),
('2025-04-01 17:30:00', '드럼 상급자 팩', 'DRUM', 'GOLD'),
('2025-04-01 17:40:00', '드럼 프로 팩', 'DRUM', 'PLATINUM'),
('2025-04-01 17:50:00', '드럼 마스터 팩', 'DRUM', 'DIAMOND'),

-- 보컬 팩
('2025-04-01 18:00:00', '보컬 입문자 팩', 'VOCAL', 'IRON'),
('2025-04-01 18:10:00', '보컬 초보자 팩', 'VOCAL', 'BRONZE'),
('2025-04-01 18:20:00', '보컬 중급자 팩', 'VOCAL', 'SILVER'),
('2025-04-01 18:30:00', '보컬 상급자 팩', 'VOCAL', 'GOLD'),
('2025-04-01 18:40:00', '보컬 프로 팩', 'VOCAL', 'PLATINUM'),
('2025-04-01 18:50:00', '보컬 마스터 팩', 'VOCAL', 'DIAMOND');

-- 3. song_by_instrument 테이블 더미 데이터
-- 비즈니스 로직: 각 팩마다 5개의 song_by_instrument 생성

-- 기타 IRON 팩 (1번) - 5개 곡
INSERT INTO `song_by_instrument` (`created_at`, `song_id`, `song_instrument_pack_id`, `instrument_url`,
                                  `song_by_instrument_filename`, `instrument`, `tier`)
VALUES ('2025-04-01 20:00:00', 1, 1, 'https://storage.dinggading.com/instruments/guitar_iron_nell_memory.mp3',
        'guitar_iron_nell_memory.mp3', 'GUITAR', 'IRON'),
       ('2025-04-01 20:01:00', 5, 1, 'https://storage.dinggading.com/instruments/guitar_iron_akmu_longtime.mp3',
        'guitar_iron_akmu_longtime.mp3', 'GUITAR', 'IRON'),
       ('2025-04-01 20:02:00', 9, 1, 'https://storage.dinggading.com/instruments/guitar_iron_iu_letter.mp3',
        'guitar_iron_iu_letter.mp3', 'GUITAR', 'IRON'),
       ('2025-04-01 20:03:00', 10, 1, 'https://storage.dinggading.com/instruments/guitar_iron_leemujin_traffic.mp3',
        'guitar_iron_leemujin_traffic.mp3', 'GUITAR', 'IRON'),
       ('2025-04-01 20:04:00', 12, 1, 'https://storage.dinggading.com/instruments/guitar_iron_dongryul_private.mp3',
        'guitar_iron_dongryul_private.mp3', 'GUITAR', 'IRON');

-- 기타 BRONZE 팩 (2번) - 5개 곡
INSERT INTO `song_by_instrument` (`created_at`, `song_id`, `song_instrument_pack_id`, `instrument_url`,
                                  `song_by_instrument_filename`, `instrument`, `tier`)
VALUES ('2025-04-01 20:10:00', 2, 2, 'https://storage.dinggading.com/instruments/guitar_bronze_hyukoh_wiing.mp3',
        'guitar_bronze_hyukoh_wiing.mp3', 'GUITAR', 'BRONZE'),
       ('2025-04-01 20:11:00', 3, 2, 'https://storage.dinggading.com/instruments/guitar_bronze_buzz_thorn.mp3',
        'guitar_bronze_buzz_thorn.mp3', 'GUITAR', 'BRONZE'),
       ('2025-04-01 20:12:00', 4, 2, 'https://storage.dinggading.com/instruments/guitar_bronze_day6_zombie.mp3',
        'guitar_bronze_day6_zombie.mp3', 'GUITAR', 'BRONZE'),
       ('2025-04-01 20:13:00', 6, 2, 'https://storage.dinggading.com/instruments/guitar_bronze_jaurim_twentyfive.mp3',
        'guitar_bronze_jaurim_twentyfive.mp3', 'GUITAR', 'BRONZE'),
       ('2025-04-01 20:14:00', 13, 2, 'https://storage.dinggading.com/instruments/guitar_bronze_jannabi_hesitant.mp3',
        'guitar_bronze_jannabi_hesitant.mp3', 'GUITAR', 'BRONZE');

-- 베이스 IRON 팩 (7번) - 5개 곡
INSERT INTO `song_by_instrument` (`created_at`, `song_id`, `song_instrument_pack_id`, `instrument_url`,
                                  `song_by_instrument_filename`, `instrument`, `tier`)
VALUES ('2025-04-01 20:20:00', 1, 7, 'https://storage.dinggading.com/instruments/bass_iron_nell_memory.mp3',
        'bass_iron_nell_memory.mp3', 'BASS', 'IRON'),
       ('2025-04-01 20:21:00', 5, 7, 'https://storage.dinggading.com/instruments/bass_iron_akmu_longtime.mp3',
        'bass_iron_akmu_longtime.mp3', 'BASS', 'IRON'),
       ('2025-04-01 20:22:00', 9, 7, 'https://storage.dinggading.com/instruments/bass_iron_iu_letter.mp3',
        'bass_iron_iu_letter.mp3', 'BASS', 'IRON'),
       ('2025-04-01 20:23:00', 10, 7, 'https://storage.dinggading.com/instruments/bass_iron_leemujin_traffic.mp3',
        'bass_iron_leemujin_traffic.mp3', 'BASS', 'IRON'),
       ('2025-04-01 20:24:00', 12, 7, 'https://storage.dinggading.com/instruments/bass_iron_dongryul_private.mp3',
        'bass_iron_dongryul_private.mp3', 'BASS', 'IRON');

-- 드럼 IRON 팩 (13번) - 5개 곡
INSERT INTO `song_by_instrument` (`created_at`, `song_id`, `song_instrument_pack_id`, `instrument_url`,
                                  `song_by_instrument_filename`, `instrument`, `tier`)
VALUES ('2025-04-01 20:30:00', 1, 13, 'https://storage.dinggading.com/instruments/drum_iron_nell_memory.mp3',
        'drum_iron_nell_memory.mp3', 'DRUM', 'IRON'),
       ('2025-04-01 20:31:00', 5, 13, 'https://storage.dinggading.com/instruments/drum_iron_akmu_longtime.mp3',
        'drum_iron_akmu_longtime.mp3', 'DRUM', 'IRON'),
       ('2025-04-01 20:32:00', 9, 13, 'https://storage.dinggading.com/instruments/drum_iron_iu_letter.mp3',
        'drum_iron_iu_letter.mp3', 'DRUM', 'IRON'),
       ('2025-04-01 20:33:00', 10, 13, 'https://storage.dinggading.com/instruments/drum_iron_leemujin_traffic.mp3',
        'drum_iron_leemujin_traffic.mp3', 'DRUM', 'IRON'),
       ('2025-04-01 20:34:00', 12, 13, 'https://storage.dinggading.com/instruments/drum_iron_dongryul_private.mp3',
        'drum_iron_dongryul_private.mp3', 'DRUM', 'IRON');

-- 보컬 IRON 팩 (19번) - 5개 곡
INSERT INTO `song_by_instrument` (`created_at`, `song_id`, `song_instrument_pack_id`, `instrument_url`,
                                  `song_by_instrument_filename`, `instrument`, `tier`)
VALUES ('2025-04-01 20:40:00', 1, 19, 'https://storage.dinggading.com/instruments/vocal_iron_nell_memory.mp3',
        'vocal_iron_nell_memory.mp3', 'VOCAL', 'IRON'),
       ('2025-04-01 20:41:00', 5, 19, 'https://storage.dinggading.com/instruments/vocal_iron_akmu_longtime.mp3',
        'vocal_iron_akmu_longtime.mp3', 'VOCAL', 'IRON'),
       ('2025-04-01 20:42:00', 9, 19, 'https://storage.dinggading.com/instruments/vocal_iron_iu_letter.mp3',
        'vocal_iron_iu_letter.mp3', 'VOCAL', 'IRON'),
       ('2025-04-01 20:43:00', 10, 19, 'https://storage.dinggading.com/instruments/vocal_iron_leemujin_traffic.mp3',
        'vocal_iron_leemujin_traffic.mp3', 'VOCAL', 'IRON'),
       ('2025-04-01 20:44:00', 12, 19, 'https://storage.dinggading.com/instruments/vocal_iron_dongryul_private.mp3',
        'vocal_iron_dongryul_private.mp3', 'VOCAL', 'IRON');

-- 보컬 SILVER 팩 (21번) - 5개 곡
INSERT INTO `song_by_instrument` (`created_at`, `song_id`, `song_instrument_pack_id`, `instrument_url`,
                                  `song_by_instrument_filename`, `instrument`, `tier`)
VALUES ('2025-04-01 20:50:00', 3, 21, 'https://storage.dinggading.com/instruments/vocal_silver_buzz_thorn.mp3',
        'vocal_silver_buzz_thorn.mp3', 'VOCAL', 'SILVER'),
       ('2025-04-01 20:51:00', 4, 21, 'https://storage.dinggading.com/instruments/vocal_silver_day6_zombie.mp3',
        'vocal_silver_day6_zombie.mp3', 'VOCAL', 'SILVER'),
       ('2025-04-01 20:52:00', 6, 21, 'https://storage.dinggading.com/instruments/vocal_silver_jaurim_twentyfive.mp3',
        'vocal_silver_jaurim_twentyfive.mp3', 'VOCAL', 'SILVER'),
       ('2025-04-01 20:53:00', 8, 21, 'https://storage.dinggading.com/instruments/vocal_silver_queen_bohemian.mp3',
        'vocal_silver_queen_bohemian.mp3', 'VOCAL', 'SILVER'),
       ('2025-04-01 20:54:00', 13, 21, 'https://storage.dinggading.com/instruments/vocal_silver_jannabi_hesitant.mp3',
        'vocal_silver_jannabi_hesitant.mp3', 'VOCAL', 'SILVER');

-- 기타 PLATINUM 팩 (5번) - 5개 곡
INSERT INTO `song_by_instrument` (`created_at`, `song_id`, `song_instrument_pack_id`, `instrument_url`,
                                  `song_by_instrument_filename`, `instrument`, `tier`)
VALUES ('2025-04-01 21:00:00', 3, 5, 'https://storage.dinggading.com/instruments/guitar_platinum_buzz_thorn.mp3',
        'guitar_platinum_buzz_thorn.mp3', 'GUITAR', 'PLATINUM'),
       ('2025-04-01 21:01:00', 4, 5, 'https://storage.dinggading.com/instruments/guitar_platinum_day6_zombie.mp3',
        'guitar_platinum_day6_zombie.mp3', 'GUITAR', 'PLATINUM'),
       ('2025-04-01 21:02:00', 7, 5,
        'https://storage.dinggading.com/instruments/guitar_platinum_sobangcha_yesterday.mp3',
        'guitar_platinum_sobangcha_yesterday.mp3', 'GUITAR', 'PLATINUM'),
       ('2025-04-01 21:03:00', 8, 5, 'https://storage.dinggading.com/instruments/guitar_platinum_queen_bohemian.mp3',
        'guitar_platinum_queen_bohemian.mp3', 'GUITAR', 'PLATINUM'),
       ('2025-04-01 21:04:00', 15, 5, 'https://storage.dinggading.com/instruments/guitar_platinum_seventeen_rock.mp3',
        'guitar_platinum_seventeen_rock.mp3', 'GUITAR', 'PLATINUM');

-- 트랜잭션 완료
COMMIT;