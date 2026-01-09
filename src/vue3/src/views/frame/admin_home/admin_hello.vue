<template>
  <div style="padding: 10px 30px;">
    <div class="whrs_home_line1" style="display: flex;">
      <div style="flex:1">
        <div class="whrs_title">您好，{{ currentUser.name }}！今天是
          {{ farmTimeUtils.formatDate8Stype2(farmTimeUtils.getDate8()) }}。</div>
        <div class="whrs_posts">
          <el-tag v-for="item in items" :key="item" type="primary" effect="light" round>
            {{ item }}
          </el-tag>
        </div>
        <div class="whrs_funcs">
          <el-button v-if="menusPops.userMng" @click="farmRoutUtils.gotoPage('/system/localuser')" size="large"
            icon="User" type="success" round>员工档案管理</el-button>
          <el-button v-if="menusPops.userMng" @click="farmRoutUtils.gotoPage('/system/localuser')" size="large"
            icon="MilkTea" type="success" round>入职办理</el-button>
          <el-button v-if="menusPops.userMng" @click="farmRoutUtils.gotoPage('/system/localuser')" size="large"
            icon="Suitcase" type="success" round>离职办理</el-button>
          <el-button v-if="menusPops.salaryMng" @click="farmRoutUtils.gotoPage('/system/salaryuser')" size="large"
            icon="Money" type="success" round>薪酬计算</el-button>
          <el-button v-if="menusPops.orgMng" @click="farmRoutUtils.gotoPage('/system/localorg')" size="large"
            icon="HomeFilled" type="success" round>组织架构</el-button>
          <el-button v-if="menusPops.attendanceMng" @click="farmRoutUtils.gotoPage('/system/attendancesummary')"
            size="large" icon="Calendar" type="success" round>考勤异常处理</el-button>
        </div>
      </div>
      <div style="padding: 30px;">
        <el-button type="danger" icon="Bell" @click="bitWinRef.openWin(true)"
          style="height: 80px;font-size: 18px;border-radius: 30px;padding: 20px 30px;" round plain>考勤打卡</el-button>
      </div>
    </div>
    <Attendance_bit_win ref="bitWinRef" :refresh-handle="() => { FarmNotice.bySuccess('打卡成功！') }"></Attendance_bit_win>
  </div>
</template>
<script setup lang="ts">
import farmTimeUtils from '@/hook/farmTimeUtils';
import farmUserUtils from '@/hook/farmUserUtils';
import type { User } from '@/types/system/user';
import { onMounted, reactive, ref } from 'vue';
import { useUserInfoStore } from "@/store/useUserInfoStore";
import farmRoutUtils from '@/hook/farmRoutUtils';
import Attendance_bit_win from '@/views/back_console/attendance/attendancesummary/attendance_bit_win.vue';
import FarmNotice from '@/components/msg/FarmNotice';
const userInfo = useUserInfoStore();
const bitWinRef = ref();
const items = ref<Array<string>>([])
const currentUser: User = reactive({});
const menusPops = reactive({
  userMng: false,
  salaryMng: false,
  orgMng: false,
  attendanceMng: false,
});

onMounted(() => {
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
