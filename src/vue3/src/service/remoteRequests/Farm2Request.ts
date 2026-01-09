import type { DataQuery } from "@/types/commons/DataQuery";
import type { DataResult } from "@/types/commons/DataResult";
import type { DataResponse } from "@/types/commons/DataResponse";
import { RequestTypeEnum } from "@/types/commons/RequestTypeEnum";
import type { Ref } from "vue";
import apiClient from "./axiosClient"; // 引入你创建的Axios实例
import type { AxiosProgressEvent, AxiosRequestConfig } from "axios";

//执行条件查询
const search = async (
  url_query: string,
  query: DataQuery,
  isLoading?: Ref<boolean, boolean>
): Promise<DataResult> => {
  try {
    // 发送 POST 请求到 Spring Boot 服务器
    if (isLoading) {
      isLoading.value = true;
    }
    const response = await apiClient.post(url_query, query);
    if (isLoading) {
      setTimeout(function () {
        isLoading.value = false;
      }, 300);
    }
    const farmResponse = response.data as DataResponse;

    // 检查响应状态码是否为 200-299 之外的状态
    if (!response || !response.data) {
      throw new Error("Invalid response from server");
    }
    if (farmResponse.code === 1) {
      return farmResponse.data as DataResult;
    } else {
      throw new Error(farmResponse.msg);
    }
  } catch (error) {
    if (isLoading) {
      isLoading.value = false;
    }
    // 处理 Axios 错误
    throw error;
  }
};

//执行数据新增，修改等
const submit = async (
  requestUrl: string,
  type: RequestTypeEnum,
  isLoading?: Ref<boolean, boolean> | null,
  requestData?: unknown,
  timeout?: number
): Promise<DataResponse> => {
  try {
    if (isLoading) {
      isLoading.value = true;
    }
    const config: AxiosRequestConfig = {
      method: type,
      url: "/" + requestUrl,
      data: requestData,
    };
    if (timeout !== undefined) {
      config.timeout = timeout;
    }
    // 发送 POST 请求到 Spring Boot 服务器
    const response = await apiClient.request(config);

    if (isLoading) {
      isLoading.value = false;
    }
    const farmResponse = response.data as DataResponse;
    // 检查响应状态码是否为 200-299 之外的状态
    if (!response || !response.data) {
      throw new Error("Invalid response from server");
    }
    if (farmResponse.code === 1) {
      return farmResponse;
    } else {
      throw new Error(farmResponse.msg);
    }
  } catch (error) {
    // 处理 Axios 错误
    if (isLoading) {
      isLoading.value = false;
    }
    throw error;
  }
};

// 封装文件上传方法
const uploadFile = async (
  requestUrl: string,
  isLoading: Ref<boolean>,
  file: File,
  onUploadProgress?: (progressPercentage: number) => void
): Promise<DataResponse> => {
  // 设置加载状态为true
  isLoading.value = true;
  try {
    const formData = new FormData();
    formData.append("file", file);
    const response = await apiClient.post(requestUrl, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      onUploadProgress: (progressEvent: AxiosProgressEvent) => {
        if (progressEvent.total && progressEvent.loaded) {
          const progressPercentage = Math.round(
            (progressEvent.loaded * 100) / progressEvent.total
          );
          console.log(`上传进度: ${progressPercentage}%`);
          // 如果提供了进度回调函数，则调用它
          if (onUploadProgress) {
            onUploadProgress(progressPercentage);
          }
        }
      },
      timeout: 0, //不限制超时时间
    });
    const farmResponse = response.data as DataResponse;
    // 检查响应状态码是否为 200-299 之外的状态
    if (!response || !response.data) {
      throw new Error("Invalid response from server");
    }
    if (farmResponse.code === 1) {
      return farmResponse;
    } else {
      throw new Error(farmResponse.msg);
    }
  } catch (error) {
    // 处理 Axios 错误
    isLoading.value = false;
    throw error;
  } finally {
    // 设置加载状态为false
    isLoading.value = false;
  }
};

