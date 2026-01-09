import { useUserInfoStore } from "@/store/useUserInfoStore";
import type { User } from "@/types/system/user";

/**获取当前用户：用法
  const cuser: User = reactive({});
  farmUserUtils.loadUser((user: User) => {
    Object.assign(cuser, user);
    cuser.isAdmin = isAdmin;
  });
 * @param handle
 */

const loadUser = (handle: (user: User) => void): void => {
  const userInfo = useUserInfoStore();
  userInfo.getUser().then((user: User) => {
    if (userInfo.isLogin) {
      user.isAdmin = user.type == "3";
      handle(user);
    }
  });
};
const isLogin = (): boolean => {
  const userInfo = useUserInfoStore();
  return userInfo.isLogin;
};

export default { loadUser, isLogin };
