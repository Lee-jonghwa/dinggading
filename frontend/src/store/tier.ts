import { create } from "zustand";
// import { useConfigStore } from "./config";
// import { MemberRankApi, MemberRankApiGetMemberRanksRequest } from "@generated/api";

interface TierState {
  userTier : string
  setUserTier : (tier : string) => void
}

export const useTierStore = create<TierState>((set) => ({
  userTier : "SILVER", // 임시
  setUserTier : (tier) => set({ userTier : tier }), 

  fetchMyRanks : async () => {
    // const configStore = useConfigStore.getState() 
    // const apiConfig = configStore.apiConfig 

    // const memberRankApi = new MemberRankApi(apiConfig)

    // const params : MemberRankApiGetMemberRanksRequest = {
    //   // memberId : response.
    // }

    // const response = await memberRankApi.getMemberRanks(params)
    // console.log("getMemberRanks response : ", response)

  }



}))