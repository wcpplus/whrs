import type { DataRule } from "./DataRule";

//用户条件查询的封装
export interface DataQuery {
  page?: number;
  pageSize?: number;
  rules?: Array<DataRule>;
  sorts?: Array<{ field: string; sort_type: string }>;
}
