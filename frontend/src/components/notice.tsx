'use client'

import { useNotificationStore } from "@/store/notification"
import styles from "./notice.module.css"
import { useEffect } from "react"
import bell from "@/assets/Bell.svg"
import Image from "next/image"

export default function Notice () {

  const { fetchNotification } = useNotificationStore() 
  useEffect(() => {
    fetchNotification()
  }, [])

  return (
    <div className={styles.container}>
      <Image
        src={bell}
        alt="bell"
        width={36}
        height={36}
      />
    </div>

  )
}