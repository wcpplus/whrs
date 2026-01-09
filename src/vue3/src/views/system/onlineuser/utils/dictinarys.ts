import farmTableUtils from "@/hook/farmTableUtils";
/**
 * 用户状态：
 *
 *  import dic from './utils/dictinarys'
 * 转义 用于el-table-column 的 :formatter="dic.formatSTATE" 转义数据列表数据值
 * @param _row
 * @param _column
 * @param cellValue
 * @returns
 */
const formatSTATE = (_row: never, _column: never, cellValue: string) => {
  const dic: { [key: string]: string } = {
    "0": "禁用",
    "1": "可用",
  };
  return dic[cellValue] || "未知";
};

/**
 * import dic from './utils/dictinarys'
 * 转义 用于el-table-column 的 :formatter="dic.formatTime" 转义数据列表数据值
 * 格式化时间
 * @param _row
 * @param _column
 * @param cellValue
 * @returns
 */
const formatTime = (_row: never, _column: never, cellValue: string) => {
  return farmTableUtils.formatDateTime(cellValue);
};

/**
 * 格式化时间戳为 "年-月-日 时:分:秒" 格式
 * @param _row - 行数据，此处未使用
 * @param _column - 列定义，此处未使用
 * @param cellValue - 单元格中的时间戳值
 * @returns {string} - 格式化后的时间字符串
 */
const formatTimestamp = (_row: never, _column: never, cellValue: string) => {
  if (cellValue === null || cellValue === undefined) {
    return ""; // 或者返回一个默认值或提示信息
  }
  const date = new Date(cellValue);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0"); // 月份从0开始，需要加1
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

export default { formatSTATE, formatTime, formatTimestamp };
