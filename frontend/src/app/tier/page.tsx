'use client'

import "@/styles/tier.css"
import Card from "@/components/card"
import { NextPage } from "next"
import Image from "next/image"
import ChevronLeft from "@/assets/chevron-left.svg"
import ChevronRight from "@/assets/chevron-right.svg"
import { useState } from "react"

const Tier: NextPage = () => {

  const [instrument, setInstrument] = useState("drums")

  const changeInstrument = (direction : "left" | "right") => {
    const instruments = ['drums', 'bass', 'guitar', 'vocals']
    const currentIndex = instruments.indexOf(instrument)
    let nextIndex = currentIndex

    if (direction === "left") {
      nextIndex = currentIndex === 0 ? instruments.length - 1 : currentIndex - 1
    } else if (direction === "right") {
      nextIndex = currentIndex === instruments.length - 1 ? 0 : currentIndex + 1
    }
    setInstrument(instruments[nextIndex])
  }

  return (
    <div className="tier">
      <div className="unity-area">
        <div className="upper">
          <div className="change instrument" onClick={() => changeInstrument("left")}>
            <Image
              src={ChevronLeft}
              alt="changing instrument"
            />
          </div>
          <div className="playing-people">
            playing character
          </div>
          <div className="change-instrument" onClick={() => changeInstrument("right")}>
            <Image
              src={ChevronRight}
              alt="change instrument"
            />
          </div>
        </div>
        <div className="under">
          <div className="tier-container">
            <div className="dummy">tier icon</div>
            <div className="dummy">tier text</div>
          </div>
          <div className="notice">
            notice area <br/> (방어전 정보, 최근 성공한 날)
          </div>
        </div>
      </div>
      <div className="to-next">
        <Image
          src={ChevronRight}
          alt="to next area"
          style={{ opacity: 0.6 }}
        />
      </div>
      <div className="nav-area">
        <Card
          href={`/tier/${instrument}/challenge/`}
          subText="내 티어를 방어하고 더 높은 티어에 도전하세요"
          titleText="도전하기"
          image="#"
          />
        <Card
          href={`/tier/${instrument}/practice`}
          subText="내 티어에 맞는 곡으로 연습하세요"
          titleText="연습하기"
          image="#"
          />
      </div>
    </div>
  )
}

export default Tier