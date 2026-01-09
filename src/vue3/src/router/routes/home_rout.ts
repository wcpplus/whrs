import system_rout from "./back_console/system_rout";
import wdap_rout from "./back_console/wdap_rout";
import social_rout from "./back_console/social_rout";
import attendance_rout from "./back_console/attendance_rout";
import salary_rout from "./back_console/salary_rout";
/**name必须等于component */
const home_rout = [
  {
    path: "/",
    meta: { title: "根目录" },
    component: { template: "<div></div>" },
  },
  {
    path: "/system",
    redirect: "/system/home",
    name: "frame_page",
    component: () => import("@/views/frame/frame_page.vue"),
    children: [
      ...system_rout,
      ...wdap_rout,
      ...social_rout,
      ...attendance_rout,
      ...salary_rout,
    ],
    meta: { title: "框架" },
  },
  {
    path: "/login",
    name: "login_page",
    component: () => import("@/views/login/login_page.vue"),
    meta: { title: "登陆页面" },
  },
];
export default home_rout;
