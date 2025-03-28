'use client'

import { useParams, useRouter } from "next/navigation"
import styles from "./challengeBySongId.module.css"

// const router = useRouter() 
// const { songId } = useParams() 

export default function ChallengeBySongId () {

  return (
    <div className={styles.challengeBySongId}>
      <div className={styles.songInfo}>
        <div className={styles.songName}>{song.title}</div>
        <div className={styles.artist}>{song.artist}</div>
        <div className={styles.image}>{}</div>
        <div className={styles.controller}>
          <div className={styles.left}>
            <div className={styles.icon}>재생</div>
            <div className={styles.icon}>중지</div>
          </div>
            <div className={styles.icon}>처음부터</div>
        </div>
      </div>
      <div className={styles.playing}></div>
    </div>

  )
}

