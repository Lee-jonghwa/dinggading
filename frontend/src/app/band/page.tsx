
// 'use client';

// import { useState, useEffect, useRef } from 'react';
// import { useInfiniteQuery } from '@tanstack/react-query';
// import BandCard from '../../components/band/bandcard';
// import CitySelector from '../../components/band/cityselector';
// import { generateDummyBands, type Band } from '@/utils/dummyData';
// import styles from './styles.module.css';
// import HorizontalScrollWrapper from '@/components/horizontalscrollwrapper';

// // api 연결 전 더미데이터 밴드 확인하는  함수 
// const fetchBands = async ({ pageParam = 1, region = '', searchTerm = '' }) => {
//   return generateDummyBands(pageParam, 8, region, searchTerm);
// };

// const Band = () => {
//   const [region, setRegion] = useState(''); //도시
//   const [searchTerm, setSearchTerm] = useState(''); // 검색어 입력 상태
//   const [openIndex, setOpenIndex] = useState<number | null>(null); //펼쳐진 밴드 카드 인덱스

//   const scrollContainerRef = useRef<HTMLDivElement>(null); //스크롤 관련 div를 직접 조작하기 위한 참조(ref)
//   const scrollWrapperRef = useRef<HTMLDivElement>(null); //스크롤 관련 div를 직접 조작하기 위한 참조(ref)


//   // react-query 무한 스크롤 처리 
//   const {
//     data,
//     fetchNextPage,
//     hasNextPage,
//     isFetchingNextPage,
//   } = useInfiniteQuery({
//     queryKey: ['bands', region, searchTerm],
//     queryFn: ({ pageParam }) => fetchBands({ pageParam, region, searchTerm }),
//     getNextPageParam: (lastPage) => lastPage.nextPage,
//     initialPageParam: 1,
//   });

//   // 무한 스크롤 트리거 함수
//   const handleScroll = ({ scrollLeft, scrollWidth, clientWidth }: any) => {
//     if (scrollWidth - scrollLeft - clientWidth < 100 && hasNextPage && !isFetchingNextPage) {
//       fetchNextPage();
//     }
//   };

//   // 휠 스크롤 처리 (8개 분량씩 이동)
//     useEffect(() => {
//     // 기존의 HorizontalScrollWrapper 내부 div 참조
//         const scrollContainer = scrollContainerRef.current?.parentElement;
//         if (!scrollContainer) return;

//     // 페이지 크기 계산에 필요한 상수
//     const CARD_WIDTH = 300; // 카드 너비
//     const GAP = 20; // 카드 간격
//     // 함수 정의: 현재 화면에서 한 줄에 몇 개 보여줄 수 있는지 계산
//     const getCardsPerRow = () => {
//         const containerWidth = scrollContainer.clientWidth;
//         return Math.max(1, Math.floor(containerWidth / (CARD_WIDTH + GAP))); // 최소 1개 보장
//     };

//     // 휠 이벤트 핸들러 - 원래 HorizontalScrollWrapper 내의 wheel 이벤트보다 먼저 실행
//     const handleWheel = (e: WheelEvent) => {
//         e.preventDefault();
//         const CARDS_PER_ROW = getCardsPerRow();
//         const PAGE_SCROLL_WIDTH = (CARD_WIDTH + GAP) * CARDS_PER_ROW * 1; // 8개 이동 기준
      
//         const direction = e.deltaY > 0 ? 1 : -1;
      
//         const currentPosition = scrollContainer.scrollLeft;
      
//         // 🔥 휠 방향에 따라 페이지 이동 기준 변경
//         const currentPage = direction > 0
//           ? Math.floor(currentPosition / PAGE_SCROLL_WIDTH)
//           : Math.ceil(currentPosition / PAGE_SCROLL_WIDTH);
      
//         const targetPosition = currentPage * PAGE_SCROLL_WIDTH + direction * PAGE_SCROLL_WIDTH;
      
//         scrollContainer.scrollTo({
//           left: targetPosition,
//           behavior: 'smooth',
//         });
      
