'use client'

import { useEffect, useState, useRef } from 'react'
import { useRouter } from 'next/navigation'
import styles from "./notice.module.css"
import Image from "next/image"
import bell from "@/assets/Bell.svg"
import { useAuthStore } from "@/store/auth"
import { useNotificationStore } from "@/store/notification"

// 알림 타입 열거형
export enum NotificationType {
  CHAT = 'CHAT',
  RECRUITMENT = 'RECRUITMENT',
  BAND = 'FOLLOW',
  RANK = 'RANK' // 티어 관련 알림 추가
}

// 알림 정보 인터페이스
export interface NotificationDTO {
  notificationId: number;
  chatRoomId?: string;
  messageId?: string;
  senderId?: string;
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

const Notice = () => {
  const [notifications, setNotifications] = useState<NotificationDTO[]>([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [showNotifications, setShowNotifications] = useState(false);
  const notificationRef = useRef<HTMLDivElement>(null);
  const router = useRouter();
  
  const { accessToken, isLoggedIn } = useAuthStore();
  const { fetchNotification, notifications: storeNotifications } = useNotificationStore();
  
  // SSE 연결 상태를 유지하기 위한 ref
  const eventSourceRef = useRef<EventSource | null>(null);

  // 외부 클릭 시 알림 드롭다운 닫기
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (notificationRef.current && !notificationRef.current.contains(event.target as Node)) {
        setShowNotifications(false);
      }
    };
    
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  // 알림 가져오기 및 SSE 연결 설정
  useEffect(() => {
    if (isLoggedIn && accessToken) {
      // 초기 알림 목록 가져오기
      fetchNotification();
      
      // 알림 소켓 연결 (SSE)
      connectSSE();
      
      return () => {
        // 컴포넌트 언마운트 시 SSE 연결 정리
        if (eventSourceRef.current) {
          eventSourceRef.current.close();
          eventSourceRef.current = null;
        }
      };
    }
  }, [isLoggedIn, accessToken, fetchNotification]);

  // 스토어에서 알림 데이터 업데이트 시 처리
  useEffect(() => {
    if (storeNotifications && storeNotifications.length > 0) {
      setNotifications(storeNotifications);
      
      // 읽지 않은 알림 개수 계산
      const unread = storeNotifications.filter(notification => !notification.readOrNot).length;
      setUnreadCount(unread);
    }
  }, [storeNotifications]);

  // SSE 연결 함수
  const connectSSE = () => {
    if (!isLoggedIn || !accessToken) return;
    
    // 이미 연결된 EventSource가 있다면 닫기
    if (eventSourceRef.current) {
      eventSourceRef.current.close();
    }
    
    // SSE 연결 설정
    const baseUrl = process.env.NEXT_PUBLIC_API_BASE_URL || '';
    const sse = new EventSource(`${baseUrl}/api/notifications/subscribe`, {
      withCredentials: true
    });
    
    sse.onopen = () => {
      console.log("SSE 연결이 열렸습니다");
    };
    
    // 연결 이벤트 리스너
    sse.addEventListener('connect', (event) => {
      console.log("SSE connect 이벤트:", event.data);
    });
    
    // 알림 이벤트 리스너
    sse.addEventListener('notification', (event) => {
      try {
        const notificationData = JSON.parse(event.data) as NotificationDTO;
        console.log("새로운 알림 수신:", notificationData);
        
        // 새 알림을 목록에 추가
        setNotifications(prev => [notificationData, ...prev]);
        
        // 읽지 않은 알림 개수 증가
        setUnreadCount(prev => prev + 1);
        
        // 토스트 알림 표시 등 추가 처리 가능
        showNotificationToast(notificationData);
        
        // 스토어 업데이트
        fetchNotification();
      } catch (e) {
        console.error("알림 데이터 파싱 에러:", e);
      }
    });
    
    // 하트비트 이벤트 리스너
    sse.addEventListener('heartbeat', (event) => {
      console.log("SSE heartbeat:", event.data);
    });
    
    sse.onerror = (error) => {
      console.error("SSE 에러 발생:", error);
      // 에러 발생 시 재연결 시도는 브라우저가 자동으로 처리
    };
    
    // 참조 저장
    eventSourceRef.current = sse;
  };

  // 토스트 알림 표시 함수
  const showNotificationToast = (notification: NotificationDTO) => {
    // 브라우저 알림 권한 요청 및 표시
    if (Notification.permission === "granted") {
      // 알림 타입에 따라 다른 타이틀과 아이콘 설정
      let title = '';
      let icon = '';
      
      switch(notification.type) {
        case NotificationType.CHAT:
          title = '새 메시지가 도착했습니다';
          icon = '/images/chat-icon.png';
          break;
        case NotificationType.RANK:
          title = '티어 알림';
          icon = '/images/rank-icon.png';
          break;
        default:
          title = '새 알림이 있습니다';
          icon = '/images/notification-icon.png';
      }
      
      const notificationOptions = {
        body: notification.message,
        icon: icon
      };
      
      new Notification(title, notificationOptions);
    } else if (Notification.permission !== "denied") {
      Notification.requestPermission();
    }
  };

  // 알림 읽음 처리
  const markAsRead = async (notificationId: number) => {
    try {
      const baseUrl = process.env.NEXT_PUBLIC_API_BASE_URL || '';
      await fetch(`${baseUrl}/api/notifications/${notificationId}/read`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`
        }
      });
      
      // 상태 업데이트
      setNotifications(prevNotifications => 
        prevNotifications.map(notification => 
          notification.notificationId === notificationId 
            ? {...notification, readOrNot: true} 
            : notification
        )
      );
      
      // 읽지 않은 알림 개수 갱신
      setUnreadCount(prev => Math.max(0, prev - 1));
      
      // 스토어 업데이트
      fetchNotification();
    } catch (error) {
      console.error('알림 읽음 처리에 실패했습니다:', error);
    }
  };

  // 알림 클릭 핸들러
  const handleNotificationClick = (notification: NotificationDTO) => {
    markAsRead(notification.notificationId);
    
    // 알림 타입에 따른 다른 처리
    switch(notification.type) {
      case NotificationType.CHAT:
        if (notification.chatRoomId) {
          router.push(`/chat/${notification.chatRoomId}`);
        }
        break;
      case NotificationType.RANK:
        router.push('/tier');
        break;
      case NotificationType.BAND:
        router.push('/myband');
        break;
      default:
        // 기본적으로는 아무 동작도 하지 않음
        break;
    }
    
    // 드롭다운 닫기
    setShowNotifications(false);
  };

  // 모든 알림 읽음 처리
  const markAllAsRead = async () => {
    try {
      const baseUrl = process.env.NEXT_PUBLIC_API_BASE_URL || '';
      await fetch(`${baseUrl}/api/notifications/read-all`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`
        }
      });
      
      // 모든 알림을 읽음 상태로 설정
      setNotifications(prevNotifications => 
        prevNotifications.map(notification => ({...notification, readOrNot: true}))
      );
      
      // 읽지 않은 알림 개수 초기화
      setUnreadCount(0);
      
      // 스토어 업데이트
      fetchNotification();
    } catch (error) {
      console.error('모든 알림 읽음 처리에 실패했습니다:', error);
    }
  };

