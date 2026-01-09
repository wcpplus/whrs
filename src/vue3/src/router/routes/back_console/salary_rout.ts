/**name必须等于component 必须等于组件名（注意大小写）*/
const salary_rout = [
  {
    path: "salarytemplate",
    name: "salarytemplate_page",
    component: () =>
      import(
        "@/views/back_console/salary/salarytemplate/salarytemplate_page.vue"
      ),
    meta: { title: "薪酬模板" },
  },
  {
    path: "salaryuser",
    name: "salaryuser_list",
    component: () =>
      import("@/views/back_console/salary/salaryuser/salaryuser_list.vue"),
    meta: { title: "薪酬管理" },
  },
];
export default salary_rout;
