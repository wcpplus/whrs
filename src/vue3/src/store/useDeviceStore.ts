import { defineStore } from "pinia";
/**
 * 判断当前设备类型
 *import { computed } from 'vue';
 *import { useDeviceStore } from '@/store/useDeviceStore';
 *const deviceStore = useDeviceStore();
 *const isMobile = computed(() => { return deviceStore.$state.isMobile });
 */
export const useDeviceStore = defineStore("device", {
  state: () => ({
    isMobile: false,
  }),
  actions: {
    detectDevice() {
      const userAgent = navigator.userAgent || navigator.vendor || "";
      this.isMobile = /android|iphone|ipad|ipod|weixin|mobile/i.test(userAgent);
    },
  },
});