//         // 스크롤 이벤트 트리거 (무한 스크롤)
//         setTimeout(() => {
//           const scrollEvent = {
//             scrollLeft: scrollContainer.scrollLeft,
//             scrollWidth: scrollContainer.scrollWidth,
//             clientWidth: scrollContainer.clientWidth,
//           };
//           handleScroll(scrollEvent);
//         }, 300);
//       };

//     // 이벤트 캡처 단계에서 휠 이벤트 처리
//     document.addEventListener('wheel', handleWheel, { capture: true, passive: false });
    
//     return () => {
//       document.removeEventListener('wheel', handleWheel, { capture: true });
//     };
//   }, [handleScroll]);

//   // 스크롤바 스타일링을 위한 사용자 정의 CSS 추가
//   useEffect(() => {
//     // 스타일 요소 생성
//     const styleElement = document.createElement('style');
//     styleElement.textContent = `
//       /* 스크롤바 스타일링 */
//       .${styles['band-container']} > div {
//         scrollbar-width: thin;
//         scrollbar-color: #888 #f1f1f1;
//         padding-bottom: 10px;
//       }
//       .${styles['band-container']} > div::-webkit-scrollbar {
//         height: 8px;
//       }
//       .${styles['band-container']} > div::-webkit-scrollbar-track {
//         background: #f1f1f1;
//         border-radius: 4px;
//       }
//       .${styles['band-container']} > div::-webkit-scrollbar-thumb {
//         background: #888;
//         border-radius: 4px;
//       }
//       .${styles['band-container']} > div::-webkit-scrollbar-thumb:hover {
//         background: #555;
//       }
//     `;
    
//     // 스타일 요소를 문서 헤드에 추가
//     document.head.appendChild(styleElement);
    
//     // 컴포넌트 언마운트 시 스타일 요소 제거
//     return () => {
//       document.head.removeChild(styleElement);
//     };
//   }, [styles]);

//   const filtered = data?.pages.flatMap(page => page.items) || [];

//   return (
//     <div className={styles['band-container']} ref={scrollWrapperRef}>
//       <div className={styles['top-bar']}>
//         <CitySelector selectedCity={region} onSelect={setRegion} />
//         <input
//           type="text"
//           placeholder="밴드 검색"
//           value={searchTerm}
//           onChange={(e) => setSearchTerm(e.target.value)}
//           className={styles['search-input']}
//         />
//       </div>

//       <HorizontalScrollWrapper onScroll={handleScroll}>
//         <div ref={scrollContainerRef} className={styles['band-wrapper']}>
//           {filtered.map((band, idx) => (
//             <div key={band.id} className={styles['band-card-wrapper']}>
//               <BandCard
//                 name={band.name}
//                 region={band.region}
//                 isSelected={openIndex === idx}
//                 onClick={() => setOpenIndex(openIndex === idx ? null : idx)}
//               />
//             </div>
//           ))}
//           {isFetchingNextPage && (
//             <div className={styles['loading-spinner']}>
//               <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"></div>
//             </div>
//           )}
//         </div>
//       </HorizontalScrollWrapper>
//     </div>
//   );
// };

// export default Band;
'use client';

import { useState, useEffect, useRef } from 'react';
import { useInfiniteQuery } from '@tanstack/react-query';
import BandCard from '../../components/band/bandcard';
import CitySelector from '../../components/band/cityselector';
import { generateDummyBands, type Band } from '@/utils/dummyData';
import styles from './styles.module.css';
import HorizontalScrollWrapper from '@/components/horizontalscrollwrapper';

// 더미 데이터 8개씩 불러오는 함수
const fetchBands = async ({ pageParam = 1, region = '', searchTerm = '' }) => {
  return generateDummyBands(pageParam, 8, region, searchTerm);
};

interface scrollInfo {
  scrollLeft: number
  scrollWidth: number
  clientWidth: number
}

