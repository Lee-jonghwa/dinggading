'use client'

import { useState } from 'react'
import '@/styles/components/navbar.css'
import Searchbar from './searchbar'
import Logo from './logo'

const Navbar: React.FC = () => {
  const [isOpen, setIsOpen] = useState(false) // 메뉴 열림 상태

  const handleHamburgerClick = () => { // 메뉴 열림 상태 조절 
    setIsOpen(!isOpen)
  }

  // 조건문 별 들어갈 내용 단순화 위한 컴포넌트 정의 
  const commonThings = (
    <div className="navbar-container-small"> 
      <div className="hamburger-button" onClick={handleHamburgerClick}>=</div>
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
      {searchbarThings}
      <div className="close-chevron" onClick={handleHamburgerClick}>C</div>
    </div>
  )

  const renderNavbarContent = () => {
    if (!isOpen) {
      return (
        <div className='container'>
          {commonThings}
        </div>
      )
    } else {
      return (
        <div className='container'>
          {commonThings}
          {middleThings}
        </div>
      )
    }
  }

  return (
    <div className="navbar">
      {renderNavbarContent()}
    </div>
  )
}

export default Navbar 