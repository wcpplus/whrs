import { createRouter, createWebHistory } from "vue-router";
import home_rout from "@/router/routes/home_rout";
import farmConfig from "@/hook/farmConfig";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [...home_rout],
});

// 动态设置标题
router.beforeEach(async (to, from, next) => {
  let homePath = await farmConfig.loadParaByPromise(
    "farm2.config.sys.home.url"
  );
  const title = to.meta.title;
  if (farmConfig.isMobile()) {
    homePath = "/home";
  }
  if (title) {
    document.title = title as string; // 设置文档标题
  }
  if (to.path === "/") {
    if (homePath && typeof homePath === "string") {
      next(homePath); // 跳转到后台配置的首页
    } else {
      next("/home"); // 继续正常导航
    }
  } else {
    next(); // 继续正常导航
  }
});

export default router;
