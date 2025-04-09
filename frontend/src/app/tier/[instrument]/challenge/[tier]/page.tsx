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
  const { instrument, tier } = useParams() // instrument와 tier 모두 가져오기
  const nowTier = typeof tier === 'string' ? tier : "IRON" // 기본값을 IRON으로 설정
  const nowInstrument = typeof instrument === 'string' ? instrument : "DRUM" // 기본값을 DRUM으로 설정
  
  const { fetchSongs, songs } = useSongsStore()
  
  useEffect(() => {
    console.log("[tier]/page.tsx useEffect 실행")
    fetchSongs(nowInstrument, nowTier) // instrument와 tier 모두 전달
  }, [nowInstrument, nowTier, fetchSongs])
  
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