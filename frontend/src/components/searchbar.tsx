'use client'

import "@/styles/components/searchbar.css"
import React from "react"
import Image from "next/image"
import search from "@/assets/search.svg"

const Searchbar:React.FC = () => {

  return (
    <div className="searchbar">
      <input type="text" className="text-area"/>
      <Image 
        src={search} 
        alt="search icon" 
        className="icon"
        width={20}  // 원하는 크기로 조정
        height={20} // 원하는 크기로 조정
      />
    </div>
  )
}

export default Searchbar 