/**name必须等于component 必须等于组件名（注意大小写）*/
const social_rout = [
  {
    path: "usermessage",
    name: "usermessage_page",
    component: () =>
      import("@/views/back_console/social/usermessage/usermessage_page.vue"),
    meta: { title: "用户消息" },
  },
];
export default social_rout;
