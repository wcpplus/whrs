//远程响应封装
export interface DataResponse {
  code: number;
  msg?: string;
  data?: unknown;
  version: string;
}
