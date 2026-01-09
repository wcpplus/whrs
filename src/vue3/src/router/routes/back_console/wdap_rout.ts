/**name必须等于component 必须等于组件名（注意大小写）*/
const wdap_rout = [
  {
    path: "wdapflow",
    name: "wdapflow_page",
    component: () =>
      import("@/views/back_console/wdap/wdapflow/wdapflow_page.vue"),
    meta: { title: "转换流程" },
  },
  {
    path: "wdapconvertor",
    name: "wdapconvertor_page",
    component: () =>
      import("@/views/back_console/wdap/wdapconvertor/wdapconvertor_page.vue"),
    meta: { title: "文件转换器" },
  },
  {
    path: "wdaptask",
    name: "wdaptask_page",
    component: () =>
      import("@/views/back_console/wdap/wdaptask/wdaptask_page.vue"),
    meta: { title: "转换任务" },
  },
  {
    path: "wdapextendfile",
    name: "wdapextendfile_page",
    component: () =>
      import(
        "@/views/back_console/wdap/wdapextendfile/wdapextendfile_page.vue"
      ),
    meta: { title: "扩展文件" },
  },
  {
    path: "soffice",
    name: "office_service_page",
    component: () =>
      import("@/views/back_console/wdap/soffice/office_service_page.vue"),
    meta: { title: "office服务状态" },
  },
];
export default wdap_rout;
