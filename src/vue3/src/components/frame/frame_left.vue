<template>
  <div class="farm2_header_container">
    <img class="logo" :src="config.logoPath" alt="Logo" />
    <div class="farm2_sys_title">{{ config.title }}</div>
  </div>
  <div style="padding-left: 15px;padding-right: 15px;">
    <el-menu v-loading="isLoading" :default-active="activeIndex" :router="true" class="el-menu-vertical-demo"
      :collapse="false">
      <el-sub-menu v-for="menu in menus" :key="menu.id" :index="menu.id.toString()">
        <template #title>
          <el-icon>
            <Menu></Menu>
          </el-icon>
          <span>{{ menu.name }}</span>
        </template>
        <el-menu-item v-for="menu2 in menu.children" :key="menu2.id" :index="menu2.menukey">{{ menu2.name
        }}</el-menu-item>
      </el-sub-menu>
    </el-menu>
    <div class="whrs_help_box">
      <div class="whrs_item" @click="farmRoutUtils.gotoPage('https://gitee.com/macplus/WHRS')">
        <el-icon>
          <Help />
        </el-icon>使用说明|帮助
      </div>
      <div class="whrs_item" @click="farmRoutUtils.gotoPage('http://skc.wcpknow.com/')">
        <el-icon>
          <Finished />
        </el-icon>知识管理 | 培训 | 考试问卷
      </div>
    </div>
  </div>
  <User_from_password update-url="api/current/reset.password" ref="rePasswordForm"></User_from_password>
  <user_logout ref="logoutRef"></user_logout>
</template>
<script setup lang="ts">
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
import user_logout from '@/components/user/user_logout.vue';
import { onMounted, reactive, ref } from 'vue';
import User_from_password from '@/views/system/localuser/components/user_form_password.vue';
import { useRoute, useRouter } from 'vue-router';
import { useSystemConfigStore } from '@/store/useSystemConfigStore';
import { useUserInfoStore } from '@/store/useUserInfoStore';
import notice from '@/components/msg/FarmNotice';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import type { DataResponse } from '@/types/commons/DataResponse';
import type { Menu } from '@/types/system/menu';
import type { User } from '@/types/system/user';
import farmRoutUtils from '@/hook/farmRoutUtils';
const sysConfig = useSystemConfigStore();
const userInfo = useUserInfoStore();
//回显菜单状态
const activeIndex = ref();
const logoutRef = ref();
activeIndex.value = useRoute().path;
const router = useRouter();
const isLoading = ref(false);
//修改密码窗口
const rePasswordForm = ref();
//后台参数
const config = reactive({ title: "", logoPath: "" });

/**
 * 菜单信息
 */
const menus = reactive<Menu[]>([]);
/**
 * 用户信息
 */
const user = reactive<User>({ name: "", loginname: "", type: "", tags: [] });
/**
 * 初始化用户信息
 */
const initUserInfo = () => {
  //加载用户信息
  //加载用户菜单
  Farm2Request.submit("api/current/user", RequestTypeEnum.post, isLoading, {}).then((dataResponse: DataResponse) => {
    Object.assign(menus, (dataResponse.data as { frameMenus: [Menu] }).frameMenus);
    Object.assign(user, (dataResponse.data as { user: User }).user)
    Object.assign(userInfo.$state.user, (dataResponse.data as { user: User }).user)
  }).catch((msg: Error) => {
    notice.byError(msg.message);
  });
}
initUserInfo();
// 监听路由变化，自动添加标签
router.afterEach((to) => {
  activeIndex.value = to.path;
});


onMounted(() => {
  sysConfig.getParameter("farm2.config.sys.title.head", (value) => {
    config.title = value as string;
  });
  sysConfig.getParameter("logoPath", (value) => {
    config.logoPath = value as string;
  });
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
.whrs_help_box {
  margin-top: 20px;

  .whrs_item {
    padding: 10px;

    font-size: 14px;
    margin: 0px;
    margin-top: 0px;
    margin-bottom: 0px;
    border-radius: 20px;
    color: #999999;
    position: relative;
    bottom: 10px;
    cursor: pointer;
    padding-left: 20px;

    .el-icon {
      top: 2px;
      margin-right: 6px;
    }
  }

  .whrs_item:hover {
    background-color: var(--el-menu-hover-bg-color);
  }
}
</style>
