'use client'

import styles from "./challenge.module.css"
import Tiercard from "@/components/tiercard"
import { useTierStore } from "@/store/tier"

const tierOrder = ['IRON', 'BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND']

export default function Challenge() {

  const { userTier } = useTierStore()

  return (
    <div className={styles.challenge}>
      <div className={styles.container}>
        {userTier ? (
          tierOrder.map((tier) => (
            <Tiercard 
              key={tier}
              tier={tier}
              currentTier={userTier}
              noticeThing={tier === userTier ? "다음 방어전까지 7일 2시간 남았습니다." : ""}
            /> 
          ))
        ) : (
          <p>유저 tier 정보가 없습니다...</p>
        )}
      </div>
    </div>
  )
}