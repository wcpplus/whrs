import farmFileUnitTools from "@/hook/farmFileUnitTools";
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
    "0": "临时",
    "1": "持久",
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
 * import dic from './utils/dictinarys'
 * 转义 用于el-table-column 的 :formatter="dic.formatFileUnit" 转义数据列表数据值
 * 格式化时间
 * @param _row
 * @param _column
 * @param cellValue
 * @returns
 */
const formatFileUnit = (_row: never, _column: never, cellValue: number) => {
  return farmFileUnitTools.getFileSizeTitle(cellValue);
};

export default { formatSTATE, formatTime, formatFileUnit };
