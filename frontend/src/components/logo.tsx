'use client'

import React from "react"
import { useRouter } from "next/navigation"
import "@/styles/components/logo.css"
// import Dingading from "@/assets/DINGADING.svg"
import Dingading from "@/app/3dtest/Sub/Logo.png"
import Image from "next/image"

const Logo:React.FC = () => {

  const router = useRouter() 
  const onClickLogo = () => {
    router.push("/main")    
  }

  return (
    <div className="logo" onClick={onClickLogo}>
      <Image 
        src={Dingading}
        alt="dingading logo"
        width={300}
        style={{rotate : "90deg"}}
      />
    </div>
  )
}

export default Logo 