const getFullPath = (path: string) => {
  return (apiClient.defaults.baseURL ? apiClient.defaults.baseURL : "/") + path;
};

const getBaseUrl = () => {
  return apiClient.defaults.baseURL;
};

const downloadFile = async (
  id: string,
  filename: string,
  startFunc?: () => void,
  completeFunc?: () => void
) => {
  try {
    if (startFunc) {
      startFunc();
    }
    // 发送GET请求以获取文件
    const response = await apiClient.request({
      method: "get",
      url: "api/files/download/" + id, // 替换为你的实际API路径
      responseType: "blob", // 确保接收的是二进制流
      onDownloadProgress: (progressEvent) => {
        const totalLength = progressEvent.total as number;
        if (totalLength !== null) {
          console.log(
            `download progress: ${Math.round(
              (progressEvent.loaded * 100) / totalLength
            )}%`
          );
          // 可以在这里更新UI，显示下载进度
        }
      },
      timeout: 0, //不限制超时时间
    });
    if (completeFunc) {
      completeFunc();
    }
    // 创建Blob对象
    const blob = new Blob([response.data], {
      type: response.headers["content-type"],
    });

    // 创建一个URL指向Blob对象
    const link = document.createElement("a");
    const href = window.URL.createObjectURL(blob);
    link.href = href;
    link.setAttribute("download", filename); // 使用从响应头中提取的文件名
    document.body.appendChild(link);

    // 触发点击事件开始下载
    link.click();

    // 下载完成后移除link元素并释放对象URL
    document.body.removeChild(link);
    window.URL.revokeObjectURL(href); // 释放通过URL.createObjectURL()创建的对象URL
  } catch (error) {
    console.error("下载过程中出现错误:", error);
    throw error; // 重新抛出错误以便调用者处理
  }
};

const downloadReport = async (
  apiUrl: string,
  query: DataQuery,
  filename: string,
  startFunc?: () => void,
  completeFunc?: () => void
) => {
  try {
    if (startFunc) {
      startFunc();
    }
    // 发送GET请求以获取文件
    const response = await apiClient.request({
      method: "post",
      url: apiUrl, // 替换为你的实际API路径
      data: query,
      responseType: "blob", // 确保接收的是二进制流
      onDownloadProgress: (progressEvent) => {
        const totalLength = progressEvent.total as number;
        if (totalLength !== null) {
          console.log(
            `download progress: ${Math.round(
              (progressEvent.loaded * 100) / totalLength
            )}%`
          );
          // 可以在这里更新UI，显示下载进度
        }
      },
      timeout: 0, //不限制超时时间
    });
    if (completeFunc) {
      completeFunc();
    }

    // 创建Blob对象
    const blob = new Blob([response.data], {
      type: response.headers["content-type"],
    });

    // 创建一个URL指向Blob对象
    const link = document.createElement("a");
    const href = window.URL.createObjectURL(blob);
    link.href = href;
    link.setAttribute("download", filename); // 使用从响应头中提取的文件名
    document.body.appendChild(link);

    // 触发点击事件开始下载
    link.click();

    // 下载完成后移除link元素并释放对象URL
    document.body.removeChild(link);
    window.URL.revokeObjectURL(href); // 释放通过URL.createObjectURL()创建的对象URL
  } catch (error) {
    console.error("下载过程中出现错误:", error);
    throw error; // 重新抛出错误以便调用者处理
  }
};
const initBasePathToHtml = (html: string) => {
  const fullPath = getFullPath("");
  html = html.replaceAll("[FARM2_BASEPATH_FLAG]", fullPath);
  return html;
};
export default {
  search,
  initBasePathToHtml,
  submit,
  getFullPath,
  uploadFile,
  downloadFile,
  downloadReport,
  getBaseUrl,
};
