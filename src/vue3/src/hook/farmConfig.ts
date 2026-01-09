import { useSystemConfigStore } from "@/store/useSystemConfigStore";

const loadPara = (
  key: string,
  handle: (val: string | number | boolean | null | undefined) => void
): void => {
  const sysConfig = useSystemConfigStore();
  sysConfig.getParameter(key, (value) => {
    handle(value);
  });
};

// 改为返回 Promise，便于 await 使用
const loadParaByPromise = (
  key: string
): Promise<string | number | boolean | null | undefined> => {
  return new Promise((resolve) => {
    const sysConfig = useSystemConfigStore();
    sysConfig.getParameter(key, (value) => {
      resolve(value);
    });
  });
};
const isMobile = (): boolean => {
  // SSR / Node.js 环境兼容
  if (typeof window === "undefined") {
    return false;
  }
  // 获取 UA 字符串（navigator.userAgent 最标准）
  const ua = navigator.userAgent;
  // 检测移动端关键词
  return /android|iphone|ipad|ipod|iemobile|opera mini|mobile|webos|windows phone/i.test(
    ua
  );
};
export default { loadPara, loadParaByPromise, isMobile };
