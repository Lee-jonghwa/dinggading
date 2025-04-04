'use client'

import { Configuration } from "@generated/configuration"
import { create } from "zustand"

// Zustand 스토어 타입 정의
interface ConfigState {
  apiConfig: Configuration
  setAccessToken: (token: string) => void
}

// Zustand 스토어 생성
export const useConfigStore = create<ConfigState>((set) => ({
  // API 설정 초기화
  apiConfig: new Configuration({
    basePath: "http://localhost:8080",
    accessToken: "asdf", // 초기 토큰값
    baseOptions: {
      headers: {
        'Content-Type': 'application/json'
      }
    }
  }),
  
  // 액세스 토큰 설정 함수
  setAccessToken: (token: string) => 
    set((state) => ({
      apiConfig: new Configuration({
        ...state.apiConfig,
        accessToken: token,
      }),
    })),
}))