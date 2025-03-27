import { create } from "zustand";

interface TierState {
  userTier : string
}

export const useTierStore = create<TierState>((set) => ({
  userTier : "Silver", // 임시
}))