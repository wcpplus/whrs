//用户条件查询的封装
/**
 *   初始化const result: DataResult<LmsResultCourse> = reactive({ totalSize: 0, totalPage: 0, page: 0, pageSize: 0 });
 */
export interface DataResult<T = unknown> {
  totalSize: number;
  totalPage: number;
  page: number;
  pageSize: number;
  data?: T[];
  sorts?: [{ field: string; sort_type: string }];
  tags?: [{ title: string; knowNum: number }];
  para?: object;//"para": {"yyyymm": "202601"},
  /**
   *DocumentResult时有此属性：LU全文检索，DQ数据库检索
   */
  searchType?: string;
  searchTitle?: string; //当前查询类型描述
}
