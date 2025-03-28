'use client'

import Image, { StaticImageData } from "next/image"
import styles from "./tiercard.module.css"
import IronIcon from "@/assets/star.png"
import BronzeIcon from "@/assets/star.png"
import SilverIcon from "@/assets/star.png"
import GoldIcon from "@/assets/star.png"
import PlatinumIcon from "@/assets/star.png"
import DiamondIcon from "@/assets/star.png"
import { useParams, useRouter } from "next/navigation"

interface TiercardProps {
  tier : string , 
  currentTier : string, 
  noticeThing? : string 
}

export default function Tiercard ({ tier, currentTier, noticeThing} : TiercardProps) {

  const tierOrder = ['IRON', 'BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND']

  const getTierStatus = () => {
    const currentIndex = tierOrder.indexOf(currentTier)
    const tierIndex = tierOrder.indexOf(tier)
     
    if (tierIndex === currentIndex) {
      return {
        isBlurred : false, navigateText : "방어하기", showNotice : true 
      }
    } else if (tierIndex === currentIndex + 1) {
      return {
        isBlurred : true, navigateText : "도전하기", showNotice : false 
      }
    } else if (tierIndex < currentIndex) {
      return {
        isBlurred : false, navigateText : "", showNotice : false 
      }
    } else {
      return {
        isBlurred : true, navigateText : "", showNotice : false 
      }
    }
  }

  const getTierIcon = () => {
    const tierIcons:Record<string, StaticImageData> = { // next/image에서 가져온 이미지는 StaticImageData 타입을 가진다.  
      Iron: IronIcon,
      Bronze: BronzeIcon,
      Silver: SilverIcon,
      Gold: GoldIcon,
      Platinum: PlatinumIcon,
      Diamond: DiamondIcon,
    }
    return tierIcons[tier] || IronIcon  // 기본값을 IronIcon으로 설정
  }
  
  const tierStatus = getTierStatus() 
  const tierIcon = getTierIcon() 

  const params = useParams() 
  const { instrument } = params

  const router = useRouter()

  const toTier = () => {
    if (tier) router.push(`/tier/${instrument}/challenge/${tier}`)
  } 

  return (
    <div className={styles.tiercard} onClick={toTier}>
      <div className={`${styles.icon} ${tierStatus.isBlurred ? styles.blurred : ''}`}>
        <Image 
          src={tierIcon}
          alt="tier image"
          className={styles.image}
        /> 
      </div>
      <div className={styles.text}>{tier}</div>
      <div className={styles.navigate}>{tierStatus.navigateText}</div>
      {tierStatus.showNotice && (
        <div 
          className={styles.notice}
          dangerouslySetInnerHTML={{__html : noticeThing || ""}}
        >
        </div>
      )}
    </div>
  )
}