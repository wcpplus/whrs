//用户条件查询的封装
export interface TalkMsg {
  msg: string;
  type: "user" | "sys";
  id?: string;
}
