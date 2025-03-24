'use client'
import { colors } from "@/constants/colors"
import "@/styles/components/Card.css"
import { useRouter } from "next/navigation"
import React from "react"

interface CardProps {
  s_text : string, 
  l_text : string, 
  icon : string, 
  image : string, 
  href : string,  
}

const Card:React.FC<CardProps> = ({ s_text, l_text, icon, image, href }) => {

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
              <div className="small-text">{s_text}</div>
              <div className="nav-text">
                <div className="large-text">{l_text}</div>
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