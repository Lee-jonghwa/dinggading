'use client'
import { colors } from "@/constants/colors"
import "@/styles/components/card.css"
import { useRouter } from "next/navigation"
import React from "react"

interface CardProps {
  subText : string, 
  titleText : string, 
  icon : string, 
  image : string, 
  href : string,  
}

const Card:React.FC<CardProps> = ({ subText, titleText, icon, image, href }) => {

  const router = useRouter()
  const handleClick = () => {
    if (href) router.push(href)
  }

  return (
    <div className="card">
      <div className="card-container" onClick={handleClick}>
        <div className="image-container" style={{backgroundImage : `url(${image})`}}>
          <div className="text-container" style={{backgroundColor : colors.BLACK}}>
            <div className="text">
              <div className="small-text">{subText}</div>
              <div className="nav-text">
                <div className="large-text">{titleText}</div>
                <div className="navigate-icon">{icon}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Card