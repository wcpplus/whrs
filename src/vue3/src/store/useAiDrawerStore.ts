// store/useAiDrawerStore.ts
import { defineStore } from 'pinia';
/**
 * AI 抽屉（AI Assistant Drawer）状态管理 Store
 *
 * 该 Store 用于全局控制右侧 AI 助手抽屉的显示/隐藏状态，
 * 并支持在打开时传递初始消息内容（如用户选中的文本、指令等）。
 * 通过响应式状态驱动 UI，避免父子组件间直接操作 ref，
 * 实现松耦合通信。
 *
 * 目的：避免同时打开多个AI抽屉
 */
export const useAiDrawerStore = defineStore('ai-drawer', {
  state: () => ({
    isOpen: false,
    // 如果需要传递消息，也可以加一个 msg 字段
    openMessage: '',
  }),
  actions: {
    open(msg = '') {
      this.isOpen = true;
      this.openMessage = msg;
    },
    close() {
      this.isOpen = false;
      this.openMessage = '';
    },
  },
});
