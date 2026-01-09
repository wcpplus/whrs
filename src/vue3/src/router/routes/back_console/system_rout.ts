/**name必须等于component 必须等于组件名（注意大小写）*/
const system_rout = [
  {
    path: "home",
    name: "frame_home",
    component: () => import("@/views/frame/frame_home.vue"),
    meta: { title: "首页" },
  },
  {
    path: "localuser",
    name: "localuser_page",
    component: () => import("@/views/system/localuser/localuser_page.vue"),
    meta: { title: "用户管理" },
  },
  {
    path: "localorg",
    name: "localorg_page",
    component: () => import("@/views/system/localorg/localorg_page.vue"),
    meta: { title: "组织机构" },
  },
  {
    path: "authgrade",
    name: "authgrade_page",
     component: () => import("@/views/system/authgrade/authgrade_page.vue"),
    meta: { title: "职级管理" },
  },
  {
    path: "actions",
    name: "actions_page",
    component: () => import("@/views/system/actions/actions_page.vue"),
    meta: { title: "菜单权限设置" },
  },
  {
    path: "post",
    name: "post_page",
    component: () => import("@/views/system/post/post_page.vue"),
    meta: { title: "岗位管理" },
  },
  {
    path: "dicentity",
    name: "dicentity_page",
    component: () => import("@/views/system/dicentity/dicentity_page.vue"),
    meta: { title: "数据字典" },
  },
  {
    path: "parameter",
    name: "parameter_page",
    component: () => import("@/views/system/parameter/parameter_page.vue"),
    meta: { title: "系统参数" },
  },
  {
    path: "eventlog",
    name: "eventlog_page",
    component: () => import("@/views/system/eventlog/eventlog_page.vue"),
    meta: { title: "事件日志" },
  },
  {
    path: "resourcefile",
    name: "resourcefile_page",
    component: () => import("@/views/system/resourcefile/resourcefile_page.vue"),
    meta: { title: "附件管理" },
  },
  {
    path: "onlineuser",
    name: "onlineuser_page",
    component: () => import("@/views/system/onlineuser/onlineuser_page.vue"),
    meta: { title: "在线用户" },
  },
  {
    path: "caches",
    name: "caches_page",
    component: () => import("@/views/system/caches/caches_page.vue"),
    meta: { title: "系统缓存" },
  },
  {
    path: "flow",
    name: "flow_page",
    component: () => import("@/views/demo/flow/flow_page.vue"),
    meta: { title: "流程图" },
  },
  {
    path: "echart",
    name: "echart_page",
    component: () => import("@/views/demo/echart/echart_page.vue"),
    meta: { title: "echart图表" },
  },
  {
    path: "editor",
    name: "editor_math_page",
    component: () => import("@/views/demo/editor/editor_math_page.vue"),
    meta: { title: "echart图表" },
  },
];
export default system_rout;
