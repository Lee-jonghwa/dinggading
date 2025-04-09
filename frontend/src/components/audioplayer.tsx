'use client'

import { useState, useRef, useEffect } from 'react';
import styles from './audioPlayer.module.css';

interface AudioPlayerProps {
  src: string;
  title: string;
  waveformSrc?: string;
  onPlayStart?: () => void;
  onPlayEnd?: () => void;
  autoPlay?: boolean;
}

export default function AudioPlayer({
  src,
  title,
  waveformSrc,
  onPlayStart,
  onPlayEnd,
  autoPlay = false
}: AudioPlayerProps) {
  const [isPlaying, setIsPlaying] = useState(false);
  const [progress, setProgress] = useState(0);
  const [duration, setDuration] = useState(0);
  const audioRef = useRef<HTMLAudioElement>(null);
  const waveformRef = useRef<HTMLDivElement>(null);

  // 오디오 로드 시 처리
  useEffect(() => {
    const audio = audioRef.current;
    if (!audio) return;

    const handleLoadedMetadata = () => {
      setDuration(audio.duration);
    };

    audio.addEventListener('loadedmetadata', handleLoadedMetadata);
    
    return () => {
      audio.removeEventListener('loadedmetadata', handleLoadedMetadata);
    };
  }, [src]);

  // 오디오 이벤트 처리
  useEffect(() => {
    const audio = audioRef.current;
    if (!audio) return;

    const handleTimeUpdate = () => {
      setProgress(audio.currentTime / audio.duration * 100);
    };

    const handlePlay = () => {
      setIsPlaying(true);
      if (onPlayStart) onPlayStart();
    };

    const handlePause = () => {
      setIsPlaying(false);
    };

    const handleEnded = () => {
      setIsPlaying(false);
      setProgress(0);
      if (onPlayEnd) onPlayEnd();
    };

    audio.addEventListener('timeupdate', handleTimeUpdate);
    audio.addEventListener('play', handlePlay);
    audio.addEventListener('pause', handlePause);
    audio.addEventListener('ended', handleEnded);

    return () => {
      audio.removeEventListener('timeupdate', handleTimeUpdate);
      audio.removeEventListener('play', handlePlay);
      audio.removeEventListener('pause', handlePause);
      audio.removeEventListener('ended', handleEnded);
    };
  }, [onPlayStart, onPlayEnd]);

  // 자동 재생
  useEffect(() => {
    if (autoPlay && audioRef.current) {
      // 자동 재생 정책으로 인해 사용자 상호작용이 필요할 수 있음
      // 실제 환경에서는 사용자에게 자동 재생 권한을 요청해야 할 수 있음
      const playPromise = audioRef.current.play();
      
      if (playPromise !== undefined) {
        playPromise.catch(error => {
          console.error('자동 재생 실패:', error);
        });
      }
    }
  }, [autoPlay]);

  const togglePlay = () => {
    const audio = audioRef.current;
    if (!audio) return;

    if (isPlaying) {
      audio.pause();
    } else {
      audio.play();
    }
  };

  const handleProgressBarClick = (e: React.MouseEvent<HTMLDivElement>) => {
    const audio = audioRef.current;
    const progressBar = e.currentTarget;
    if (!audio || !progressBar) return;

    const rect = progressBar.getBoundingClientRect();
    const clickPosition = (e.clientX - rect.left) / rect.width;
    const newTime = clickPosition * audio.duration;
    
    audio.currentTime = newTime;
    setProgress(clickPosition * 100);
  };

  const formatTime = (seconds: number) => {
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return `${mins}:${secs < 10 ? '0' : ''}${secs}`;
  };

  return (
    <div className={styles.audioPlayer}>
      <div className={styles.title}>{title}</div>
      
      <audio ref={audioRef} src={src} />
      
      <div className={styles.controls}>
        <button 
          className={styles.playButton} 
          onClick={togglePlay}
        >
          {isPlaying ? '일시정지' : '재생'}
        </button>
        
        <div className={styles.timeInfo}>
          <span>{formatTime(audioRef.current?.currentTime || 0)}</span>
          <span>/</span>
          <span>{formatTime(duration)}</span>
        </div>
      </div>
      
      <div className={styles.progressContainer} onClick={handleProgressBarClick}>
        <div className={styles.progressBackground}>
          {waveformSrc ? (
            <div 
              ref={waveformRef} 
              className={styles.waveform} 
              style={{ backgroundImage: `url(${waveformSrc})` }}
            />
          ) : (
            <div className={styles.progressBar} />
          )}
        </div>
        <div 
          className={styles.progressCurrent} 
          style={{ width: `${progress}%` }}
        />
      </div>
    </div>
  );
}