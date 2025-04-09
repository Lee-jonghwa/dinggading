'use client'

import { create } from "zustand"
import { SongByInstrumentApi, SongByInstrumentApiGetSongByInstrumentUrlBySongIdAndInstrumentRequest } from "../../generated/api"
import { SongByInstrumentDTO } from "../../generated/model/song-by-instrument-dto"
import { Instrument } from "../../generated/model/instrument"
import { useConfigStore } from "./config"

interface SongByInstrumentState {
  songByInstrument: SongByInstrumentDTO | null
  songUrl: string | null
  loading: boolean
  error: string | null
  
  // 악기별 곡 상세 정보 조회
  fetchSongByInstrument: (songByInstrumentId: number) => Promise<void>
  
  // 악기별 곡 URL 조회
  fetchSongByInstrumentUrl: (songByInstrumentId: number) => Promise<void>

  // 악기, 티어별 곡 URL 조회 
  fetchSongByInstrumentUrlBySongIdAndInstrument: (songId: number, instrument: string) => Promise<void>
}

// Zustand store 생성
export const useSongByInstrumentStore = create<SongByInstrumentState>((set) => ({
  songByInstrument: null,
  songUrl: null,
  loading: false,
  error: null,
  
  fetchSongByInstrument: async (songByInstrumentId: number) => {
    set({ loading: true, error: null })
    try {
      const apiConfig = useConfigStore.getState().apiConfig
      const songByInstrumentApi = new SongByInstrumentApi(apiConfig)
      
      const response = await songByInstrumentApi.getSongByInstrument({ songByInstrumentId })
      
      set({
        songByInstrument: response.data,
        loading: false,
        error: null
      })
    } catch (error) {
      console.error("Error fetching song by instrument:", error)
      set({
        loading: false,
        error: error instanceof Error ? error.message : "알 수 없는 오류가 발생했습니다."
      })
    }
  },
  
  fetchSongByInstrumentUrlBySongIdAndInstrument: async (songId: number, instrument: string) => {
    set({ loading: true, error: null })
    try {
      const apiConfig = useConfigStore.getState().apiConfig 
      const songByInstrumentApi = new SongByInstrumentApi(apiConfig)
      
      // 문자열을 Instrument enum으로 변환
      const instrumentEnum = instrument.toUpperCase() as Instrument
      
      const params: SongByInstrumentApiGetSongByInstrumentUrlBySongIdAndInstrumentRequest = {
        songId,
        instrument: instrumentEnum
      }
      
      const response = await songByInstrumentApi.getSongByInstrumentUrlBySongIdAndInstrument(params)

      set({
        songUrl: response.data.songByInstrumentUrl,
        loading: false,
        error: null
      })
    } catch (error) {
      console.log("Error fetching song by instrument, songid : ", error)
      set({
        loading: false,
        error: error instanceof Error ? error.message : "알 수 없는 오류가 발생했습니다."
      })
    }
  }, 

  fetchSongByInstrumentUrl: async (songByInstrumentId: number) => {
    set({ loading: true, error: null })
    try {
      const apiConfig = useConfigStore.getState().apiConfig
      const songByInstrumentApi = new SongByInstrumentApi(apiConfig)
      
      const response = await songByInstrumentApi.requestSongByInstrumentUrl({ songByInstrumentId })
      
      set({
        songUrl: response.data.songByInstrumentUrl,
        loading: false,
        error: null
      })
    } catch (error) {
      console.error("Error fetching song by instrument URL:", error)
      set({
        loading: false,
        error: error instanceof Error ? error.message : "알 수 없는 오류가 발생했습니다."
      })
    }
  }
}))