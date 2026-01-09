import { defineStore } from "pinia";
/**
 * 用户登录后要去往的页面地址
 */
export const useUserConfigStore = defineStore("UserConfig", {
  state: () => ({
    goUrlKey: "finally-url",
  }),
  getters: {
    getGotoUrl(): string | null {
      const finallyUrl = localStorage.getItem(this.goUrlKey);
      localStorage.removeItem(this.goUrlKey);
      return finallyUrl;
    },
  },
  actions: {
    setGoUrl(url: string) {
      localStorage.setItem(this.goUrlKey, url);
    },
    clearGoUrl() {
      localStorage.removeItem(this.goUrlKey);
    },
  },
});
