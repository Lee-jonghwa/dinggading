'use client'

import styles from "./challenge.module.css"
import Tiercard from "@/components/tiercard"

export default function Challenge () {

  return (
    <div className={styles.challenge}>
      <div className={styles.container}>
        <Tiercard 
          tierText="Silver"
          noticeThing=""
        /> 
      </div>
    </div>
  )
}