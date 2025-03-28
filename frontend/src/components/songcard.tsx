'use client'

import { useParams, useRouter } from "next/navigation"
import styles from "./songcard.module.css"
import Image from "next/image"

interface SongcardProps {
  songId : number
  songName : string 
  artist : string 
  thumbnailImg : string
  // onClick : () => void 
  // isSelected : boolean
}

export default function Songcard ({
  songId, 
  songName , 
  artist, 
  thumbnailImg, 
  // isSelected=false
} : SongcardProps) {

  const { instrument, tier } = useParams()
  const router = useRouter() 
  const onClickSong = () => {
    router.push(`/tier/${instrument}/challenge/${tier}/${songId}`)
  }

  console.log("songcard.tsx/ backgroundImg props : ", thumbnailImg)

  return (
    <div className={styles.songCard}>
      <div 
        className={styles.song} 
        onClick={onClickSong} 
        // style={{backgroundImage : `url(${thumbnailImg})`}}
      >
        <div 
          className={styles.image}
          style={{backgroundImage : `url(${thumbnailImg})`}}
        >
          {/* <Image 
            src={thumbnailImg} 
            alt="thumbnail image"
            fill
            sizes="100%"
            style={{objectFit: "cover"}}
            priority
          /> */}
        </div>
        <div className={styles.songInfo}>
          <div className={styles.songName}>{songName}</div>
          <div className={styles.artist}>{artist}</div>
        </div>
        <div className={styles.startButton}>도전/연습하기</div>
      </div>
      <div className={styles.soundTest}>음향 테스트</div>
    </div>
  )
}