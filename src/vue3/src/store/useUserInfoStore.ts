import farmSessionStorageUtils from "@/hook/farmSessionCacheUtils";
import Farm2Request from "@/service/remoteRequests/Farm2Request";
import type { DataResponse } from "@/types/commons/DataResponse";
import { RequestTypeEnum } from "@/types/commons/RequestTypeEnum";
import type { Menu } from "@/types/system/menu";
import type { User } from "@/types/system/user";
import { defineStore } from "pinia";
import { useSystemConfigStore } from "@/store/useSystemConfigStore";
import farmLocalCacheUtils from "@/hook/farmLocalCacheUtils";
/**
 * 递归检查菜单数组中是否包含指定 menukey
 */
function hasMenuKeyRecursive(menus: Menu[], key: string): boolean {
  for (const menu of menus) {
    if (menu.menukey === key) {
      return true;
    }
    if (menu.children && menu.children.length > 0) {
      if (hasMenuKeyRecursive(menu.children, key)) {
        return true;
      }
    }
  }
  return false;
}
export const useUserInfoStore = defineStore("userInfoStore", {
  state: () => ({
    jwt: "",
    user: {} as User,
    appMenus: [] as Menu[],
    frameMenus: [] as Menu[],
    toLogin: false, // 是否跳转登录页
    lastFreshTime: sessionStorage.getItem("lastFreshTime"), // JWT 刷新时间
    refreshJwtTime: 30, // 分钟
    userKey: "LOCAL_USER",
    logOutKey: "LOGOUT_USERKEY",
  }),
  getters: {
    getJwt(): string {
      if (!this.jwt) this.jwt = sessionStorage.getItem("jwt") || "";
      return this.jwt;
    },
    getLongKey(): string | null {
      return (
        sessionStorage.getItem("longkey") || localStorage.getItem("longkey")
      );
    },
    getLastFreshTime(): Date {
      const timestamp = Number(this.lastFreshTime);
      return isNaN(timestamp) ? new Date(0) : new Date(timestamp);
    },
    isLogin(): boolean {
      return !!this.user.loginname;
    },
  },
  actions: {
    /**
     * 获取当前用户信息（本地缓存优先）
     */
    async getUser(): Promise<User> {
      if (!this.user.loginname) {
        const userinfo = await this.getRemoteUser();
        if (userinfo?.user && userinfo.appMenus) {
          this.user = userinfo.user;
          this.appMenus = userinfo.appMenus;
          this.frameMenus = userinfo.frameMenus || [];
        }
        {
          //处理其他session注销
          const user = farmLocalCacheUtils.get(this.logOutKey);
          if (
            user &&
            (user as User).loginname &&
            (user as User).loginname == this.user.loginname
          ) {
            this.logout();
          }
        }
      }
      return this.user;
    },

    /**
     * 从远程获取用户信息并缓存
     */
    async getRemoteUser(): Promise<{
      appMenus?: Menu[];
      user?: User;
      frameMenus?: Menu[];
    }> {
      const cachedUser = farmSessionStorageUtils.get(this.userKey);
      if (cachedUser) return cachedUser;

      try {
        const res: DataResponse = await Farm2Request.submit(
          "api/current/user",
          RequestTypeEnum.post,
          null,
          {}
        );
        const data = res.data ?? {};
        farmSessionStorageUtils.put(this.userKey, data, 30);
        return data;
      } catch (error) {
        console.error("获取用户失败", error);
        throw error;
      }
    },

    /**
     * 用户登录：设置 JWT 并保存刷新 key
     * @param token jwt||refreshKey
     * @param saveLogin 是否持久化登录
     */
    login(token: string, saveLogin: boolean): void {
      this.clearJwt();
      if (token) {
        const [jwtToken, refreshToken] = token.split("||");

        sessionStorage.setItem("jwt", jwtToken);
        if (saveLogin) {
          localStorage.setItem("longkey", refreshToken);
        } else {
          sessionStorage.setItem("longkey", refreshToken);
        }

        this.refreshLastFreshTime();
        this.toLogin = false;
        this.jwt = jwtToken;
      }
      //取消强制注销
      farmLocalCacheUtils.clear(this.logOutKey);
    },

    /**
     * 刷新 JWT
     * @param token 新的 JWT
     */
    refreshJwt(token: string): void {
      sessionStorage.setItem("jwt", token);
      this.jwt = token;
      this.refreshLastFreshTime();
    },

    setUser(name: string, photoid: string): void {
      this.user.name = name;
      this.user.photoid = photoid;
    },
    async logout() {
      const user: User = await this.getUser();
      this.clearJwt();
      this.clearUser();
      if (user && user.loginname) {
        farmLocalCacheUtils.put(this.logOutKey, user, 60 * 12);
      }
    },

    toLoginpage(): void {
      this.toLogin = true;
    },

    refreshLastFreshTime(): void {
      const now = new Date().getTime().toString();
      this.lastFreshTime = now;
      sessionStorage.setItem("lastFreshTime", now);
    },

    clearJwt(): void {
      sessionStorage.removeItem("jwt");
      sessionStorage.removeItem("longkey");
      localStorage.removeItem("longkey");
      this.clearUser();
    },

    clearUser(): void {
      farmSessionStorageUtils.clear(this.userKey);
      const systemConfig = useSystemConfigStore();
      farmSessionStorageUtils.clear(systemConfig.configKey);
      this.user = {} as User;
      this.appMenus = [];
    },

    /**
     * 检查是否包含指定 app 菜单
     * @param key 菜单 key
     * @param handle 回调函数
     */
    async containAppMenu(
      key: string,
      handle: (isContain: boolean) => void
    ): Promise<void> {
      await this.getUser();
      if (this.user.loginname) {
        const inAppMenus = hasMenuKeyRecursive(this.appMenus, key);
        const inFrameMenus = hasMenuKeyRecursive(this.frameMenus, key);
        const isContain = inAppMenus || inFrameMenus;
        handle(isContain);
      } else {
        handle(false);
      }
    },
  },
});
