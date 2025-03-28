'use client'
import { colors } from "@/constants/colors"
import "@/styles/components/card.css"
import ChevronRight from "@/assets/chevron-right.svg"
import Image from "next/image"
import { useRouter } from "next/navigation"

interface CardProps {
  subText : string, 
  titleText : string, 
  image : string, 
  href : string,  
}

const Card:React.FC<CardProps> = ({ subText, titleText, image, href }) => {

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
                <div className="navigate-icon">
                  <Image
                    src={ChevronRight}
                    alt="chevron right"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Card