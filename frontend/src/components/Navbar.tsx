'use client'

import { useState } from 'react'
import Link from 'next/link'
import '@/styles/components/navbar.css'

const Navbar: React.FC = () => {
  const [isOpen, setIsOpen] = useState(false) // 메뉴 열림 상태
  const [isSearchOpen, setIsSearchOpen] = useState(false) // 검색창 열림 상태

  const handleHamburgerClick = () => {
    setIsOpen(!isOpen)
  }


  return (
    <nav className="navbar">
      if (isOpen === false) { // 기본 상태 
        <div className="default-navbar-container">
          <div className="hamburger-button" onClick={handleHamburgerClick}>=</div>
          <div className="logo">DINGADING</div>
        </div>
      } else if (isSearchOpen === false) {
        <div className="opened-navbar-container">
          <div className="hamburger-button" onClick={handleHamburgerClick}>=</div>
          <div className="logo">DINGADING</div>
          <div className="searchbar-navbar-container">
            
          </div>
        </div>
      } else {
        <div className="searchbar-navbar-container">

        </div>
      }

    </nav>
  )
}

export default Navbar 