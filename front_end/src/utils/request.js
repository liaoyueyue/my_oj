import axios from "axios";
import { ElMessage } from "element-plus";
import { useTokenStore } from "@/stores/token";
import router from "@/router";

const baseURL = "/api";
const axiosInstance = axios.create({ baseURL });

// 添加请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    let tokenStore = useTokenStore();
    if (tokenStore.token) {
      config.headers.Authorization = tokenStore.token;
    }
    return config;
  },
  (err) => {
    Promise.reject(err);
  }
);

// 添加响应拦截器
axiosInstance.interceptors.response.use(
  (result) => {
    if (result.data.code === 0) {
      return result.data;
    }
    // 操作失败
    ElMessage.error(result.data.msg ? result.data.msg : "服务异常");
    return Promise.reject(err);
  },
  (err) => {
    // 判断响应状态码,如果为401,则证明未登录,提示请登录,并跳转到登录页面
    if (err.response.status === 401) {
      router.push("/login");
      ElMessage.error("请先登录");
    } else {
      ElMessage.error("服务异常");
    }
    return Promise.reject(err); // 异步状态转化为失败的状态
  }
);

export default axiosInstance;
