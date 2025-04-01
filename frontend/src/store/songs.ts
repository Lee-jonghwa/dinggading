import axios from "axios"
import { create } from "zustand"

// instrument , tier 를 정하고 요청하면 백에서 곡 팩을 response해준다. 
// 곡 팩 안에는 title, description, artist가 있다. 
// title 을 기준으로 도전 페이지로 넘기면 되겠다. 


interface Song {
  songId : number
  title : string 
  description : string 
  artist : string
  youtubeUrl : string
}

interface SongsStore {
  songs : Song[], 
  loading : boolean, 
  error : string | null , 
  fetchSongs : (instrument : string, tier : string) => Promise<void> 
}

export const useSongsStore = create<SongsStore>((set) => ({
  songs : [] , 
  loading : false , 
  error : null , 

  // fetchSongs 에서 할 일 : instrument, tier를 알면 그에 맞는 song list를 불러오기. 
  fetchSongs : async (instrument , tier) => {
    console.log("songs.ts/fetchSongs 실행")
    set({loading : true, error : null})
    try {
      const response = await axios.get(`http://localhost:8081/api/songs/by-tier/${tier}`, {
        params : { instrument , tier}, 
        headers : {
          Authorization : `Bearer YOUR_ACCESS_TOKEN`
        }
      })
      console.log("songs.ts/fetchSongs 실행 성공, response : ", response.data.content )
      set({ songs : response.data.content , loading : false })
    } catch (error : unknown) {
      if (axios.isAxiosError(error)) {
        console.log("songs.ts/axiosError : ", error)
        set({
          error : error.message || "데이터를 불러오지 못했습니다.", 
          loading : false
        })
      } else {
        console.log("songs.ts/unknownError : ", error)
        set({
          error : "알 수 없는 오류가 발생했습니다.", 
          loading : false
        })
      }
    }
  }
}))