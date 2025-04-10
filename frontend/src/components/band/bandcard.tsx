// src/components/band/bandcard.tsx

import React from 'react';
import styles from './bandcard.module.css'; // BandCard 전용 CSS 모듈 import
import { InstrumentAvailabilityDTO } from '@generated/model';


// 악기 키를 한글 이름으로 매핑 (소문자 기준)
const instrumentMap: { [key: string]: string } = {
  vocal: '🎤 보컬', // 아이콘 예시
  guitar: '🎸 기타',
  bass: ' bassist 베이스', // 예시 아이콘 변경
  drum: '🥁 드럼',
  // 필요시 다른 악기 추가
};

// BandCard 컴포넌트에 필요한 속성만 정의한 인터페이스
interface BandCardProps {
  name: string;
  sigun?: string;
  profileUrl?: string;
  tags?: string;
  maxSize?: number;
  jobOpening?: boolean;
  memberCount?: number;
  instrumentAvailability?: InstrumentAvailabilityDTO;
}

const BandCard: React.FC<BandCardProps> = ({
  name,
  sigun,
  profileUrl,
  tags,
  maxSize,
  jobOpening,
  memberCount, // 현재 사용 안 함 (null 가능성)
  instrumentAvailability,
  // onClick,
  // isSelected,
}) => {
  // 모집 중인 악기 목록 생성
  const availableInstruments = instrumentAvailability
    ? Object.entries(instrumentAvailability)
        .filter(([key, value]) => {
          const lowerKey = key.toLowerCase();
          return value === true && instrumentMap[lowerKey];
        })
        .map(([key]) => instrumentMap[key.toLowerCase()])
        .filter((value, index, self) => self.indexOf(value) === index)
    : [];
  // 태그 문자열을 배열로 변환 (null 처리 포함)
  const tagList = tags ? tags.split(',').map(tag => tag.trim()).filter(tag => tag) : [];

  return (
    <div className={`${styles.card} ${/*isSelected ? styles.selected : ''*/ ''}`} /*onClick={onClick}*/>
      <div className={styles.imageContainer}>
        {profileUrl ? (
          <img src={profileUrl} alt={`${name} profile`} className={styles.profileImage} />
        ) : (
          <div className={styles.placeholderImage}>No Image</div> // 대체 이미지
        )}

        {/* 좌측 상단 정보 */}
        <div className={styles.topLeftInfo}>
          {/* 태그 표시 */}
          {tagList.length > 0 && (
            <div className={styles.tags}>
              {tagList.slice(0, 3).map((tag, index) => ( // 최대 3개 태그 표시 예시
                <span key={index} className={styles.tag}>#{tag}</span>
              ))}
            </div>
          )}
           {/* 인원 및 모집 상태 */}
           <div className={styles.status}>
             <span className={styles.sizeInfo}>최대 {maxSize}명</span>
             <span className={styles.sizeInfo}>현재 {memberCount}명</span>
             <span className={`${styles.jobStatus} ${jobOpening ? styles.open : styles.closed}`}>
               {jobOpening ? '모집중' : '모집마감'}
             </span>
           </div>
        </div>

        {/* 좌측 하단 정보 (모집 악기) */}
        {availableInstruments.length > 0 && (
            <div className={styles.bottomLeftInfo}>
                <span className={styles.instrumentTitle}>모집악기:</span>
                <div className={styles.instruments}>
                {availableInstruments.map((instrument, index) => (
                    <span key={index} className={styles.instrument}>{instrument}</span>
                ))}
                </div>
            </div>
        )}
      </div>

      <div className={styles.cardContent}>
        <h3 className={styles.bandName}>{name}</h3>
        <p className={styles.region}>{sigun}</p>
      </div>
    </div>
  );
};

export default BandCard;