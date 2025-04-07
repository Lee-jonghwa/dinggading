'use client'
import { NextPage } from "next"
import "@/styles/main.css"
import Card from "@/components/card"
import card1 from "@/app/3dtest/Mainpage/Drum111.gif"
import card2 from "@/app/3dtest/Mainpage/Guitar161.gif"
import card3 from "@/app/3dtest/Mainpage/Bass138.gif"
import BigCard from "@/components/bigcard"

const Main: NextPage = () => {

  return (
    <div className="main">
      <div className="left">
        <BigCard
          subText1="당신의 티어를 측정해보세요"
          titleText="티어 측정하기"
          image={card1}
          href="/tier"
        />
      </div>
      <div className="right">
        <Card
          subText1="나와 맞는 밴드원과"
          subText2="밴드를 결성해보세요"
          titleText="밴드원 구하기"
          image={card2}
          href="/band"
          />
        <Card
          subText1="다른 사람의 연주를 듣고"
          subText2="내 실력을 뽐내 보세요"
          titleText="라이브 하우스"
          image={card3}
          href="/live"
          />
      </div>
    </div>
  )
}

export default Main