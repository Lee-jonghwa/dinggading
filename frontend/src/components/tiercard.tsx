'use client'

import Image from "next/image"
import medal from "@/assets/star.png"
import styles from "./tiercard.module.css"

interface TiercardProps {
  tierText : string , 
  noticeThing? : string 
}

export default function Tiercard ({tierText, noticeThing} : TiercardProps) {

  return (
    <div className={styles.tiercard}>
      <div className={styles.icon}>
        <Image 
          src={medal}
          alt="tier image"
        /> 
      </div>
      <div className={styles.text}>{tierText}</div>
      <div className={styles.navigate}>도전하기</div>
      <div className={styles.notice}>{noticeThing}</div>
    </div>
  )
}