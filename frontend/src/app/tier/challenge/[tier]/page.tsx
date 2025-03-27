'use client'
// 이 부분에서는 'use client'를 사용하지 않았는데 , 동적 라우팅을 하려면 서버 사이드에서만 해야 하나 ? 
// zustand 상태 관리는 클라이언트 사이드에서만 실행된다. 
// React hook(useState , useEffect 등)은 클라이언트 사이드에서만 실행된다. 

import Tiercard from "@/components/tiercard"
import styles from "./tierpage.module.css"
import { useTierStore } from "@/store/tier"
import { useParams } from "next/navigation"
import React from "react"

export default function TierPage () {
  
  const { userTier } = useTierStore() 
  const params = useParams() // dynamic params를 client side에서 가져오기 위해 사용하는 useParams 훅 

  const tier = typeof params?.tier === 'string' ? params.tier : "Iron" // 타입 명시 

  return (
    <div className={styles.tierpage}>
      <Tiercard 
        currentTier={userTier}
        tier={tier} 
      /> 
      <h1>{tier} tier page</h1>
    </div>
  )
}