import { NotificationApi } from "@generated/api";
import { create } from "zustand";
import { useConfigStore } from "./config";

export interface Notice {
  notificationId: number;
  chatRoomId?: string;
  messageId?: string;
  senderId: string;
  senderNickname?: string;
  senderProfileUrl?: string;
  receiverId: string;
  message: string;
  readOrNot: boolean;
  type: "CHAT" | "RECRUITMENT" | "BAND" | "SYSTEM";
  acceptUrl?: string;
  rejectUrl?: string;
  createdAt: string;
}

interface NotificationState {
  loading : boolean 
  error : string | null 
  notices : Notice[] 
  fetchNotification : () => void 
}

export const useNotificationStore = create<NotificationState>((set) => ({
  loading : false , 
  error : null, 
  notices : [], 
  fetchNotification : async () => {
    set({ loading : true, error : null })

    try {
      const configStore = useConfigStore.getState() 
      const apiConfig = configStore.apiConfig

      const notificationApi = new NotificationApi(apiConfig)

      // const params : 

      // const response = await notificationApi.getNotifications(0, 10)
      const response = await notificationApi.getNotifications()
      console.log("notification.ts fetchNotification response : ", response)

    } catch (error) {
      console.log("notification.ts fetchNotification error : ", error)
    }
  }

}))