import type { Post } from "@/views/system/post/utils/post";

export interface User {
  loginname?: string;
  name?: string;
  type?: string;
  tags?: string[];
  photoid?: string;
  frameMenuNum?: number;
  isAdmin?: boolean;
  posts?:Array<Post>;
}
