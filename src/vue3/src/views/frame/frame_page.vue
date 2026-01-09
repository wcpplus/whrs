<template>
  <el-container>
    <el-aside width="270px" id="farm2-frame-left">
      <!--菜单menus-->
      <Frame_left></Frame_left>
    </el-aside>
    <el-container id="farm2-frame-container">
      <el-header>
        <div style="display: flex;">
          <div style="flex: 1;">
            <!-- 缓存标签页tabs-->
            <frame_tabs />
          </div>
          <div style="margin-left: auto;">
            <div class="skc_user_info" style="padding-top: 14px; padding-right: 20px;">
              <div class="skc-username" @click="myInfoRef.openWin(true)">{{ user.name }}</div>
              <img class="skc-photo" @click="myInfoRef.openWin(true)"
                :src="'/images/photo/photo' + (user.photoid ? user.photoid : '6') + '.png'"
                style="padding-right: 4px;margin-top: 0px;margin-left: 8px;" alt="Logo" />
              <div class="flags">
                <el-button @click="reSetPassword" style="margin-left: 12px;" type="warning" title="修改密码" size="small"
                  icon="Lock" circle />
                <el-badge :value="nums.msgNum" class="item" :hidden="nums.msgNum===0">
                  <el-button @click=" messageRef.openWin(true);" style="margin-left: 16px;" index="/login" type="success"
                    title="我的消息" size="small" icon="Message" circle />
                </el-badge>
                <el-button @click=" logoutRef.goToLogin();" style="margin-left: 16px;" index="/login" type="info"
                  title="退出系统" size="small" icon="SwitchButton" circle />
              </div>
            </div>
          </div>
        </div>
      </el-header>
      <el-main> <!--首页-->
        <router-view v-slot="{ Component }">
          <keep-alive :max="5" :include="cachedViews">
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </el-main>
      <el-footer>
        <!--权限信息foot-->
        <Frame_foot />
      </el-footer>
    </el-container>
  </el-container>
  <User_from_password update-url="api/current/reset.password" ref="rePasswordForm"></User_from_password>
  <User_logout ref="logoutRef"></User_logout>
  <My_info_setting ref="myInfoRef"></My_info_setting>
  <Message_list ref="messageRef"></Message_list>
</template>
<script setup lang="ts">
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
import Frame_foot from '@/components/frame/frame_foot.vue';
import Frame_left from '@/components/frame/frame_left.vue';
import frame_tabs from '@/components/frame/frame_tabs.vue';
import User_from_password from '@/views/system/localuser/components/user_form_password.vue';
import { onMounted, reactive, ref } from 'vue';
import { useRoute } from 'vue-router';
import { useFrameTabStore } from '@/store/useFrameTabStore';
import { computed } from 'vue';
import type { User } from '@/types/system/user';
import farmUserUtils from '@/hook/farmUserUtils';
import User_logout from '@/components/user/user_logout.vue';
import My_info_setting from '../web_page/user/my_info_setting.vue';
import Message_list from '../web_page/social/message/message_list.vue';
import farmClientUtils from '@/hook/farmClientUtils';
const rePasswordForm = ref();
const logoutRef = ref();
const myInfoRef = ref();
const messageRef = ref();
/**
 * 修改密码
 */
const reSetPassword = () => {
  rePasswordForm.value.openWin([user.loginname]);
}
const user = reactive<User>({ name: "", loginname: "", type: "", tags: [] });
const route = useRoute();
const frameTabStore = useFrameTabStore();
const cachedViews = computed(() => {
  return frameTabStore.$state.tabs.map((node) => node.name);
});
const nums = reactive({
  msgNum: 0,
})
onMounted(() => {
  // 页面初次加载时执行逻辑
  farmUserUtils.loadUser((cuser: User) => {
    Object.assign(user, cuser);
  });
  const name = route.name as string || 'default-name';
  frameTabStore.addTab({
    path: route.path,
    title: route.meta.title as string,
    name: name,
    isCloase: true,
  });
  farmClientUtils.getNum('api/wmsg/num', (num) => {
    nums.msgNum = num;
  });
});




//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
.skc_user_info {
  display: flex;
}

.skc-photo {
  width: 24px;
  height: 24px;
  border-radius: 32px;
  cursor: pointer;
}

.skc-photo:hover {
  opacity: 0.8;
}

.skc-username {
  font-size: 14px;
  margin-top: 2px;
  color: #666666;
  cursor: pointer;
}

.skc-username:hover {
  color: #999999;
}
</style>
