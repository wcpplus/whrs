import type { DataQuery } from "@/types/commons/DataQuery";
import type { SortInfo } from "@/types/elementplus/SortInfo";

//执行封装排序条件
const getSortQuery = (data: SortInfo): DataQuery => {
  const query: DataQuery = { sorts: [] };
  if (data.order == "ascending") {
    query.sorts = [{ field: data.prop, sort_type: "ASC" }];
  }
  if (data.order == "descending") {
    query.sorts = [{ field: data.prop, sort_type: "DESC" }];
  }
  return query;
};

/**
 * 14位日期格式化
 * @param dateTimeString
 * @returns
 */
const formatDateTime = (dateTimeString: string): string => {
  // 检查输入是否符合预期格式（14位数字）
  if (typeof dateTimeString !== "string" || !/^\d{14}$/.test(dateTimeString)) {
    return dateTimeString;
  }

  // 分割字符串并构建新的格式
  const year = dateTimeString.slice(0, 4);
  const month = dateTimeString.slice(4, 6);
  const day = dateTimeString.slice(6, 8);
  const hour = dateTimeString.slice(8, 10);
  const minute = dateTimeString.slice(10, 12);

  // 返回格式化后的时间字符串
  return `${year}-${month}-${day} ${hour}:${minute}`;
};

export default { getSortQuery, formatDateTime };
