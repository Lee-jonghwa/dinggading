'use client'
// 이 부분에서는 'use client'를 사용하지 않았는데 , 동적 라우팅을 하려면 서버 사이드에서만 해야 하나 ? 
// zustand 상태 관리는 클라이언트 사이드에서만 실행된다. 
// React hook(useState , useEffect 등)은 클라이언트 사이드에서만 실행된다. 

import Tiercard from "@/components/tiercard"
import styles from "./tierpage.module.css"
import { useTierStore } from "@/store/tier"
import { useParams } from "next/navigation"
import React, { useEffect } from "react"
import { useSongsStore } from "@/store/songs"

export default function TierPage () {
  
  const { userTier } = useTierStore() 
  const { instrument, tier} = useParams() // dynamic params를 client side에서 가져오기 위해 사용하는 useParams 훅 
  const store = useSongsStore() 

  const nowTier = typeof tier === 'string' ? tier : "Iron" // 타입 명시 

  useEffect(() => {
    if (instrument && tier)
    store.fetchSongs(instrument , tier)
  }, [])

  return (
    <div className={styles.tierpage}>
      <Tiercard 
        currentTier={userTier}
        tier={nowTier} 
      /> 
      <div className={styles.songs}>
        <div className={styles.song}>Song1</div>
        <div className={styles.song}>Song2</div>
        <div className={styles.song}>Song3</div>
        <div className={styles.song}>Song4</div>
        <div className={styles.song}>Song5</div>
      </div>
      <div className={styles.right}>
        <div className={styles.selectedSongs}>
          <div className={styles.songTitle}>
            <div className={styles.songName}>Songname</div>
            <div className={styles.artist}>Artist</div>
          </div>
          <div className={styles.startButton}>도전/연습하기</div>
        </div>
        <div className={styles.soundtest}>음향 테스트</div>
      </div>
    </div>
  )
}