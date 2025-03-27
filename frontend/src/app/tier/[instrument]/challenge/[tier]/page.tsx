'use client'
// 이 부분에서는 'use client'를 사용하지 않았는데 , 동적 라우팅을 하려면 서버 사이드에서만 해야 하나 ? 
// zustand 상태 관리는 클라이언트 사이드에서만 실행된다. 
// React hook(useState , useEffect 등)은 클라이언트 사이드에서만 실행된다. 

import Tiercard from "@/components/tiercard"
import styles from "./tierpage.module.css"
import { useTierStore } from "@/store/tier"
import { useParams } from "next/navigation"
import React, { useCallback, useEffect } from "react"
import { useSongsStore } from "@/store/songs"
import Songcard from "@/components/songcard"
import SongCarousel from "@/components/songcarousel"

export default function TierPage () {
  
  const { userTier } = useTierStore() 
  const { fetchSongs, songs=[] } = useSongsStore() 

  const { instrument, tier} = useParams() // dynamic params를 client side에서 가져오기 위해 사용하는 useParams 훅 
  
  const nowTier = typeof tier === 'string' ? tier : "Iron" // 타입 명시 , 기본값 명시 
  const nowInstrument = typeof instrument === "string" ? instrument : "Drums"
  
  // 지금 url에서 가리키는 instrument, tier가 변한다면 그에 맞게 fetchSongs 함수를 실행한다. (조건에 맞는 song pack 불러오기)
  // 하지만 store에서 가져오는 값을 바로 useEffet의 의존성 배열로 사용할 경우, 참조 불안정성 때문에 계속 값이 바뀌게 되고 , 리렌더링하게 되면서 무한 렌더링이 될 수 있음 
  // useCallback을 사용하여 fetchSongs 함수의 참조 안정성 보장
    const fetchSongsCallback = useCallback(() => {
        if (nowInstrument && nowTier) {
            fetchSongs(nowInstrument, nowTier)
        }
    }, [fetchSongs, nowInstrument, nowTier])

    useEffect(() => {
        fetchSongsCallback()
    }, [fetchSongsCallback])

  return (
    <div className={styles.tierpage}>
      <Tiercard 
        currentTier={userTier}
        tier={nowTier} 
      /> 
      <div className={styles.songs}>
        {/* 방법 1. 캐루셀 컴포넌트 관리 */}
        <SongCarousel 
          songs={songs}
        />
        {/* 방법 2. 일일이 card로 가져오기 */}
        {!songs.length ? (
          <p>노래를 불러오는 중 ...</p>
        ) : (
          songs.map((song) => (
            <Songcard
              key={song.id} 
              songName = {song.title}
              artist = {song.artist}
            />
          ))
        )}
          {/* 더미 데이터 */}
          {/* <Songcard
            key={"1"} 
            songName = {"American idiot"}
            artist = {"GreenDay"}
          />
          <Songcard
            key={"12"} 
            songName = {"American idiot"}
            artist = {"GreenDay"}
          />
          <Songcard
            key={"123"} 
            songName = {"American idiot"}
            artist = {"GreenDay"}
          />
          <Songcard
            key={"1234"} 
            songName = {"American idiot"}
            artist = {"GreenDay"}
          />
          <Songcard
            key={"12345"} 
            songName = {"American idiot"}
            artist = {"GreenDay"}
          /> */}
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