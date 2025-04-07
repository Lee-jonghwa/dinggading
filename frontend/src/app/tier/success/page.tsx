'use client'

import { useRouter } from "next/navigation"
import styles from "./successPage.module.css"
import { useTierStore } from "@/store/tier"
import SilverDrum from "@/app/3dtest/Viewer/SILVERDrum"

export default function SuccessPage() {
  const router = useRouter()
  
  const toTierPage = () => {
    router.push(`/tier/`)
  }

  const { userTier } = useTierStore() 

  return (
    <div className={styles.successPage}>
      <div className={styles.pageContainer}>
        <div className={styles.left}>
          <div className={styles.scoreSection}>
            <div className={styles.unityArea}>
              <SilverDrum />
              {/* <Image src={Trophy} alt="Trophy" width={100} height={100} /> */}
            </div>
          </div>
          <div className={styles.title}>
            <div className={styles.text}>
            🎉 축하합니다! {userTier}에 도달했습니다 🎉
            </div> 
          </div>
        </div>
        <div className={styles.right}>
          <div className={styles.score}>Your Score: 92/100</div>
          <div className={styles.accuracy}>Accuracy: 85</div>
          <div className={styles.combo}>Max Combo: 45</div>
          <div className={styles.toTierPage} onClick={toTierPage}>
            돌아가기
          </div>
        </div>
      </div>
    </div>
  )
}
