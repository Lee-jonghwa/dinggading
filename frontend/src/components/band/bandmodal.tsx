// src/components/band/BandDetailModal.tsx
"use client";

import React, { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { BandApi } from '@generated/api'; // 생성된 API 클라이언트 및 설정 import
import styles from './bandmodal.module.css'; // 모달 전용 CSS 모듈
import { Configuration } from '@generated/configuration';
import { BandDTO, InstrumentAvailabilityDTO } from '@generated/model';
// 악기 키를 한글 이름으로 매핑 (BandCard와 동일하게 사용)
const instrumentMap: { [key: string]: string } = {
  vocal: '🎤 보컬',
  guitar: '🎸 기타',
  bass: ' bassist 베이스',
  drum: '🥁 드럼',
};

// SNS 아이콘 매핑 (간단 예시)
const snsIconMap: { [key: string]: string } = {
    INSTAGRAM: '📷',
    YOUTUBE: '▶️',
    FACEBOOK: '👍',
    X: '🐦', // 트위터/X
    CUSTOM: '🔗', // 일반 링크
    // 필요에 따라 추가
};


// API 호출 함수
const fetchBandDetail = async (bandId: number, apiConfig: Configuration): Promise<BandDTO> => {
  if (!bandId) {
    throw new Error("Band ID is required");
  }
  const bandApi = new BandApi(apiConfig);
  const response = await bandApi.getBand({ bandId }); // 경로 파라미터 전달 방식 확인 필요 (생성된 코드 기준)
                                                      // 만약 bandApi.getBand(bandId) 형태라면 수정
  if (!response || !response.data) {
      throw new Error("Failed to fetch band details or invalid response");
  }
  console.log("Band Detail API Response:", response.data);
  return response.data; // 실제 데이터 반환
};

interface BandDetailModalProps {
  bandId: number | null; // 선택된 밴드 ID (null이면 모달 닫힘)
  isOpen: boolean;
  onClose: () => void;
  apiConfig: Configuration; // API 호출 위한 설정
}

const BandDetailModal: React.FC<BandDetailModalProps> = ({
  bandId,
  isOpen,
  onClose,
  apiConfig,
}) => {
  // TanStack Query v4 형식으로 수정
  const { data: bandData, isLoading, isError, error } = useQuery({
    queryKey: ['bandDetail', bandId],
    queryFn: () => bandId ? fetchBandDetail(bandId, apiConfig) : null,
    enabled: isOpen && bandId !== null,
    staleTime: 1000 * 60 * 5, // 5분간 신선한 데이터로 간주
    refetchOnWindowFocus: false, // 창 포커스 시 자동 재요청 비활성화
  });
  // 모달 외부 클릭 시 닫기 처리
  const handleOverlayClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) { // 오버레이 자체를 클릭했을 때만
      onClose();
    }
  };

  // ESC 키로 모달 닫기
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        onClose();
      }
    };
    if (isOpen) {
      document.addEventListener('keydown', handleKeyDown);
    } else {
      document.removeEventListener('keydown', handleKeyDown);
    }
    // 컴포넌트 언마운트 또는 isOpen 변경 시 리스너 제거
    return () => {
      document.removeEventListener('keydown', handleKeyDown);
    };
  }, [isOpen, onClose]);


  if (!isOpen || !bandId) {
    return null; // 모달이 열려있지 않으면 아무것도 렌더링하지 않음
  }

  // 모집 중인 악기 목록 생성 (BandCard 로직과 유사)
  const getAvailableInstruments = (availability: InstrumentAvailabilityDTO | undefined): string[] => {
    if (!availability) return [];
    return Object.entries(availability)
      .filter(([key, value]) => {
          const lowerKey = key.toLowerCase();
          return value === true && instrumentMap[lowerKey];
      })
      .map(([key]) => instrumentMap[key.toLowerCase()])
      .filter((value, index, self) => self.indexOf(value) === index);
  };

  // 로딩 및 에러 상태 처리
  let content;
  if (isLoading) {
    content = <div className={styles.loading}>Loading...</div>;
  } else if (isError) {
    content = <div className={styles.error}>Error: {error?.message || '밴드 정보를 불러오는데 실패했습니다.'}</div>;
  } else if (bandData) {
    const availableInstruments = getAvailableInstruments(bandData.instrumentAvailability);
    const tagList = bandData.tags ? bandData.tags.split(',').map(tag => tag.trim()).filter(tag => tag) : [];

    content = (
      <>
        <div className={styles.header}>
            {bandData.profileUrl ? (
                <img src={bandData.profileUrl} alt={`${bandData.name} profile`} className={styles.profileImage} />
            ) : (
                <div className={styles.placeholderImage}></div>
            )}
            <div className={styles.headerInfo}>
                <h2 className={styles.bandName}>{bandData.name}</h2>
                <p className={styles.region}>{bandData.sigun}</p>
                {/* 태그 */}
                {tagList.length > 0 && (
                    <div className={styles.tags}>
                    {tagList.map((tag, index) => (
                        <span key={index} className={styles.tag}>#{tag}</span>
                    ))}
                    </div>
                )}
                 {/* 인원/모집 상태 */}
                <div className={styles.status}>
                    <span className={styles.sizeInfo}>최대 {bandData.maxSize}명</span>
                    {bandData.memberCount !== null && <span className={styles.memberInfo}>(현재 {bandData.memberCount}명)</span>}
                    <span className={`${styles.jobStatus} ${bandData.jobOpening ? styles.open : styles.closed}`}>
                    {bandData.jobOpening ? '모집중' : '모집마감'}
                    </span>
                </div>
            </div>
            <button onClick={onClose} className={styles.closeButton}>&times;</button>
        </div>

        <div className={styles.body}>
            {/* 설명 */}
            <section className={styles.section}>
                <h3>밴드 소개</h3>
                <p className={styles.description}>{bandData.description || '밴드 소개가 없습니다.'}</p>
            </section>

             {/* 모집 악기 */}
             {availableInstruments.length > 0 && (
                <section className={styles.section}>
                    <h3>모집 중인 파트</h3>
                    <div className={styles.instruments}>
                    {availableInstruments.map((inst, index) => (
                        <span key={index} className={styles.instrument}>{inst}</span>
                    ))}
                    </div>
                </section>
            )}

             {/* 현재 멤버 */}
             {bandData.bandMember && bandData.bandMember.length > 0 && (
                <section className={styles.section}>
                    <h3>현재 멤버</h3>
                    <ul className={styles.memberList}>
                    {bandData.bandMember.map((member) => (
                        <li key={member.bandMemberId} className={styles.memberItem}>
                            {/* 멤버 프로필 사진 등 추가 가능 */}
                            <span>{member.instrument}</span> {/* API 응답값 그대로 표시 */}
                            {/* <span>{instrumentMap[member.instrument.toLowerCase()] || member.instrument}</span> */} {/* 한글 변환 시 */}
                        </li>
                    ))}
                    </ul>
                </section>
             )}

             {/* 연락처 / SNS */}
             {bandData.contact && bandData.contact.length > 0 && (
                 <section className={styles.section}>
                     <h3>연락처 / SNS</h3>
                     <ul className={styles.contactList}>
                         {bandData.contact.map((contact) => (
                             <li key={contact.contactId} className={styles.contactItem}>
                                 <a href={contact.url} target="_blank" rel="noopener noreferrer" title={contact.title}>
                                     {snsIconMap[contact.sns] || snsIconMap['CUSTOM']} {contact.title || contact.sns}
                                 </a>
                             </li>
                         ))}
                     </ul>
                 </section>
             )}
        </div>
      </>
    );
  } else {
      content = <div className={styles.error}>밴드 정보를 찾을 수 없습니다.</div>; // 데이터 없는 경우
  }


  return (
    <div className={styles.overlay} onClick={handleOverlayClick}>
      <div className={styles.modal}>
        {content}
      </div>
    </div>
  );
};

export default BandDetailModal;