'use client'

import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { useRecordingStore, cleanupRecordingResources } from '@/store/recording';
import AudioPlayer from '@/components/audioplayer';
import AudioRecorder from '@/components/audiorecorder';
import styles from './challengeBySongId.module.css';
import { useSongsStore } from '@/store/songs';
import { useSongByInstrumentStore } from '@/store/songbyinstrument';
import { getYouTubeThumbnail } from '@/utils/getYoutubeThumbnail';
import { useRecordStore } from '@/store/record';

export default function ChallengeBySongId() {
  // URL 파라미터 가져오기
  const { instrument, tier, songId } = useParams() 
  const router = useRouter() 
  
  // 자동 녹음 상태 관리
  const [shouldStartRecording, setShouldStartRecording] = useState(false);
  const [shouldStopRecording, setShouldStopRecording] = useState(false);
  
  // 녹음 스토어에서 현재 녹음 상태 가져오기
  const currentRecording = useRecordingStore(state => state.currentRecording);
  
  // 노래 데이터 가져오기 
  const { songs, fetchSongs } = useSongsStore();
  
  // 악기별 곡 URL 가져오기
  const { songUrl, loading: songUrlLoading, fetchSongByInstrumentUrlBySongIdAndInstrument } = useSongByInstrumentStore();
  const { createRecord } = useRecordStore();

  useEffect(() => {
    if (tier && instrument) {
      const tierValue = Array.isArray(tier) ? tier[0] : tier;
      const instrumentValue = Array.isArray(instrument) ? instrument[0] : instrument;
      fetchSongs(instrumentValue, tierValue);
    }
  }, [tier, instrument, fetchSongs]);
  
  // 악기별 곡 URL 가져오기
  useEffect(() => {
    if (songId && instrument) {
      const songIdValue = Array.isArray(songId) ? songId[0] : songId;
      const instrumentValue = Array.isArray(instrument) ? instrument[0] : instrument 
      fetchSongByInstrumentUrlBySongIdAndInstrument(parseInt(songIdValue), instrumentValue);
    }
  }, [songId, instrument, fetchSongByInstrumentUrlBySongIdAndInstrument]);
  
  const song = songs.find(s => s.songId.toString() === songId) || {
    // 마땅한 곡이 없을 때 값 
    songId: 123, 
    title: "Shape of You",
    artist: "Ed Sheeran",
    description: "2017년 Ed Sheeran의 히트곡", 
    youtubeUrl: "https://www.youtube.com/watch?v=JGwWNGJdvx8",
    audioUrl: "", 
  };

  // 재생 시작 시 녹음 시작
  const handlePlayStart = () => {
    setShouldStartRecording(true);
    // 다음 렌더링에서 트리거를 초기화
    setTimeout(() => setShouldStartRecording(false), 100);
  };

  // 재생 종료 시 녹음 종료
  const handlePlayEnd = () => {
    setShouldStopRecording(true);
    // 다음 렌더링에서 트리거를 초기화
    setTimeout(() => setShouldStopRecording(false), 100);
  };

  // 도전 시작하기
  const startChallenge = async () => {
    if (!currentRecording || currentRecording.songId !== songId) {
      alert('먼저 녹음을 완료해주세요.');
      return;
    }
    
    try {
      ///////////////// 디버깅용 파일 저장 로직 시작 /////////////////
      // // 녹음 파일 정보 로깅
      // console.log('녹음 파일 정보:', {
      //   blob: currentRecording.blob,
      //   size: currentRecording.blob.size,
      //   type: currentRecording.blob.type,
      //   duration: currentRecording.duration
      // });
      ///////////////// 디버깅용 파일 저장 로직 끝 /////////////////
      
      // 녹음 파일을 File 객체로 변환
      const audioFile = new File([currentRecording.blob], `recording-${Date.now()}.webm`, {
        type: 'audio/webm'
      });
      
      ///////////////// 디버깅용 파일 저장 로직 시작 /////////////////
      // // File 객체 정보 로깅
      // console.log('변환된 File 객체:', {
      //   name: audioFile.name,
      //   size: audioFile.size,
      //   type: audioFile.type,
      //   lastModified: audioFile.lastModified
      // });

      // // 파일 다운로드 테스트 (디버깅용)
      // const downloadUrl = URL.createObjectURL(audioFile);
      // const a = document.createElement('a');
      // a.href = downloadUrl;
      // a.download = audioFile.name;
      // document.body.appendChild(a);
      // a.click();
      // document.body.removeChild(a);
      // URL.revokeObjectURL(downloadUrl);
      ///////////////// 디버깅용 파일 저장 로직 끝 /////////////////
      
      // 녹음 정보 생성
      const recordInfo = {
        dtype: 'CHALLENGE' as const,
        title: `${instrument} - ${song.title}`,
      };
      
      ///////////////// 디버깅용 파일 저장 로직 시작 /////////////////
      // console.log('서버로 전송되는 데이터:', {
      //   recordInfo,
      //   audioFile: {
      //     name: audioFile.name,
      //     size: audioFile.size,
      //     type: audioFile.type
      //   }
      // });
      ///////////////// 디버깅용 파일 저장 로직 끝 /////////////////
      
      // 녹음 파일 업로드
      await createRecord(recordInfo, audioFile);
      
      alert('결과가 제출되었습니다.');
      // 성공 시 페이지 이동
      router.push(`/tier/success`);
    } catch (error) {
      console.error('녹음 파일 업로드 중 오류 발생:', error);
      alert('녹음 파일 업로드 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
  };
  
  // 컴포넌트 언마운트 시 리소스 정리
  useEffect(() => {
    return () => {
      cleanupRecordingResources();
    };
  }, []);

  const bgImage = getYouTubeThumbnail(song.youtubeUrl) || '';
  
  return (
    <div className={styles.pageContainer}>
      <div className={styles.songInfo}>
        <div className={styles.imageField}>
          {bgImage ? (
            <img 
              src={bgImage} 
              alt={`${song.title} 유튜브 썸네일`} 
              className={styles.albumCover}
            />
          ) : (
            <div></div>
          )}
        </div>
        <div className={styles.songText}>
          <div className={styles.songName}>{song.title}</div>
          <div className={styles.artist}>{song.artist}</div>
        </div>
      </div>
      
      <div className={styles.instructions}>
        <p>아래 재생 버튼을 눌러 {instrument} 파트가 제거된 버전의 곡을 들으세요.</p>
        <p>재생 버튼을 누르면 자동으로 녹음이 시작됩니다. 곡이 끝나면 녹음도 자동으로 종료됩니다.</p>
        <p>녹음이 완료된 후 결과 확인하기 버튼을 눌러 평가를 받으세요.</p>
      </div>
      
      <div className={styles.playingInfo}>
        <div className={styles.songWave}>
          {songUrlLoading ? (
            <div className={styles.loadingMessage}>악기별 곡 로딩 중...</div>
          ) : songUrl ? (
            <AudioPlayer
              src={songUrl}
              title={`${instrument} 파트 제거 버전`}
              waveformSrc=""
              onPlayStart={handlePlayStart}
              onPlayEnd={handlePlayEnd}
            />
          ) : (
            <div className={styles.errorMessage}>악기별 곡을 불러올 수 없습니다.</div>
          )}
        </div>
        
        <div className={styles.userWave}>
          <AudioRecorder
            songId={songId as string}
            title="내 녹음"
            externalStartTrigger={shouldStartRecording}
            externalStopTrigger={shouldStopRecording}
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