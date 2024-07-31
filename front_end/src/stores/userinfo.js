import { ref } from "vue";
import { defineStore } from "pinia";

export const useUserInfoStore = defineStore(
  "userinfo",
  () => {
    const info = ref({
      id: null,
      username: null,
      nickname: null,
      email: null,
      userPic: null,
    });

    const setInfo = (newInfo) => {
      info.value = newInfo;
    };

    const removeInfo = () => {
      info.value = {};
    };

    return {
      info,
      setInfo,
      removeInfo,
    };
  },
  {
    persist: true, // 持久化存储
  }
);
