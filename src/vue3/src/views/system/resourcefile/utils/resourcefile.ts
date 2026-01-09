//用户条件查询的封装
export interface ResourceFile {
  id?: string;
  ctime?: string;
  cuserkey?: string;
  state?: string;
  note?: string;
  exname?: string;
  relativepath?: string;
  filename?: string;
  title?: string;
  filesize?: number;
  resourcekey?: string;
  appid?: string;
  fullpath: string;
}
