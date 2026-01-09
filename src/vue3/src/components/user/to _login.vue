<template>
  <span style="display: none;"></span>
</template>
<script setup lang="ts">
import { useUserInfoStore } from "@/store/useUserInfoStore";
import { useRouter } from 'vue-router';
const router = useRouter();
const userInfoStore = useUserInfoStore();
import { watch } from 'vue'
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
watch(
  () => userInfoStore.$state.toLogin,//监视用户登录状态（tologin为true时要跳转到登录页面）
  (toLogin) => {
    if (toLogin) {
      router.push('/login');
    }
  },
  { immediate: true } // 立即执行一次，确保页面初次加载时也同步
);
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
