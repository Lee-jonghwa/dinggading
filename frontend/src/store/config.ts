// config.ts
'use client'

import { Configuration } from "@generated/configuration"
import { create } from "zustand"

// 토큰을 로컬 스토리지에 저장
const saveTokenToStorage = (token: string) => {
  if (typeof window !== 'undefined') {
    localStorage.setItem('accessToken', token);
  }
}

// 토큰을 로컬 스토리지에서 가져오기
const getTokenFromStorage = (): string | null => {
  if (typeof window !== 'undefined') {
    return localStorage.getItem('accessToken');
  }
  return null;
}

type EnvMode = "DEVELOPMENT_LOCAL" | "PRODUCTION"
const getDefaultBasePath = () : string => {
  const envMode = process.env.NEXT_PUBLIC_ENV_MODE as EnvMode || "DEVELOPMENT_LOCAL"

  let baseUrl = ''
  if (envMode === "PRODUCTION") {
    baseUrl = process.env.NEXT_PUBLIC_API_PROD_URL || "https://j12e107.p.ssafy.io" // 이럼 .env 왜 씀? 
  } else {
    baseUrl = process.env.NEXT_PUBLIC_API_BASE_URL || "localhost:8080"
  }

  // // URL이 http:// 또는 https://로 시작하는지 확인하고, 그렇지 않으면 http://를 추가
  // if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
  //   return `http://${baseUrl}`
  // }

  return baseUrl 

}

// Zustand 스토어 타입 정의 
interface ConfigState {
  apiConfig: Configuration
  token: string | null
  envMode : EnvMode 
  setBaseUrl: (url: string) => void
  setAccessToken: (token: string) => void
  getAccessToken: () => string | null
}

const getCurrentEnvMode = (): EnvMode => {
  return (process.env.NEXT_PUBLIC_ENV_MODE as EnvMode) || "DEVELOPMENT_LOCAL"
}

const initialToken = getTokenFromStorage();
const initialEnvMode = getCurrentEnvMode() 

// Zustand 스토어 생성
export const useConfigStore = create<ConfigState>((set, get) => ({
  // API 설정 초기화
  apiConfig: new Configuration({
    basePath: getDefaultBasePath(),
    accessToken: initialToken || "", // 문자열로 초기화
    baseOptions: {
      headers: {
        'Content-Type': 'application/json'
      }
    }
  }),
  
  token: initialToken,
  envMode : initialEnvMode, 
  
  setBaseUrl: (url: string) => {
    // URL 형식 확인 및 수정
    let validUrl = url;
    if (!validUrl.startsWith('http://') && !validUrl.startsWith('https://')) {
      validUrl = `https://${validUrl}`;
    }
    
    console.log("Setting baseUrl to:", validUrl);
    
    set((state) => ({
      apiConfig: new Configuration({
        ...state.apiConfig,
        basePath: validUrl,
        accessToken: state.token || "",
      })
    }));
  },
  
  // 액세스 토큰 설정 함수
  setAccessToken: (token: string) => {
    saveTokenToStorage(token);
    
    console.log("Setting accessToken:", token);
    
    set((state) => ({
      token: token, // 토큰 상태 업데이트 추가
      apiConfig: new Configuration({
        ...state.apiConfig,
        accessToken: token,
      }),
    }));
  },
  
  // 액세스 토큰 가져오기 함수
  getAccessToken: () => {
    return get().token;
  },
}));