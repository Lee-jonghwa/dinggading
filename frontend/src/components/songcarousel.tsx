'use client'

import React, { useState } from 'react'
import Songcard from "@/components/songcard"
import styles from './songcarousel.module.css'

interface Song {
  id: string
  title: string
  artist: string
}

interface SongCarouselProps {
  songs: Song[]
}

export default function SongCarousel({ songs }: SongCarouselProps) {
  const [currentIndex, setCurrentIndex] = useState(0)

  // 다섯 곡 중 현재 곡의 위아래 곡 보여주기
  const handleNext = () => {
    setCurrentIndex((prevIndex) => 
      prevIndex < songs.length - 1 ? prevIndex + 1 : prevIndex
    )
  }

  const handlePrev = () => {
    setCurrentIndex((prevIndex) => 
      prevIndex > 0 ? prevIndex - 1 : prevIndex
    )
  }

  return (
    <div className={styles.songCarousel}>
      <div className={styles.carouselWrapper}>
        {/* 이전 곡 (위쪽) */}
        {currentIndex > 0 && (
          <div className={styles.prevSong}>
            <Songcard 
              key={songs[currentIndex - 1].id}
              songName={songs[currentIndex - 1].title}
              artist={songs[currentIndex - 1].artist}
              // className={styles.smallerCard}
            />
          </div>
        )}

        <div className={styles.navigationButtons}>
          <button 
            onClick={handlePrev} 
            disabled={currentIndex === 0}
            className={styles.prevButton}
          >
            ▲
          </button>
        </div>

        {/* 현재 곡 (가운데) */}
        <div className={styles.currentSong}>
          <Songcard 
            key={songs[currentIndex].id}
            songName={songs[currentIndex].title}
            artist={songs[currentIndex].artist}
            // className={styles.largerCard}
          />
        </div>

        <div className={styles.navigationButtons}>
          <button 
            onClick={handleNext} 
            disabled={currentIndex === songs.length - 1}
            className={styles.nextButton}
          >
            ▼
          </button>
        </div>

        {/* 다음 곡 (아래쪽) */}
        {currentIndex < songs.length - 1 && (
          <div className={styles.nextSong}>
            <Songcard 
              key={songs[currentIndex + 1].id}
              songName={songs[currentIndex + 1].title}
              artist={songs[currentIndex + 1].artist}
              // className={styles.smallerCard}
            />
          </div>
        )}

        {/* 네비게이션 버튼 */}
        <div className={styles.navigationButtons}>
          <button 
            onClick={handlePrev} 
            disabled={currentIndex === 0}
            className={styles.prevButton}
          >
            ▲
          </button>
          <button 
            onClick={handleNext} 
            disabled={currentIndex === songs.length - 1}
            className={styles.nextButton}
          >
            ▼
          </button>
        </div>
      </div>
    </div>
  )
}