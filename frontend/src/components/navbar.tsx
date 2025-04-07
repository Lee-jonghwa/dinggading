'use client'

import { useEffect, useState } from 'react'
import '@/styles/components/navbar.css'
import Searchbar from './searchbar'
import Logo from './logo'
import Image from 'next/image'
import hamburger from "@/assets/hamburger.svg"
import chevronLeft from "@/assets/chevron-left.svg"
import { usePathname } from 'next/navigation'
import missA from "@/app/3dtest/Sub/Look.png"

const Navbar: React.FC = () => {

  const [isOpen, setIsOpen] = useState(false) // 메뉴 열림 상태
  const pathName = usePathname()

  useEffect(() => {
    setIsOpen(false)
  }, [pathName]) // pathname이 변할 때 , navbar는 접기
  
  const handleHamburgerClick = () => { // 메뉴 열림 상태 조절 
    setIsOpen(!isOpen)
  }

  // 조건문 별 들어갈 내용 단순화 위한 컴포넌트 정의 
  const commonThings = (
    <div className="navbar-container-small"> 
      <div className="hamburger-button" onClick={handleHamburgerClick}>
        <Image 
          src={hamburger}
          alt='hamburger-button'
        />
      </div>
      <Logo />
    </div>
  )

  const searchbarThings = (
    <div className="searchbar-container">
      <Searchbar/> 
    </div>
  )

  const middleThings = (
    <div className="navbar-container-middle">
      <div className="columns">
        {searchbarThings}
        <div className="texts">
          <div className="nav-text">로그인</div>
          <div className="nav-text">회원가입</div>
          <div className="nav-text">주변 밴드 찾기</div>
          <div className="nav-text">내 밴드</div>
          <div className="nav-text">티어 측정하기</div>
          <div className="nav-text">이달의 도전곡</div>
          <div className="nav-text">라이브 하우스</div>
        </div>
        <div className="3dImage">
          <Image
            src={missA}
            alt='miss A'
            width={250}
          />
        </div>
      </div>
    </div>
  )

  const renderNavbarContent = () => {
    if (!isOpen) {
      return (
        <div className='container' style={{width : "5rem"}}>
          {commonThings}
        </div>
      )
    } else {  
      return (
        <div className='container' style={{width : "25rem"}}>
          {commonThings}
          {middleThings}
          <div className="chevron" onClick={handleHamburgerClick}>
            <Image src={chevronLeft} alt='chevron left'/>
          </div>
        </div>
      )
    }
  }

  return (
    <div className="navbar">
      {renderNavbarContent()}
      <div className="right">
        <div className="end-line">|</div>
      </div>
    </div>
  )
}

export default Navbar 