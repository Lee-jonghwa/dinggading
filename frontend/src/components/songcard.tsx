'use client'

import styles from "./songcard.module.css"

interface SongcardProps {
  songName : string 
  artist : string 
  // onClick : () => void 
  // isSelected : boolean
}

export default function Songcard ({
  songName , 
  artist, 
  // isSelected=false
} : SongcardProps) {

  return (
    <div className={styles.songCard}>
      <div className={styles.songInfo}>
        <div className={styles.songName}>{songName}</div>
        <div className={styles.artist}>{artist}</div>
      </div>
      <div className={styles.startButton}>도전/연습하기</div>
      <div className={styles.soundTest}>음향 테스트</div>
    </div>
  )
}