export default function Band () {
  const [region, setRegion] = useState(''); // 도시 선택
  const [searchTerm, setSearchTerm] = useState(''); // 검색어 상태
  // const [openIndex, setOpenIndex] = useState<number | null>(null); // 펼쳐진 카드 인덱스
  
  const scrollContainerRef = useRef<HTMLDivElement>(null);
  const scrollWrapperRef = useRef<HTMLDivElement>(null);
  
  // react-query 무한 스크롤
  const {
    data,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useInfiniteQuery({
    queryKey: ['bands', region, searchTerm],
    queryFn: ({ pageParam }) => fetchBands({ pageParam, region, searchTerm }),
    getNextPageParam: (lastPage) => lastPage.nextPage,
    initialPageParam: 1,
  });


  // 무한 스크롤 트리거
  const handleScroll = ({ scrollLeft, scrollWidth, clientWidth }: scrollInfo) => {
    if (scrollWidth - scrollLeft - clientWidth < 100 && hasNextPage && !isFetchingNextPage) {
      fetchNextPage();
    }
  };

  // 휠 스크롤: 가로로 8개 분량씩 이동
  useEffect(() => {
    const scrollContainer = scrollContainerRef.current?.parentElement;
    if (!scrollContainer) return;

    const CARD_WIDTH = 300;
    const GAP = 20;

    const getCardsPerRow = () => {
      const containerWidth = scrollContainer.clientWidth;
      return Math.max(1, Math.floor(containerWidth / (CARD_WIDTH + GAP)));
    };

    const handleWheel = (e: WheelEvent) => {
      e.preventDefault();

      const CARDS_PER_ROW = getCardsPerRow();
      const PAGE_SCROLL_WIDTH = (CARD_WIDTH + GAP) * CARDS_PER_ROW;

      const direction = e.deltaY > 0 ? 1 : -1;
      const currentPosition = scrollContainer.scrollLeft;

      const currentPage = Math.round(currentPosition / PAGE_SCROLL_WIDTH);
      let targetPage = currentPage + direction;

      if (targetPage < 0) targetPage = 0;

      const targetPosition = targetPage * PAGE_SCROLL_WIDTH;

      scrollContainer.scrollTo({
        left: targetPosition,
        behavior: 'smooth',
      });

      // 스크롤 트리거
      setTimeout(() => {
        const scrollEvent = {
          scrollLeft: scrollContainer.scrollLeft,
          scrollWidth: scrollContainer.scrollWidth,
          clientWidth: scrollContainer.clientWidth,
        };
        handleScroll(scrollEvent);
      }, 300);
    };

    document.addEventListener('wheel', handleWheel, { capture: true, passive: false });

    return () => {
      document.removeEventListener('wheel', handleWheel, { capture: true });
    };
  }, [handleScroll]);

  // 스크롤바 스타일 적용
  useEffect(() => {
    const styleElement = document.createElement('style');
    styleElement.textContent = `
      .${styles['band-container']} > div {
        scrollbar-width: thin;
        scrollbar-color: #888 #f1f1f1;
        padding-bottom: 10px;
      }
      .${styles['band-container']} > div::-webkit-scrollbar {
        height: 8px;
      }
      .${styles['band-container']} > div::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 4px;
      }
      .${styles['band-container']} > div::-webkit-scrollbar-thumb {
        background: #888;
        border-radius: 4px;
      }
      .${styles['band-container']} > div::-webkit-scrollbar-thumb:hover {
        background: #555;
      }
    `;
    document.head.appendChild(styleElement);

    return () => {
      document.head.removeChild(styleElement);
    };
  }, [styles]);

  // 전체 밴드 데이터 펼치기
  const filtered = data?.pages.flatMap((page) => page.items) || [];

  return (
    <div className={styles['band-container']} ref={scrollWrapperRef}>
      <div className={styles['top-bar']}>
        <CitySelector selectedCity={region} onSelect={setRegion} />
        <input
          type="text"
          placeholder="밴드 검색"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className={styles['search-input']}
        />
      </div>

      <HorizontalScrollWrapper onScroll={handleScroll}>
        <div ref={scrollContainerRef} className={styles['band-wrapper']}>
          {filtered.map((band) => ( // idx 인자에서 제외 
            <div key={band.id} className={styles['band-card-wrapper']}>
              <BandCard
                name={band.name}
                region={band.region}
                // isSelected={openIndex === idx}
                // onClick={() => setOpenIndex(openIndex === idx ? null : idx)}
              />
            </div>
          ))}
          {isFetchingNextPage && (
            <div className={styles['loading-spinner']}>
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"></div>
            </div>
          )}
        </div>
      </HorizontalScrollWrapper>
    </div>
  );
};

