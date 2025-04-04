'use client'

import { useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { useRecordingStore, cleanupRecordingResources } from '@/store/recording';
// import AudioPlayer from '@/components/audioplayer';
import AudioRecorder from '@/components/audiorecorder';
import styles from './challengeBySongId.module.css';
import { useSongsStore } from '@/store/songs';
import { getYouTubeThumbnail } from '@/utils/getYoutubeThumbnail';

export default function ChallengeBySongId() {
  // URL 파라미터 가져오기
  const { instrument, tier, songId } = useParams() 
  const router = useRouter() 
  
  // 녹음 스토어에서 현재 녹음 상태 가져오기
  const currentRecording = useRecordingStore(state => state.currentRecording);
  
  // 노래 데이터 가져오기 
  const { songs, fetchSongs } = useSongsStore() 

  useEffect(() => {
    if (tier) {
      const tierValue = Array.isArray(tier) ? tier[0] : tier; // tier가 배열일 경우 첫 번째 요소 사용
      fetchSongs(tierValue); // 현재 티어에 맞는 노래 가져오기 
    }
  }, [tier, fetchSongs])
  
  const song = songs.find(s => s.songId.toString() === songId) || {
    // 이렇게 들어올 song data 
    songId: 123, 
    title: "Shape of You",
    artist: "Ed Sheeran",
    description : "2017년 Ed Sheeran의 히트곡", 
    youtubeUrl: "https://www.youtube.com/watch?v=JGwWNGJdvx8",
    audioUrl : "", 
    waveforUrl : "", 
  };

  // 도전 시작하기
  const startChallenge = () => {
    if (!currentRecording || currentRecording.songId !== songId) {
      alert('먼저 녹음을 완료해주세요.');
      return;
    }
    
    // TODO: 서버에 녹음 파일 업로드 또는 처리 로직
    console.log('도전 시작:', {
      instrument,
      tier,
      songId,
      recordingId: currentRecording.id
    });
    
    // FormData 예시 (실제 API 전송 시)
    const formData = new FormData();
    formData.append('audio', currentRecording.blob, `recording-${Date.now()}.webm`);
    formData.append('songId', songId as string);
    formData.append('instrument', instrument as string);
    formData.append('tier', tier as string);
    
    alert('결과가 제출되었습니다.');

    // 성공 시 , 실패 시 
    router.push(`/tier/${instrument}/challenge/${tier}/${songId}/success`)
  };
  
  // 컴포넌트 언마운트 시 리소스 정리
  useEffect(() => {
    return () => {
      cleanupRecordingResources();
    };
  }, []);

  const bgImage = getYouTubeThumbnail(song.youtubeUrl) || ''; // 기본값 설정
  
  return (
    <div className={styles.pageContainer}>
      <div className={styles.songInfo}>
        <div className={styles.imageField}>
          <img 
            src={bgImage} 
            alt={`${song.title} 유튜브 썸네일`} 
            className={styles.albumCover}
          />
        </div>
        <div className={styles.songText}>
          <div className={styles.songName}>{song.title}</div>
          <div className={styles.artist}>{song.artist}</div>
        </div>
      </div>
      
      <div className={styles.playingInfo}>
        <div className={styles.songWave}>
          {/* <AudioPlayer
            src={song.audioUrl}
            title="원곡"
            waveformSrc={song.waveformUrl}
          /> */}
        </div>
        
        <div className={styles.userWave}>
          <AudioRecorder
            songId={songId as string}
            title="내 녹음"
          />
        </div>
        <button
          className={styles.challengeButton}
          onClick={startChallenge}
          disabled={!currentRecording || currentRecording.songId !== songId}
        >
          결과 확인하기
          </button>
      </div>
      
    </div>
  );
}