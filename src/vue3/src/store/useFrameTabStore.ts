/**用于存储和管理框架当前打开页面的缓存 */
import { defineStore } from "pinia";

export const useFrameTabStore = defineStore("frameTabStore", {
  state: () => ({
    tabs: [
      {
        path: "/system/home",
        title: "首页",
        name: "frame_home",
        isCloase: false,
      },
    ] as FrameTab[],
    activeName: "/" as string | null,
  }),
  actions: {
    addTab(tab: FrameTab) {
      if (!this.tabs.find((t) => t.path === tab.path)) {
        if (this.tabs.length >= 5) {
          this.removeTab(this.tabs[1].name);
        }
        this.tabs.push(tab);
      }
      this.setActiveTab(tab.name);
    },
    removeTab(name: string) {
      this.tabs = this.tabs.filter((tab) => tab.name !== name);
      if (this.activeName === name) {
        this.setActiveTab(this.tabs[this.tabs.length - 1]?.path || "/");
      }
    },
    setActiveTab(name: string) {
      this.activeName = name;
    },
  },
});
