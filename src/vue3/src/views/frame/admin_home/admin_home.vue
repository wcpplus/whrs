<template>
  <div style="padding: 10px 30px;padding-top: 0px;">
    <el-row class="whrs_home_funcs">
      <div class="whrs_home-td" v-if="menusPops.attendanceMng">
        <div class="whrs_title"><el-icon>
            <Calendar />
          </el-icon>本月考勤 </div>
        <div>
          <div class="whrs_item whrs_success" :class="attendanceNum.s_zc > 0 ? 'whrs_success' : ''">
            <div class="whre-item-title "> 正常</div>
            <div class="whre-item-flag"> {{ attendanceNum.s_zc }}人次</div>
          </div>
          <div class="whrs_item " :class="attendanceNum.s_zt > 0 ? 'whrs_danger' : ''">
            <div class="whre-item-title"> 早退</div>
            <div class="whre-item-flag"> {{ attendanceNum.s_zt }}人次</div>
          </div>
          <div class="whrs_item " :class="attendanceNum.s_cd > 0 ? 'whrs_danger' : ''">
            <div class="whre-item-title"> 迟到</div>
            <div class="whre-item-flag"> {{ attendanceNum.s_cd }}人次</div>
          </div>
          <div class="whrs_item " :class="attendanceNum.s_qq > 0 ? 'whrs_danger' : ''">
            <div class="whre-item-title"> 缺勤</div>
            <div class="whre-item-flag"> {{ attendanceNum.s_qq }}人次</div>
          </div>
          <div class="whrs_item " :class="attendanceNum.s_qj > 0 ? 'whrs_success' : ''">
            <div class="whre-item-title"> 请假</div>
            <div class="whre-item-flag"> {{ attendanceNum.s_qj }}人次</div>
          </div>
          <div class="whrs_item " :class="attendanceNum.s_yc > 0 ? 'whrs_success' : ''">
            <div class="whre-item-title"> 远程</div>
            <div class="whre-item-flag"> {{ attendanceNum.s_yc }}人次</div>
          </div>
        </div>
      </div>
      <div class="whrs_home-td" v-if="menusPops.userMng">
        <div class="whrs_title"><el-icon>
            <User />
          </el-icon>本月人资 </div>
        <div class="whrs_flags" style="margin-top:10px;">
          <div class="whrs_flag_item" style="">
            <div class="whrs_flag">{{ userNum.s_all_num }}人</div>
            <div>总人数</div>
          </div>
          <div class="whrs_flag_item whrs_success" style="">
            <div class="whrs_flag">{{ userNum.s_rz_t_m_num }}人</div>
            <div>本月入职</div>
          </div>
          <div class="whrs_flag_item whrs_danger" v-if="false" style="">
            <div class="whrs_flag">34人</div>
            <div>本月离职</div>
          </div>
          <div class="whrs_flag_item" style="">
            <div class="whrs_flag">{{ userNum.s_syq_num }}人</div>
            <div>试用期</div>
          </div>
        </div>
        <div style="height: 300px;">
          <Echarts_pie_rose_type1 :data="[{ value: userNum.s_rz_num, name: '待入职', id: '1' },
          { value: userNum.s_sy_num, name: '试用期', id: '2' },
          { value: userNum.s_zs_num, name: '正式', id: '3' },
          { value: userNum.s_li_num, name: '离职', id: '4' }]" :click-handle="() => { }">
          </Echarts_pie_rose_type1>
        </div>
      </div>
      <div class="whrs_home-td" v-if="menusPops.salaryMng">
        <div class="whrs_title"><el-icon>
            <Money />
          </el-icon>本月薪酬 </div>
        <div>
          <div class="whrs_item">
            <div class="whre-item-title "> 本月薪酬计算状态
              <el-progress :percentage="(userNum?.s_all_num > 0
                ? Math.min(100, Math.max(0, (salaryNum?.finish_salary || 0) * 100 / userNum.s_all_num))
                : 0
              )
                " />
            </div>
          </div>
          <div class="whrs_item ">
            <div class="whre-item-title"> <el-icon>
                <Money />
              </el-icon>上月薪酬总额</div>
            <div class="whre-item-flag" style="font-size: 32px; color: #666666;"> {{ salaryNum.last_all_Salary }}元</div>
          </div>
          <div style="height: 230px;">
            <Echarts_line title="最近6个月薪酬发放" :data="salaryArray" v-if="salaryArray.length > 0"></Echarts_line>
          </div>
        </div>
      </div>
    </el-row>
  </div>
