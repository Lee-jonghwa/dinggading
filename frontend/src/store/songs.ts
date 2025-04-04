'use client'

import { create } from "zustand"
import { GetSongsByTierTierEnum, SongApi, SongApiGetSongsByTierRequest } from "@generated/api"
import { useConfigStore } from "./config"

// 타입 정의
interface Song {
  songId: number
  title: string
  artist: string
  youtubeUrl: string
  description?: string  // description을 옵셔널 필드로 변경
  audioUrl?: string 
  waveforUrl?: string 
}

interface SongsState {
  songs: Song[]
  loading: boolean
  error: string | null
  fetchSongs: (tier: string) => Promise<void>
}

// Zustand store 생성
export const useSongsStore = create<SongsState>((set) => ({
  songs: [],
  loading: false,
  error: null,
  
  fetchSongs: async (tier: string) => {
    set({ loading: true, error: null })
    
    try {
      // configStore에서 apiConfig를 가져오는 방식 수정
      // Zustand store 내부에서는 다른 store의 hook을 직접 사용할 수 없음
      const configStore = useConfigStore.getState()
      const apiConfig = configStore.apiConfig
      
      // SongApi 인스턴스 생성
      const songApi = new SongApi(apiConfig)
      
      // API 요청 파라미터 설정
      const params: SongApiGetSongsByTierRequest = {
        tier: tier as GetSongsByTierTierEnum
      }
      
      // API 호출
      const response = await songApi.getSongsByTier(params)
      console.log("songs.ts getSongsByTier response:", response)
      
      // 타입 호환성을 위해 필요한 필드 변환
      const formattedSongs = response.data.content.map(songDTO => ({
        songId: songDTO.songId,
        title: songDTO.title,
        description: songDTO.description || '',  // undefined인 경우 빈 문자열로 대체
        artist: songDTO.artist,
        youtubeUrl: songDTO.youtubeUrl
      }));
      
      // 성공 시 상태 업데이트
      set({ 
        songs: formattedSongs,
        loading: false,
        error: null
      })
    } catch (error) {
      console.error("songs.ts getSongsByTier error:", error)
      
      // 에러 발생 시 상태 업데이트
      set({ 
        loading: false,
        error: error instanceof Error ? error.message : "Unknown error occurred"
      })
    }
  }
}))