-- 트랜잭션 시작
START TRANSACTION;

-- 1. song_instrument_pack 더미 데이터 생성
-- 티어별, 악기별 팩 생성 (기존과 동일)
INSERT INTO song_instrument_pack (created_at, updated_at, pack_name, song_pack_instrument, song_pack_tier)
VALUES
-- VOCAL 팩
(NOW(), NOW(), 'VOCAL 입문자 팩', 'VOCAL', 'IRON'),
(NOW(), NOW(), 'VOCAL 초보자 팩', 'VOCAL', 'BRONZE'),
(NOW(), NOW(), 'VOCAL 중급자 팩', 'VOCAL', 'SILVER'),
(NOW(), NOW(), 'VOCAL 상급자 팩', 'VOCAL', 'GOLD'),
(NOW(), NOW(), 'VOCAL 전문가 팩', 'VOCAL', 'PLATINUM'),
(NOW(), NOW(), 'VOCAL 마스터 팩', 'VOCAL', 'DIAMOND'),

-- GUITAR 팩
(NOW(), NOW(), 'GUITAR 입문자 팩', 'GUITAR', 'IRON'),
(NOW(), NOW(), 'GUITAR 초보자 팩', 'GUITAR', 'BRONZE'),
(NOW(), NOW(), 'GUITAR 중급자 팩', 'GUITAR', 'SILVER'),
(NOW(), NOW(), 'GUITAR 상급자 팩', 'GUITAR', 'GOLD'),
(NOW(), NOW(), 'GUITAR 전문가 팩', 'GUITAR', 'PLATINUM'),
(NOW(), NOW(), 'GUITAR 마스터 팩', 'GUITAR', 'DIAMOND'),

-- BASS 팩
(NOW(), NOW(), 'BASS 입문자 팩', 'BASS', 'IRON'),
(NOW(), NOW(), 'BASS 초보자 팩', 'BASS', 'BRONZE'),
(NOW(), NOW(), 'BASS 중급자 팩', 'BASS', 'SILVER'),
(NOW(), NOW(), 'BASS 상급자 팩', 'BASS', 'GOLD'),
(NOW(), NOW(), 'BASS 전문가 팩', 'BASS', 'PLATINUM'),
(NOW(), NOW(), 'BASS 마스터 팩', 'BASS', 'DIAMOND'),

-- DRUM 팩
(NOW(), NOW(), 'DRUM 입문자 팩', 'DRUM', 'IRON'),
(NOW(), NOW(), 'DRUM 초보자 팩', 'DRUM', 'BRONZE'),
(NOW(), NOW(), 'DRUM 중급자 팩', 'DRUM', 'SILVER'),
(NOW(), NOW(), 'DRUM 상급자 팩', 'DRUM', 'GOLD'),
(NOW(), NOW(), 'DRUM 전문가 팩', 'DRUM', 'PLATINUM'),
(NOW(), NOW(), 'DRUM 마스터 팩', 'DRUM', 'DIAMOND');

-- 2. song 더미 데이터 생성 (총 30개 노래 - 각 티어와 악기 조합에 대해 5개씩 제공하기 위함)
INSERT INTO song (created_at, updated_at, artist, description, song_filename, title, youtube_url)
VALUES
-- 입문자(IRON) 난이도 곡들
(NOW(), NOW(), '버즈', '대표적인 록밴드 버즈의 명곡', 'buzz_monologue.mp3', '가시', 'https://www.youtube.com/watch?v=W0cs6ciCt_k'),
(NOW(), NOW(), '신해철', '한국 록의 전설', 'shin_flower.mp3', '민들레홀씨', 'https://www.youtube.com/watch?v=IHU9hbvWmMU'),
(NOW(), NOW(), '동물원', '대표적인 포크송', 'zoo_will_be_back.mp3', '혜화동', 'https://www.youtube.com/watch?v=e7Lf6HUjdYc'),
(NOW(), NOW(), '김광석', '포크송의 거장', 'kim_around_30.mp3', '서른 즈음에', 'https://www.youtube.com/watch?v=TdipS0L0hIE'),
(NOW(), NOW(), '015B', '90년대 대표곡', '015b_flying.mp3', '텔미텔미', 'https://www.youtube.com/watch?v=GG7ZWMyxBI4'),