</template>
<script setup lang="ts">
import farmUserUtils from '@/hook/farmUserUtils';
import type { User } from '@/types/system/user';
import Echarts_line from '@/views/chart/line/echarts_line.vue';
import Echarts_pie_rose_type1 from '@/views/chart/pie/echarts_pie_rose_type1.vue';
import { computed, onMounted, reactive, ref } from 'vue';
import { useUserInfoStore } from "@/store/useUserInfoStore";
import farmClientUtils from '@/hook/farmClientUtils';
const userInfo = useUserInfoStore();
const items = ref<Array<string>>([])
const currentUser: User = reactive({});
const menusPops = reactive({
  userMng: false,
  salaryMng: false,
  orgMng: false,
  attendanceMng: false,
});
const attendanceNum = reactive({
  s_zc: 0,
  s_zt: 0,
  s_cd: 0,
  s_qq: 0,
  s_qj: 0,
  s_yc: 0,
});
const userNum = reactive({
  s_all_num: 0,
  s_rz_t_m_num: 0,
  s_syq_num: 0,
  s_rz_num: 0,
  s_sy_num: 0,
  s_zs_num: 0,
  s_li_num: 0,
});
const salaryNum = reactive({
  finish_salary: 0,
  last_all_Salary: 0,
});
const salaryLine = reactive({
  salaryLastSixMonths: [] as Array<string>,
  months: [] as Array<number>,
});

const salaryArray = computed(() => {
  const result = salaryLine.salaryLastSixMonths.map((salaryStr, index) => {
    const value = parseFloat(salaryStr) || 0;
    const monthNum = salaryLine.months[index] || 0;
    const name = monthNum.toString().padStart(6, '0').replace(/(\d{4})(\d{2})/, '$1-$2');
    return { value, name };
  });
  return result.reverse(); // 返回逆序副本
});
onMounted(() => {
  farmClientUtils.postObject('api/salaryuser/salaryLastSixMonths', {}, (nums: object) => {
    Object.assign(salaryLine, nums);
  });

  farmClientUtils.postObject('api/salaryuser/counts', {}, (nums: object) => {
    Object.assign(salaryNum, nums);
  });

  farmClientUtils.postObject('api/localuser/counts', {}, (nums: object) => {
    Object.assign(userNum, nums);
  });
  farmClientUtils.postObject('api/attendancesummary/counts', {}, (nums: object) => {
    Object.assign(attendanceNum, nums);
  });
  farmUserUtils.loadUser((user: User) => {
    Object.assign(currentUser, user);
    items.value = currentUser.posts?.map(node => node.name!) || [];
  });
  userInfo.containAppMenu('/system/localuser', (isContain: boolean) => {
    menusPops.userMng = isContain;
  });
  userInfo.containAppMenu('/system/salaryuser', (isContain: boolean) => {
    menusPops.salaryMng = isContain;
  });
  userInfo.containAppMenu('/system/localorg', (isContain: boolean) => {
    menusPops.orgMng = isContain;
  });
  userInfo.containAppMenu('/system/attendancesummary', (isContain: boolean) => {
    menusPops.attendanceMng = isContain;
  });
})

</script>
<style scoped>
.whrs_flags {
  display: flex;
  gap: 20px;
  padding: 20px;
  padding-top: 0px;

  .whrs_flag_item {
    background-color: var(--el-skc-theme-gray-color);
    flex: 1;
    padding: 20px;
    text-align: center;
    color: #999999;
    border-radius: 10px;
    font-size: 12px;

    .whrs_flag {
      font-size: 22px;
    }
  }
}

.whrs_home_funcs {
  margin-top: 20px;
  display: flex;
  gap: 20px;

  /* 子元素之间的间距 */
  .whrs_home-td {
    min-height: 300px;
    background-color: #ffffff;
    border-radius: 20px;
    flex: 1;

    /* 让每个子项平均分配宽度 */
    .whrs_title {
      padding: 20px;
      padding-bottom: 10px;
      font-weight: 700;
      font-size: 15px;
      color: #666;

      .el-icon {
        margin-right: 8px;
        top: 2px;
      }
    }

    .whrs_item {
      display: flex;
      padding: 10px 0px;
      font-size: 14px;
      color: #999999;
      margin: 10px;
      border-radius: 8px;
      background-color: var(--el-skc-theme-gray-color);

      .whre-item-title {
        padding: 10px 20px;
        flex: 1;

        .el-icon {
          top: 2px;
          margin-right: 4px;
        }
      }

      .whre-item-flag {
        margin: 4px;
        padding: 8px;
        margin-right: 20px;
        min-width: 20px;
        font-size: 12px;
        text-align: center;
        border-radius: 16px;
        background-color: #ffffff;
        opacity: 0.6;
      }
    }

    .whrs_success {
      .whre-item-title {
        color: var(--el-color-success);
      }

      .whrs_flag {
        color: var(--el-color-success);
      }

      .whre-item-flag {
        background-color: var(--el-color-success);
        color: #ffffff;
      }
    }

    .whrs_danger {
      .whre-item-title {
        color: var(--el-color-warning);
      }

      .whrs_flag {
        color: var(--el-color-warning);
      }

      .whre-item-flag {
        background-color: var(--el-color-warning);
        color: #ffffff;
      }
    }
  }
}


.whrs_home_line1 {
  background-color: var(--el-skc-theme-h-color);
  border-radius: 20px;
  color: rgb(255, 255, 255);
  flex-wrap: wrap;
  padding-bottom: 20px;
  padding: 20px;

  .whrs_title {
    font-size: 22px;
    padding: 0px 8px;
  }

  .whrs_posts {
    margin-top: 20px;

    .el-tag {
      margin-left: 8px;
    }
  }

  .whrs_funcs {
    margin-top: 20px;

    .el-button {
      margin-left: 8px;
      margin-top: 8px;
    }
  }
}
</style>
