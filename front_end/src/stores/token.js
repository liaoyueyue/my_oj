// 定义store
import { ref } from "vue";
import { defineStore } from "pinia";

/**
 * definestore参数描述:
 * 第一个参数:给状态起名，具有唯一性
 * 第二个参数：函数，可以定义该状态中拥有的内容
 * 返回值: 返回的是一个的数，将来可以调用该函数，得到第二个参数中返回的内容
 */
export const useTokenStore = defineStore(
  "token",
  () => {
    // 定义状态的内容
    // 1.响应式变量
    const token = ref("");

    // 2.定义一个函数，修改token的值
    const setToken = (newToken) => {
      token.value = newToken;
    };

    // 3.函数，移除token的值
    const removeToken = () => {
      token.value = "";
    };

    return {
      token,
      setToken,
      removeToken,
    };
  },
  {
    persist: true, // 持久化存储
  }
);
