import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { createPinia } from "pinia";
import "element-plus/dist/index.css";
import "element-plus/theme-chalk/dark/css-vars.css";
import "./assets/css/base.css";
import "./assets/css/element_pluss_over.css";
import "./assets/css/left_menu.css";
import "./assets/css/farm2_data.css";
import "./assets/css/farm2_form.css";
import ElementPlus from "element-plus";
import zhCn from "element-plus/es/locale/lang/zh-cn";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import { useDeviceStore } from "./store/useDeviceStore";
import 'katex/dist/katex.min.css';//公式编辑器
const app = createApp(App);
const pinia = createPinia();
app.use(pinia);
app.use(router);
Object.entries(ElementPlusIconsVue).forEach(([name, component]) => {
  app.component(name, component);
});
app.use(ElementPlus, {
  locale: zhCn,
});

// 初始化设备类型检测
useDeviceStore().detectDevice();

// 👇 关键：等路由首次导航完成后再隐藏 loading
router.isReady().then(() => {
  app.mount("#app");
  // 隐藏 loading mask
  const loadingMask = document.getElementById("loading-mask");
  if (loadingMask) {
    loadingMask.style.display = "none";
  }
}).catch(console.error);
