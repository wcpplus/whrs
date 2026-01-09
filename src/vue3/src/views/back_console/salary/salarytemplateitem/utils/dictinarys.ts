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
    "0": "系统变量",
    "1": "手动更新",
    "2": "每月更新",
    "3": "中间变量",
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
export default { formatSTATE, formatTime };
