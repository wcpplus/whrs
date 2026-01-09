/**name必须等于component 必须等于组件名（注意大小写）*/
const attendance_rout = [
  {
    path: "attendancesummary",
    name: "attendancesummary_list",
    component: () =>
      import(
        "@/views/back_console/attendance/attendancesummary/attendancesummary_list.vue"
      ),
    meta: { title: "考勤结果" },
  },
];
export default attendance_rout;
