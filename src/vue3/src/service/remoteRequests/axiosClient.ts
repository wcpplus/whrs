import axios from "axios";
import { useUserInfoStore } from "@/store/useUserInfoStore";
import type { DataResponse } from "@/types/commons/DataResponse";
import farmHtmlConfig from "@/hook/farmHtmlConfig";

const getBaseUrl = () => {
  const appElement = document.getElementById("app");
  return appElement?.getAttribute("data-config-base-url") ?? "/farm2/";
};

// 创建一个 Axios 实例
const apiClient = axios.create({
  baseURL: getBaseUrl(),
  timeout: 20000,
  withCredentials: true, // 如果需要携带凭证（如Cookies），请确保设置此选项
});

// 添加请求拦截器
apiClient.interceptors.request.use(
  async (config) => {
    const userInfo = useUserInfoStore();

    if (farmHtmlConfig.isJwtModel("local")) {
      //前端存储jwt模式
      // 判断是否刷新JWT
      const jwt = await refreshJwt();
      if (jwt && jwt !== "") {
        userInfo.refreshJwt(jwt as string);
      }
      const token = userInfo.getJwt; // 从Pinia store或其他存储中获取JWT
      if (token) {
        config.headers["token"] = token; // 将JWT添加到请求头
      } else {
        userInfo.toLoginpage();
      }
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 刷新jwt
const refreshJwt = async () => {
  const userInfo = useUserInfoStore();
  // 上次刷新jwt的时间
  const lastData = userInfo.getLastFreshTime;
  if (!(lastData instanceof Date)) {
    throw new Error("lastFreshTime 必须是一个有效的 Date 对象");
  }
  // 计算两个时间之间的差异（以毫秒为单位）
  const differenceInMilliseconds = new Date().getTime() - lastData.getTime();
  // 将毫秒转换为分钟
  const differenceInMinutes = differenceInMilliseconds / (1000 * 60);
  //console.log("JWT已存活" + differenceInMinutes + "分钟");
  // 到达30分或没有jwt时刷新
  if (
    (differenceInMinutes >= userInfo.$state.refreshJwtTime ||
      !userInfo.getJwt) &&
    userInfo.getLongKey
  ) {
    //console.log("刷新jwt" );
    // 刷新jwt
    try {
      // 发送 POST 请求到 Spring Boot 服务器
      const response = await axios
        .create({
          baseURL: getBaseUrl(),
          timeout: 10000,
          withCredentials: false,
        })
        .request({
          method: "post",
          url: "api/login/rekey",
          data: { jwt: userInfo.getJwt, longKey: userInfo.getLongKey },
        });
      const farmResponse = response.data as DataResponse;
      if (!response || !response.data) {
        throw new Error("Invalid response from server");
      }
      if (farmResponse.code === 1) {
        return farmResponse.data;
      } else {
        // 清空JWT
        const errorMsg = farmResponse.msg || "获取新 JWT 失败";
        userInfo.clearJwt();
        console.error(errorMsg);
        window.location.reload();
        throw new Error(errorMsg);
      }
    } catch (error) {
      // 处理 Axios 错误
      throw error;
    }
  } else {
    return null;
  }
};

// 添加响应拦截器（可选）
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response && error.response.status === 401) {
      // 处理未授权的情况，例如重定向到登录页面或刷新Token
      const userInfo = useUserInfoStore();
      userInfo.toLoginpage();
      if (farmHtmlConfig.isJwtModel("local")) {
        userInfo.clearJwt();
      } else {
        userInfo.clearUser();
      }
      return Promise.reject(new Error("请登录系统!"));
    }
    return Promise.reject(error);
  }
);

export default apiClient;
