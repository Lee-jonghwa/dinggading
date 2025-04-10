// C:\Users\SSAFY\Desktop\project\S12P21E107\frontend\src\app\tier\attempt\[attemptId]\success\SuccessContent.tsx
'use client'

import { useRouter } from "next/navigation"
import { useEffect } from "react"
import styles from "./successPage.module.css"
import { useTierStore } from "@/store/tier"
import { useAttemptStore } from "@/store/attempt"
import SilverDrum from "@/app/3dtest/Viewer/SILVERDRUM"

// 클라이언트 컴포넌트는 params 대신 attemptId를 직접 props로 받습니다
export default function SuccessContent({ attemptId }: { attemptId: string }) {
  console.log("Attempt ID:", attemptId)
  const router = useRouter()
  
  const { userTier } = useTierStore()
  const { attempt, loading, fetchAttempt } = useAttemptStore()
  
  useEffect(() => {
    if (attemptId) {
      // String을 숫자로 변환
      const parsedAttemptId = parseInt(attemptId, 10)
      if (!isNaN(parsedAttemptId)) {
        // 자동으로 더미 데이터 사용 옵션 활성화
        fetchAttempt(parsedAttemptId);
      }
    }
  }, [attemptId, fetchAttempt])
  
  const toTierPage = () => {
    router.push(`/tier`)
  }

  // 로딩 상태 처리
  if (loading) {
    return <div className={styles.successPage}>데이터를 불러오는 중...</div>
  }

  return (
    <div className={styles.successPage}>
      <div className={styles.pageContainer}>
        <div className={styles.left}>
          <div className={styles.scoreSection}>
            <div className={styles.unityArea}>
              <SilverDrum />
            </div>
          </div>
          <div className={styles.title}>
            <div className={styles.text}>
              🎉 축하합니다! {userTier}에 도달했습니다 🎉 
            </div> 
          </div>
        </div>
        <div className={styles.right}>
          {attempt && (
            <>
              <div className={styles.score}>총점: {attempt.totalScore || 0}/100</div>
              <div className={styles.accuracy}>음정 점수: {attempt.tuneScore || 0}</div>
              <div className={styles.combo}>톤 점수: {attempt.toneScore || 0}</div>
              <div className={styles.combo}>박자 점수: {attempt.beatScore || 0}</div>

              <div className={styles.gameType}>
                게임 모드: {attempt.gameType === 'RANK' ? '랭크모드' : '연습모드'}
              </div>
              {attempt.rankType && (
                <div className={styles.rankType}>
                  랭크 유형: {
                    attempt.rankType === 'FIRST' ? '첫 도전' :
                    attempt.rankType === 'CHALLENGE' ? '챌린지' : 
                    attempt.rankType === 'DEFENCE' ? '방어전' : ''
                  }
                </div>
              )}
            </>
          )}
          <div className={styles.toTierPage} onClick={toTierPage}>
            돌아가기
          </div>
        </div>
      </div>
    </div>
  )
}