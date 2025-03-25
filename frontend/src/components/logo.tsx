'use client'

import React from "react"
import { useRouter } from "next/navigation"
import "@/styles/components/logo.css"

const Logo:React.FC = () => {

  const router = useRouter() 
  const onClickLogo = () => {
    router.push("/main")    
  }

  return (
    <div className="logo" onClick={onClickLogo}>
      <h1>DINGADING</h1>
    </div>
  )
}

export default Logo 