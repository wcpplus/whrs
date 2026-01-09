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
 * 格式化文件大小：
 *
 * 用于el-table-column 的 :formatter="dic.formatFileSize" 转义数据列表数据值
 * @param _row 行数据
 * @param _column 列信息
 * @param cellValue 文件大小（字节）
 * @returns 格式化的文件大小字符串
 */
const formatFileSize = (_row: never, _column: never, cellValue: number): string => {
  const units = ['B', 'KB', 'MB', 'GB'];
  let unitIndex = 0;
  let size = cellValue;

  // Determine the appropriate unit
  while (size >= 1024 && unitIndex < units.length - 1) {
      size /= 1024;
      unitIndex++;
  }

  // Format the size to two decimal places and append the unit
  return `${size.toFixed(2)} ${units[unitIndex]}`;
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
export default { formatSTATE, formatTime,formatFileSize };
