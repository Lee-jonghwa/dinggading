'use client'

import { useEffect } from "react"
import { useParams } from "next/navigation"
import Tiercard from "@/components/tiercard"
import styles from "./tierpage.module.css"
import { useTierStore } from "@/store/tier"
import { useSongsStore } from "@/store/songs"
import SongCarousel from "@/components/songcarousel"

export default function TierPage() {
  const { userTier } = useTierStore()
  const { tier } = useParams() // dynamic params를 client side에서 가져오기 위해 사용하는 useParams 훅
  const nowTier = typeof tier === 'string' ? tier : "Iron" // 타입 명시, 기본값 명시
  
  const { fetchSongs, songs } = useSongsStore()
  
  useEffect(() => {
    fetchSongs(nowTier) // fetchSongs 함수 호출 시 현재 선택된 tier 전달
  }, [nowTier, fetchSongs])
  
  return (
    <div className={styles.tierpage}>
      <Tiercard
        currentTier={userTier}
        tier={nowTier}
      />
      
      <div className={styles.songs}>
        {songs.length > 0 && (
          <SongCarousel
            songs={songs}
          />
        )}
      </div>
    </div>
  )
}