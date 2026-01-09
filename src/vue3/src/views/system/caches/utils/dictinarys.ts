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
    "-1": "无限制",
  };
  return dic[cellValue] ||cellValue+"秒";
};


/**
 * import dic from './utils/dictinarys'
 * 转义 用于el-table-column 的 :formatter="dic.formatSTATE" 转义数据列表数据值
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
