import type { LocalOrg } from "../../localorg/utils/localorg";
import type { Post } from "../../post/utils/post";

//用户条件查询的封装
export interface user {
  id?: string;
  ctime?: string;
  etime?: string;
  cuserkey?: string;
  euserkey?: string;
  password?: string;
  type?: string;
  loginname?: string;
  name?: string;
  state?: string;
  note?: string;
  orgid?: string;
  photoid?: string;
  posts?: Array<Post>;
  orgs?: Array<LocalOrg>;
}
