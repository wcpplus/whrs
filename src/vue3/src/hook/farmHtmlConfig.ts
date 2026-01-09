/**
 * 获得参赛值
 * @param html - 大段的HTML字符串
 * @returns 包含标题名称、标签、层级和顺序号的数组
 */
const getStringVal = (key: string): string | null => {
  const appElement = document.getElementById("app");
  return appElement?.getAttribute(key) ?? null;
};
/**
 *
 * @param eqModel  local：表示通过前端缓存存储JWT
 * @returns
 */

const isJwtModel = (eqModel: string): boolean => {
  const appElement = document.getElementById("app");
  const model = appElement?.getAttribute("data-config-auth-jwt-model") ?? null;
  if (!model) {
    alert("data-config-auth-jwt-model is nudefind");
    return false;
  }
  if (model === eqModel) {
    return true;
  } else {
    return false;
  }
};
/**
 * 是否是宽屏模式
 * @returns
 */

const isWideModel = (): boolean => {
  const appElement = document.getElementById("app");
  const model = appElement?.getAttribute("data-config-wide-model") ?? null;
  if (!model) {
    return false;
  }
  if (model === "true") {
    return true;
  } else {
    return false;
  }
};
const isWaterShow = (): boolean => {
  const appElement = document.getElementById("app");
  const model = appElement?.getAttribute("data-config-water-able") ?? null;
  if (!model) {
    return false;
  }
  if (model === "true") {
    return true;
  } else {
    return false;
  }
};
export default {
  getStringVal,
  isJwtModel,
  isWideModel,
  isWaterShow,
};