  // 알림 버튼 클릭 핸들러
  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  // 로그인 상태가 아니면 null 반환
  if (!isLoggedIn) return null;

  return (
    <div className={styles.container} ref={notificationRef}>
      <div 
        className={styles.bellContainer} 
        onClick={toggleNotifications}
      >
        <div className={styles.bellIcon}>
          <Image 
            src={bell}
            alt='bell'
            width={24}
            height={24}
          />
        </div>
        {unreadCount > 0 && (
          <span className={styles.badge}>{unreadCount}</span>
        )}
      </div>
      
      {showNotifications && (
        <div className={styles.dropdown}>
          <div className={styles.header}>
            <h3>알림</h3>
            {unreadCount > 0 && (
              <button 
                className={styles.readAllBtn}
                onClick={markAllAsRead}
              >
                모두 읽음
              </button>
            )}
          </div>
          <div className={styles.list}>
            {notifications.length > 0 ? (
              notifications.map(notification => (
                <div 
                  key={notification.notificationId}
                  className={`${styles.item} ${!notification.readOrNot ? styles.unread : ''}`}
                  onClick={() => handleNotificationClick(notification)}
                >
                  <div className={styles.content}>
                    {notification.senderProfileUrl && (
                      <div className={styles.avatar}>
                        <img 
                          src={notification.senderProfileUrl} 
                          alt={notification.senderNickname || '사용자'} 
                        />
                      </div>
                    )}
                    <div className={styles.message}>
                      <p>{notification.message}</p>
                      <small>{new Date(notification.createdAt).toLocaleString()}</small>
                    </div>
                  </div>
                  
                  {!notification.readOrNot && (
                    <div className={styles.indicator}></div>
                  )}
                </div>
              ))
            ) : (
              <div className={styles.empty}>
                <p>알림이 없습니다</p>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default Notice;
