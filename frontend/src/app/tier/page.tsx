'use client'

import styles from "./tier.module.css";
import Card from "@/components/card";
import { NextPage } from "next";
import Image from "next/image";
import ChevronLeft from "@/assets/chevron-left.svg";
import ChevronRight from "@/assets/chevron-right.svg";
import { useState } from "react";

const Tier: NextPage = () => {
  const [instrument, setInstrument] = useState("drums");

  const changeInstrument = (direction: "left" | "right") => {
    const instruments = ["drums", "bass", "guitar", "vocals"];
    const currentIndex = instruments.indexOf(instrument);
    let nextIndex = currentIndex;

    if (direction === "left") {
      nextIndex = currentIndex === 0 ? instruments.length - 1 : currentIndex - 1;
    } else if (direction === "right") {
      nextIndex = currentIndex === instruments.length - 1 ? 0 : currentIndex + 1;
    }
    setInstrument(instruments[nextIndex]);
  };

  return (
    <div className={styles.tier}>
      <div className={styles.unityArea}>
        <div className={styles.upper}>
          <div className={styles.changeInstrument} onClick={() => changeInstrument("left")}>
            <Image src={ChevronLeft} alt="changing instrument" />
          </div>
          <div className={styles.playingPeople}>playing character</div>
          <div className={styles.changeInstrument} onClick={() => changeInstrument("right")}>
            <Image src={ChevronRight} alt="change instrument" />
          </div>
        </div>
        <div className={styles.under}>
          <div className={styles.tierContainer}>
            <div className={styles.dummy}>tier icon</div>
            <div className={styles.dummy}>tier text</div>
          </div>
          <div className={styles.notice}>
            notice area <br /> (방어전 정보, 최근 성공한 날)
          </div>
        </div>
      </div>
      <div className={styles.toNext}>
        <Image src={ChevronRight} alt="to next area" style={{ opacity: 0.6 }} />
      </div>
      <div className={styles.navArea}>
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
  );
};

export default Tier;