-- 초보자(BRONZE) 난이도 곡들
(NOW(), NOW(), '혁오', '인디 밴드의 대표곡', 'hyukoh_comes_and_goes.mp3', '와리가리', 'https://www.youtube.com/watch?v=ECMc1SB60E0'),
(NOW(), NOW(), '이적', '가수 이적의 대표곡', 'lee_doll.mp3', '인형', 'https://www.youtube.com/watch?v=87wOFQGYZj0'),
(NOW(), NOW(), '장범준', '버스커버스커 출신', 'jang_hard_to_say.mp3', '노래 (말하는 대로)',
 'https://www.youtube.com/watch?v=ZtcM3KhgF-s'),
(NOW(), NOW(), '윤종신', '가수 윤종신의 인기곡', 'yoon_like.mp3', '좋니', 'https://www.youtube.com/watch?v=jy_UiIQn_d0'),
(NOW(), NOW(), '폴킴', '발라드 싱어', 'paul_rain.mp3', '비', 'https://www.youtube.com/watch?v=QhXm3nR9lGQ'),

-- 중급자(SILVER) 난이도 곡들
(NOW(), NOW(), '자우림', '자우림의 대표곡', 'jaurim_hey_hey_hey.mp3', '스물다섯, 스물하나',
 'https://www.youtube.com/watch?v=_jzMDsYHktI'),
(NOW(), NOW(), '델리스파이스', '대표적인 얼터너티브 록', 'delispice_chau_chau.mp3', '챠우챠우',
 'https://www.youtube.com/watch?v=1KMMp1wFHJ8'),
(NOW(), NOW(), '이승환', '록의 대부', 'lee_never_ending.mp3', '너에게', 'https://www.youtube.com/watch?v=sqgxcCjD04s'),
(NOW(), NOW(), '김동률', '싱어송라이터', 'kim_youth.mp3', '청춘', 'https://www.youtube.com/watch?v=jn36PQlX8kk'),
(NOW(), NOW(), '에픽하이', '힙합그룹', 'epik_love_love.mp3', '러브러브러브', 'https://www.youtube.com/watch?v=1HPww0Wa06Q'),

-- 상급자(GOLD) 난이도 곡들
(NOW(), NOW(), '넬', '대표적인 모던 록', 'nell_time_walking_through.mp3', '타임워킹 쓰루 더 메모리즈',
 'https://www.youtube.com/watch?v=K72ZxP9ZAP4'),
(NOW(), NOW(), '데이식스', '밴드 데이식스의 인기곡', 'day6_you_were_beautiful.mp3', '예뻤어',
 'https://www.youtube.com/watch?v=BS7tz2rAOSA'),
(NOW(), NOW(), '국카스텐', '하드록 밴드', 'guckkasten_red.mp3', '변할까봐', 'https://www.youtube.com/watch?v=WvrPrIGVNZY'),
(NOW(), NOW(), '노브레인', '펑크 록밴드', 'nobrain_youth.mp3', '넌 내게 반했어', 'https://www.youtube.com/watch?v=vT7Odd9Avo8'),
(NOW(), NOW(), '이문세', '발라드의 거장', 'moon_flight.mp3', '붉은노을', 'https://www.youtube.com/watch?v=bEgr3dJ7zf4'),

-- 전문가(PLATINUM) 난이도 곡들
(NOW(), NOW(), '아이유', '싱어송라이터 아이유의 곡', 'iu_through_the_night.mp3', '밤편지',
 'https://www.youtube.com/watch?v=BzYnNdJhZQw'),
(NOW(), NOW(), '정준일', '실력파 싱어송라이터', 'jung_hug.mp3', '안아줘', 'https://www.youtube.com/watch?v=OQC1z2EBt7U'),
(NOW(), NOW(), '검정치마', '인디 록밴드', 'blackskirt_everything.mp3', '모든 것이 노래',
 'https://www.youtube.com/watch?v=FMqhQwJ4eQU'),
(NOW(), NOW(), '소란', '인디 밴드', 'soran_day.mp3', '오늘', 'https://www.youtube.com/watch?v=PJXDFzBRjyQ'),
(NOW(), NOW(), '마마무', '실력파 걸그룹', 'mamamoo_egotistic.mp3', '너나 해', 'https://www.youtube.com/watch?v=SZ4CnszOAhA'),

