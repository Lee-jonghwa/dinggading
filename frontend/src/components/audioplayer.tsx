'use client'

import { useState, useRef } from 'react';
import styles from './audioPlayer.module.css';

type AudioPlayerProps = {
  src: string;
  title?: string;
  waveformSrc?: string;
  disabled?: boolean;
  onPlayStateChange?: (isPlaying: boolean) => void;
};

export default function AudioPlayer({
  src,
  title = '오디오',
  waveformSrc,
  disabled = false,
  onPlayStateChange,
}: AudioPlayerProps) {
  const [isPlaying, setIsPlaying] = useState(false);
  const [duration, setDuration] = useState(0);
  const [currentTime, setCurrentTime] = useState(0);
  const audioRef = useRef<HTMLAudioElement | null>(null);
  
  // 재생/정지 토글
  const togglePlayback = () => {
    if (!audioRef.current || disabled) return;
    
    if (isPlaying) {
      audioRef.current.pause();
    } else {
      audioRef.current.play();
    }
  };
  
  // 처음부터 재생
  const playFromStart = () => {
    if (!audioRef.current || disabled) return;
    
    audioRef.current.currentTime = 0;
    audioRef.current.play();
  };
  
  // 오디오 메타데이터 로드 시
  const handleLoadedMetadata = () => {
    if (!audioRef.current) return;
    
    setDuration(audioRef.current.duration);
  };
  
  // 재생 상태 변경 시
  const handlePlayStateChange = (playing: boolean) => {
    setIsPlaying(playing);
    if (onPlayStateChange) {
      onPlayStateChange(playing);
    }
  };
  
  // 시간 업데이트
  const handleTimeUpdate = () => {
    if (!audioRef.current) return;
    
    setCurrentTime(audioRef.current.currentTime);
  };
  
  // 재생 바 변경 시
  const handleSeek = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!audioRef.current) return;
    
    const time = parseFloat(e.target.value);
    audioRef.current.currentTime = time;
    setCurrentTime(time);
  };
  
  // 시간 포맷팅 (mm:ss)
  const formatTime = (timeInSeconds: number) => {
    const minutes = Math.floor(timeInSeconds / 60);
    const seconds = Math.floor(timeInSeconds % 60);
    return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
  };
  
  return (
    <div className={styles.audioPlayer}>
      <div className={styles.playerTitle}>{title}</div>
      
      <div className={styles.waveformContainer}>
        {waveformSrc ? (
          <img src={waveformSrc} alt="파형" className={styles.waveform} />
        ) : (
          <div className={styles.waveformPlaceholder}>
            {isPlaying ? '재생 중...' : '오디오 파형'}
          </div>
        )}
      </div>
      
      <div className={styles.timelineContainer}>
        <span className={styles.timeDisplay}>{formatTime(currentTime)}</span>
        <input
          type="range"
          min="0"
          max={duration || 0}
          value={currentTime}
          onChange={handleSeek}
          className={styles.timeline}
          disabled={disabled}
        />
        <span className={styles.timeDisplay}>{formatTime(duration)}</span>
      </div>
      
      <div className={styles.controls}>
        <button
          className={styles.controlButton}
          onClick={togglePlayback}
          disabled={disabled}
        >
          {isPlaying ? '정지' : '재생'}
        </button>
        
        <button
          className={styles.controlButton}
          onClick={playFromStart}
          disabled={disabled}
        >
          처음부터
        </button>
      </div>
      
      <audio
        ref={audioRef}
        src={src}
        onPlay={() => handlePlayStateChange(true)}
        onPause={() => handlePlayStateChange(false)}
        onEnded={() => handlePlayStateChange(false)}
        onLoadedMetadata={handleLoadedMetadata}
        onTimeUpdate={handleTimeUpdate}
      />
    </div>
  );
}