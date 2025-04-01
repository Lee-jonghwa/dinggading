import { create } from "zustand";

interface TierState {
  userTier : string
  setUserTier : (tier : string) => void
}

export const useTierStore = create<TierState>((set) => ({
  userTier : "SILVER", // 임시
  setUserTier : (tier) => set({ userTier : tier })
}))