-- 마스터(DIAMOND) 난이도 곡들
(NOW(), NOW(), '퀸', '전설적인 록밴드의 명곡', 'queen_bohemian_rhapsody.mp3', '보헤미안 랩소디',
 'https://www.youtube.com/watch?v=fJ9rUzIMcZQ'),
(NOW(), NOW(), '레드핫칠리페퍼스', '대표적인 펑크 록', 'rhcp_californication.mp3', '캘리포니케이션',
 'https://www.youtube.com/watch?v=YlUKcNNmywk'),
(NOW(), NOW(), '메탈리카', '헤비메탈의 대표곡', 'metallica_enter_sandman.mp3', 'Enter Sandman',
 'https://www.youtube.com/watch?v=CD-E-LDc384'),
(NOW(), NOW(), '머라이어 캐리', '팝의 여제', 'mariah_all.mp3', 'All I Want For Christmas Is You',
 'https://www.youtube.com/watch?v=yXQViqx6GMY'),
(NOW(), NOW(), '마이클 잭슨', '팝의 제왕', 'mj_thriller.mp3', 'Thriller', 'https://www.youtube.com/watch?v=sOnqjkJTMaA');

-- 3. song_by_instrument 더미 데이터 생성
-- 이제는 각 팩별로 5개씩의 노래를 연결해줍니다
-- IRON 티어 VOCAL 팩 (1번 팩)
INSERT INTO song_by_instrument (created_at, updated_at, song_id, song_instrument_pack_id,
                                song_by_instrument_ex_filename, song_by_instrument_filename,
                                song_by_instrument_analysis_json, instrument, tier)
VALUES (NOW(), NOW(), 1, 1, 'buzz_monologue_ex_vocal.mp3', 'buzz_monologue_vocal.mp3',
        'buzz_monologue_vocal_analysis.json', 'VOCAL', 'IRON'),
       (NOW(), NOW(), 2, 1, 'shin_flower_ex_vocal.mp3', 'shin_flower_vocal.mp3',
        'shin_flower_vocal_analysis.json', 'VOCAL', 'IRON'),
       (NOW(), NOW(), 3, 1, 'zoo_will_be_back_ex_vocal.mp3', 'zoo_will_be_back_vocal.mp3',
        'zoo_will_be_back_vocal_analysis.json', 'VOCAL', 'IRON'),
       (NOW(), NOW(), 4, 1, 'kim_around_30_ex_vocal.mp3', 'kim_around_30_vocal.mp3',
        'kim_around_30_vocal_analysis.json', 'VOCAL', 'IRON'),
       (NOW(), NOW(), 5, 1, '015b_flying_ex_vocal.mp3', '015b_flying_vocal.mp3',
        '015b_flying_vocal_analysis.json', 'VOCAL', 'IRON');

-- IRON 티어 GUITAR 팩 (7번 팩)
INSERT INTO song_by_instrument (created_at, updated_at, song_id, song_instrument_pack_id,
                                song_by_instrument_ex_filename, song_by_instrument_filename,
                                song_by_instrument_analysis_json, instrument, tier)
VALUES (NOW(), NOW(), 1, 7, 'buzz_monologue_ex_guitar.mp3', 'buzz_monologue_guitar.mp3',
        'buzz_monologue_guitar_analysis.json', 'GUITAR', 'IRON'),
       (NOW(), NOW(), 2, 7, 'shin_flower_ex_guitar.mp3', 'shin_flower_guitar.mp3',
        'shin_flower_guitar_analysis.json', 'GUITAR', 'IRON'),
       (NOW(), NOW(), 3, 7, 'zoo_will_be_back_ex_guitar.mp3', 'zoo_will_be_back_guitar.mp3',
        'zoo_will_be_back_guitar_analysis.json', 'GUITAR', 'IRON'),
       (NOW(), NOW(), 4, 7, 'kim_around_30_ex_guitar.mp3', 'kim_around_30_guitar.mp3',
        'kim_around_30_guitar_analysis.json', 'GUITAR', 'IRON'),
       (NOW(), NOW(), 5, 7, '015b_flying_ex_guitar.mp3', '015b_flying_guitar.mp3',
        '015b_flying_guitar_analysis.json', 'GUITAR', 'IRON');

