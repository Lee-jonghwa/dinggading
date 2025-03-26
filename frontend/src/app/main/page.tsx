'use client'
import { NextPage } from "next"
import "@/styles/main.css"
import Card from "@/components/card"

const Main: NextPage = () => {

  return (
    <div className="main"> 
      <Card 
        subText="당신의 티어를 측정해보세요"
        titleText="티어 측정"
        icon=">"
        image="#"
        href="/tier"
      />
      <Card 
        subText="나와 맞는 밴드원과 밴드를 결성해보세요"
        titleText="밴드원 구하기"
        icon=">"
        image="#"
        href="/band"
      />
      <Card 
        subText="다른 사람의 연주를 듣고 내 실력을 뽐내 보세요"
        titleText="라이브 하우스"
        icon=">"
        image="#"
        href="/live"
      />
    </div>
  )
}

export default Main