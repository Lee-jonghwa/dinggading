'use client'

import { useEffect, useState, useRef } from 'react'
import { useRouter } from 'next/navigation'
import { NotificationApi } from "@generated/api"
import { useConfigStore } from '@/store/config'
import './notification.module.css' // 알림 컴포넌트에 대한 CSS
import Image from 'next/image'
import bell from "@/assets/Bell.svg"

// 알림 타입 열거형
export enum NotificationType {
  CHAT = 'CHAT',
  RECRUITMENT = 'RECRUITMENT',
  BAND = 'BAND',
  SYSTEM = 'SYSTEM'
}

// 알림 정보 인터페이스
export interface NotificationDTO {
  notificationId: number;
  chatRoomId?: string;
  messageId?: string;
  senderId: string;
  senderNickname?: string;
  senderProfileUrl?: string;
  receiverId: string;
  message: string;
  readOrNot: boolean;
  type: NotificationType;
  acceptUrl?: string;
  rejectUrl?: string;
  createdAt: string;
}

const NotificationComponent: React.FC = () => {
  const [notifications, setNotifications] = useState<NotificationDTO[]>([])
  const [unreadCount, setUnreadCount] = useState(0)
  const [showNotifications, setShowNotifications] = useState(false)
  const notificationRef = useRef<HTMLDivElement>(null)
  const router = useRouter()
  const configStore = useConfigStore.getState()
  
  // 알림 가져오기
  const fetchNotifications = async () => {
    try {
      const notificationApi = new NotificationApi(configStore.apiConfig)
      // const response = await notificationApi.getNotifications(0, 10)
      const response = await notificationApi.getNotifications()
      
      if (response.data && response.data.content) {
        // setNotifications(response.data.content)
        
        // 읽지 않은 알림 개수 계산
        const unread = response.data.content.filter(notification => !notification.readOrNot).length
        setUnreadCount(unread)
      }
    } catch (error) {
      console.error('알림을 가져오는데 실패했습니다:', error)
    }
  }
  
  // 알림 읽음 처리
  const markAsRead = async (notificationId: number) => {
    try {
      // const notificationApi = new NotificationApi(configStore.apiConfig)
      // await notificationApi.markNotificationAsRead(notificationId)
      
      // 상태 업데이트
      setNotifications(prevNotifications => 
        prevNotifications.map(notification => 
          notification.notificationId === notificationId 
            ? {...notification, readOrNot: true} 
            : notification
        )
      )
      
      // 읽지 않은 알림 개수 갱신
      setUnreadCount(prev => Math.max(0, prev - 1))
    } catch (error) {
      console.error('알림 읽음 처리에 실패했습니다:', error)
    }
  }
  
  // 알림 클릭 핸들러
  const handleNotificationClick = (notification: NotificationDTO) => {
    markAsRead(notification.notificationId)
    
    // 알림 타입에 따른 다른 처리
    switch(notification.type) {
      case NotificationType.CHAT:
        if (notification.chatRoomId) {
          router.push(`/chat/${notification.chatRoomId}`)
        }
        break
      case NotificationType.RECRUITMENT:
        // 밴드 모집 알림 처리
        if (notification.acceptUrl) {
          // 여기서는 단순히 URL로 이동만 하지만, 
          // 실제로는 API 호출이 필요할 수 있음
          router.push(notification.acceptUrl)
        }
        break
      case NotificationType.BAND:
        // 밴드 관련 알림 처리
        router.push('/myband')
        break
      case NotificationType.SYSTEM:
      default:
        // 기본적으로는 아무 동작도 하지 않음
        break
    }
    
    // 드롭다운 닫기
    setShowNotifications(false)
  }
  
  // 알림 수락/거절 처리
  // const handleAction = async (url: string) => {
  //   try {
  //     await fetch(url, {
  //       method: 'POST',
  //       headers: {
  //         'Authorization': `Bearer ${configStore.token || ''}`,
  //         'Content-Type': 'application/json'
  //       }
  //     })
      
  //     // 알림 목록 새로고침
  //     fetchNotifications()
  //   } catch (error) {
  //     console.error('알림 액션 처리 실패:', error)
  //   }
  // }
  
  // 외부 클릭 시 알림 드롭다운 닫기
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (notificationRef.current && !notificationRef.current.contains(event.target as Node)) {
        setShowNotifications(false)
      }
    }
    
    document.addEventListener('mousedown', handleClickOutside)
    return () => {
      document.removeEventListener('mousedown', handleClickOutside)
    }
  }, [])
  
  // 정기적으로 알림 가져오기 (1분마다)
  useEffect(() => {
    // 로그인 상태일 때만 알림을 가져옴
    if (configStore.token) {
      fetchNotifications()
      
      const interval = setInterval(() => {
        fetchNotifications()
      }, 60000)
      
      return () => clearInterval(interval)
    }
  }, [configStore.token])
  
  // 알림 버튼 클릭 핸들러
  const toggleNotifications = () => {
    setShowNotifications(!showNotifications)
  }
  
  // 토큰이 없으면 null 반환 (컴포넌트를 렌더링하지 않음)
  if (!configStore.token) return null
  
  return (
    <div className="notification-container" ref={notificationRef}>
      <div 
        className="notification-icon" 
        onClick={toggleNotifications}
      >
        <Image 
          src={bell}
          alt='bell'
          width={24}
          height={24}
        />
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
          <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
        </svg>
        {unreadCount > 0 && (
          <span className="notification-badge">{unreadCount}</span>
        )}
      </div>
      
      {showNotifications && (
        <div className="notification-dropdown">
          <div className="notification-header">
            <h3>알림</h3>
          </div>
          <div className="notification-list">
            {notifications.length > 0 ? (
              notifications.map(notification => (
                <div 
                  key={notification.notificationId}
                  className={`notification-item ${!notification.readOrNot ? 'unread' : ''}`}
                >
                  <div 
                    className="notification-content"
                    onClick={() => handleNotificationClick(notification)}
                  >
                    {notification.senderProfileUrl && (
                      <div className="notification-avatar">
                        <img 
                          src={notification.senderProfileUrl} 
                          alt={notification.senderNickname || '사용자'} 
                        />
                      </div>
                    )}
                    <div className="notification-message">
                      <p>{notification.message}</p>
                      <small>{new Date(notification.createdAt).toLocaleString()}</small>
                    </div>
                  </div>
                  
                  {/* 수락/거절 버튼 (RECRUITMENT 타입일 경우) */}
                  {notification.type === NotificationType.RECRUITMENT && 
                   notification.acceptUrl && 
                   notification.rejectUrl && (
                    <div className="notification-actions">
                      <button 
                        className="btn-accept"
                        // onClick={() => handleAction(notification.acceptUrl!, notification.notificationId)}
                      >
                        수락
                      </button>
                      <button 
                        className="btn-reject"
                        // onClick={() => handleAction(notification.rejectUrl!, notification.notificationId)}
                      >
                        거절
                      </button>
                    </div>
                  )}
                </div>
              ))
            ) : (
              <div className="notification-empty">
                <p>알림이 없습니다</p>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  )
}

export default NotificationComponent 