-- IRON 티어 BASS 팩 (13번 팩)
INSERT INTO song_by_instrument (created_at, updated_at, song_id, song_instrument_pack_id,
                                song_by_instrument_ex_filename, song_by_instrument_filename,
                                song_by_instrument_analysis_json, instrument, tier)
VALUES (NOW(), NOW(), 1, 13, 'buzz_monologue_ex_bass.mp3', 'buzz_monologue_bass.mp3',
        'buzz_monologue_bass_analysis.json', 'BASS', 'IRON'),
       (NOW(), NOW(), 2, 13, 'shin_flower_ex_bass.mp3', 'shin_flower_bass.mp3',
        'shin_flower_bass_analysis.json', 'BASS', 'IRON'),
       (NOW(), NOW(), 3, 13, 'zoo_will_be_back_ex_bass.mp3', 'zoo_will_be_back_bass.mp3',
        'zoo_will_be_back_bass_analysis.json', 'BASS', 'IRON'),
       (NOW(), NOW(), 4, 13, 'kim_around_30_ex_bass.mp3', 'kim_around_30_bass.mp3',
        'kim_around_30_bass_analysis.json', 'BASS', 'IRON'),
       (NOW(), NOW(), 5, 13, '015b_flying_ex_bass.mp3', '015b_flying_bass.mp3',
        '015b_flying_bass_analysis.json', 'BASS', 'IRON');

-- IRON 티어 DRUM 팩 (19번 팩)
INSERT INTO song_by_instrument (created_at, updated_at, song_id, song_instrument_pack_id,
                                song_by_instrument_ex_filename, song_by_instrument_filename,
                                song_by_instrument_analysis_json, instrument, tier)
VALUES (NOW(), NOW(), 1, 19, 'buzz_monologue_ex_drum.mp3', 'buzz_monologue_drum.mp3',
        'buzz_monologue_drum_analysis.json', 'DRUM', 'IRON'),
       (NOW(), NOW(), 2, 19, 'shin_flower_ex_drum.mp3', 'shin_flower_drum.mp3',
        'shin_flower_drum_analysis.json', 'DRUM', 'IRON'),
       (NOW(), NOW(), 3, 19, 'zoo_will_be_back_ex_drum.mp3', 'zoo_will_be_back_drum.mp3',
        'zoo_will_be_back_drum_analysis.json', 'DRUM', 'IRON'),
       (NOW(), NOW(), 4, 19, 'kim_around_30_ex_drum.mp3', 'kim_around_30_drum.mp3',
        'kim_around_30_drum_analysis.json', 'DRUM', 'IRON'),
       (NOW(), NOW(), 5, 19, '015b_flying_ex_drum.mp3', '015b_flying_drum.mp3',
        '015b_flying_drum_analysis.json', 'DRUM', 'IRON');

-- BRONZE 티어 VOCAL 팩 (2번 팩)
INSERT INTO song_by_instrument (created_at, updated_at, song_id, song_instrument_pack_id,
                                song_by_instrument_ex_filename, song_by_instrument_filename,
                                song_by_instrument_analysis_json, instrument, tier)
VALUES (NOW(), NOW(), 6, 2, 'hyukoh_comes_and_goes_ex_vocal.mp3', 'hyukoh_comes_and_goes_vocal.mp3',
        'hyukoh_comes_and_goes_vocal_analysis.json', 'VOCAL', 'BRONZE'),
       (NOW(), NOW(), 7, 2, 'lee_doll_ex_vocal.mp3', 'lee_doll_vocal.mp3',
        'lee_doll_vocal_analysis.json', 'VOCAL', 'BRONZE'),
       (NOW(), NOW(), 8, 2, 'jang_hard_to_say_ex_vocal.mp3', 'jang_hard_to_say_vocal.mp3',
        'jang_hard_to_say_vocal_analysis.json', 'VOCAL', 'BRONZE'),
       (NOW(), NOW(), 9, 2, 'yoon_like_ex_vocal.mp3', 'yoon_like_vocal.mp3',
        'yoon_like_vocal_analysis.json', 'VOCAL', 'BRONZE'),
       (NOW(), NOW(), 10, 2, 'paul_rain_ex_vocal.mp3', 'paul_rain_vocal.mp3',
        'paul_rain_vocal_analysis.json', 'VOCAL', 'BRONZE');

-- 트랜잭션 커밋
COMMIT;