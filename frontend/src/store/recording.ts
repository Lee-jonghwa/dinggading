'use client'

import { create } from 'zustand';
import { persist } from 'zustand/middleware';

// 녹음 타입 정의
type RecordingType = {
  id: string;
  url: string;
  blob: Blob;
  timestamp: Date;
  songId: string;
};

// 스토어 상태 타입
interface RecordingState {
  isRecording: boolean;
  currentRecording: RecordingType | null;
  recordings: RecordingType[];
  mediaRecorder: MediaRecorder | null;
  audioChunks: Blob[];
  currentSongId: string;
  
  // 액션
  startRecording: (songId: string) => Promise<void>;
  stopRecording: () => Promise<RecordingType | null>;
  deleteRecording: (id: string) => void;
  clearRecordings: () => void;
}

// Zustand 스토어 생성
export const useRecordingStore = create<RecordingState>()(
  // persist 미들웨어 적용 (선택적)
  // 참고: Blob 객체는 직렬화되지 않으므로 필요한 속성만 저장
  persist(
    (set, get) => ({
      isRecording: false,
      currentRecording: null,
      recordings: [],
      mediaRecorder: null,
      audioChunks: [],
      currentSongId: '',
      
      // 녹음 시작
      startRecording: async (songId) => {
        if (get().isRecording) return;
        
        try {
          const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
          const mediaRecorder = new MediaRecorder(stream);
          
          // 녹음 데이터 수집 설정
          mediaRecorder.ondataavailable = (event) => {
            if (event.data.size > 0) {
              set((state) => ({
                audioChunks: [...state.audioChunks, event.data]
              }));
            }
          };
          
          // 녹음 시작
          mediaRecorder.start(100);
          
          set({
            isRecording: true,
            mediaRecorder,
            audioChunks: [],
            currentSongId: songId
          });
        } catch (error) {
          console.error('마이크 접근 오류:', error);
        }
      },
      
      // 녹음 중지
      stopRecording: async () => {
        const { isRecording, mediaRecorder, audioChunks, currentSongId } = get();
        
        if (!isRecording || !mediaRecorder) return null;
        
        return new Promise((resolve) => {
          mediaRecorder.onstop = () => {
            // 오디오 블롭 생성
            const audioBlob = new Blob(audioChunks, { type: 'audio/webm' });
            const audioUrl = URL.createObjectURL(audioBlob);
            
            // 새 녹음 객체
            const newRecording: RecordingType = {
              id: Date.now().toString(),
              url: audioUrl,
              blob: audioBlob,
              timestamp: new Date(),
              songId: currentSongId,
            };
            
            // 상태 업데이트
            set((state) => ({
              isRecording: false,
              mediaRecorder: null,
              audioChunks: [],
              currentRecording: newRecording,
              recordings: [...state.recordings, newRecording]
            }));
            
            // 마이크 스트림 중지
            mediaRecorder.stream.getTracks().forEach(track => track.stop());
            
            resolve(newRecording);
          };
          
          mediaRecorder.stop();
        });
      },
      
      // 녹음 삭제
      deleteRecording: (id) => {
        const { recordings, currentRecording } = get();
        
        const recordingToDelete = recordings.find(rec => rec.id === id);
        
        if (recordingToDelete) {
          // URL 객체 해제
          URL.revokeObjectURL(recordingToDelete.url);
          
          set({
            recordings: recordings.filter(rec => rec.id !== id),
            currentRecording: currentRecording?.id === id ? null : currentRecording
          });
        }
      },
      
      // 모든 녹음 삭제
      clearRecordings: () => {
        const { recordings } = get();
        
        // 모든 URL 객체 해제
        recordings.forEach(rec => URL.revokeObjectURL(rec.url));
        
        set({
          recordings: [],
          currentRecording: null
        });
      }
    }),
    {
      name: 'recording-storage',
      partialize: (state) => ({
        isRecording: state.isRecording, // 직렬화할 수 있는 상태만 포함
        mediaRecorder: state.mediaRecorder, // 직렬화할 수 있는 상태만 포함
        audioChunks: state.audioChunks, // 직렬화할 수 있는 상태만 포함
        currentSongId: state.currentSongId // 직렬화할 수 있는 상태만 포함
        // recordings와 currentRecording은 제외
      })
    }
  )
);

// 컴포넌트 언마운트 시 정리 유틸리티 함수
export function cleanupRecordingResources() {
  const { isRecording, mediaRecorder, recordings } = useRecordingStore.getState();
  
  // 녹음 중인 경우 중지
  if (isRecording && mediaRecorder) {
    mediaRecorder.stream.getTracks().forEach(track => track.stop());
  }
  
  // URL 객체 해제
  recordings.forEach(rec => URL.revokeObjectURL(rec.url));
}