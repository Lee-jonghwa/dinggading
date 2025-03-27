import { create } from "zustand";

interface TierState {
  userTier : string
  setUserTier : (tier : string) => void
}

export const useTierStore = create<TierState>((set) => ({
  userTier : "Silver", // 임시
  setUserTier : (tier) => set({ userTier : tier })
}))