'use client'

import { useState, useEffect } from 'react';
import { useRecordingStore } from '@/store/recording';
import AudioPlayer from '@/components/audioplayer';
import styles from './audioRecorder.module.css';

type AudioRecorderProps = {
  songId: string;
  title?: string;
};

export default function AudioRecorder({ songId, title = '내 녹음' }: AudioRecorderProps) {
  // Zustand 스토어에서 필요한 상태와 액션만 가져오기
  const isRecording = useRecordingStore(state => state.isRecording);
  const currentRecording = useRecordingStore(state => state.currentRecording);
  const startRecording = useRecordingStore(state => state.startRecording);
  const stopRecording = useRecordingStore(state => state.stopRecording);
  
  const [recordingTime, setRecordingTime] = useState(0);
  const [timerInterval, setTimerInterval] = useState<NodeJS.Timeout | null>(null);
  
  // 녹음 시작/중지 토글
  const toggleRecording = async () => {
    if (isRecording) {
      // 녹음 중지
      await stopRecording();
      
      // 타이머 중지
      if (timerInterval) {
        clearInterval(timerInterval);
        setTimerInterval(null);
      }
      
      setRecordingTime(0);
    } else {
      // 녹음 시작
      await startRecording(songId);
      
      // 타이머 시작
      const interval = setInterval(() => {
        setRecordingTime(prev => prev + 1);
      }, 1000);
      
      setTimerInterval(interval);
    }
  };
  
  // 새 녹음 시작
  const startNewRecording = () => {
    toggleRecording();
  };
  
  // 컴포넌트 언마운트 시 타이머 정리
  useEffect(() => {
    return () => {
      if (timerInterval) {
        clearInterval(timerInterval);
      }
    };
  }, [timerInterval]);
  
  // 시간 포맷팅 (mm:ss)
  const formatTime = (timeInSeconds: number) => {
    const minutes = Math.floor(timeInSeconds / 60);
    const seconds = Math.floor(timeInSeconds % 60);
    return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
  };
  
  // 현재 노래에 맞는 녹음인지 확인
  const isSongRecording = currentRecording?.songId === songId;
  
  return (
    <div className={styles.audioRecorder}>
      <div className={styles.recorderTitle}>{title}</div>
      
      {(currentRecording && isSongRecording) ? (
        <div className={styles.recordingResult}>
          <AudioPlayer
            src={currentRecording.url}
            title="녹음된 오디오"
            waveformSrc="/api/placeholder/600/100" 
          />
          
          <div className={styles.recordingInfo}>
            녹음 시간: {new Date(currentRecording.timestamp).toLocaleTimeString()}
          </div>
          
          <button
            className={styles.newRecordingButton}
            onClick={startNewRecording}
          >
            새로 녹음하기
          </button>
        </div>
      ) : (
        <div className={styles.recordingControls}>
          <div className={styles.recordingStatus}>
            {isRecording ? (
              <div className={styles.recordingIndicator}>
                <span className={styles.recordingDot}></span>
                녹음 중... {formatTime(recordingTime)}
              </div>
            ) : (
              <div className={styles.recordingPlaceholder}>
                녹음을 시작하려면 아래 버튼을 클릭하세요
              </div>
            )}
          </div>
          
          <button
            className={`${styles.recordButton} ${isRecording ? styles.recording : ''}`}
            onClick={toggleRecording}
          >
            {isRecording ? '녹음 중지' : '녹음 시작'}
          </button>
        </div>
      )}
    </div>
  );
}