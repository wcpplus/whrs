<template>
  <div class="farm2-frame-tabs">
    <el-tabs v-model="editableTabsValue" type="card" class="farm2-frame-tabs" @tab-remove="removeTab"
      @tab-click="selectTab">
      <el-tab-pane v-for="item in tabs" :key="item.name" :closable="item.isCloase" :label="item.title"
        :name="item.name"> </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script lang="ts" setup>
import { computed, ref, watch } from 'vue'
import { useFrameTabStore } from '@/store/useFrameTabStore';
import { useRouter } from 'vue-router';
import { useUserInfoStore } from "@/store/useUserInfoStore";
const router = useRouter();
const userInfoStore = useUserInfoStore();
const frameTabStore = useFrameTabStore();
const editableTabsValue = ref()
const tabs = computed(() => frameTabStore.$state.tabs);

watch(
  () => frameTabStore.$state.activeName,
  (newActiveName) => {
    editableTabsValue.value = newActiveName; // 直接赋值
  },
  { immediate: true } // 立即执行一次，确保页面初次加载时也同步
);

watch(
  () => userInfoStore.$state.toLogin,//监视用户登录状态（tologin为true时要跳转到登录页面）
  (toLogin) => {
    if (toLogin) {
      router.push('/login');
    }
  },
  { immediate: true } // 立即执行一次，确保页面初次加载时也同步
);


const removeTab = (targetName: string) => {
  const tabs = frameTabStore.tabs;
  let activeName = editableTabsValue.value;
  frameTabStore.removeTab(targetName);
  if (activeName === targetName) {
    tabs.forEach((tab, index) => {
      if (tab.name === targetName) {
        const nextTab = tabs[index + 1] || tabs[index - 1];
        if (nextTab) {
          activeName = nextTab.name;
        }
      }
    });
  }
  router.push({ name: activeName });
}

// 选择标签
const selectTab = (tab: { paneName: string; }) => {
  frameTabStore.setActiveTab(tab.paneName);
  router.push({ name: tab.paneName });
};

// 监听路由变化，自动添加标签
router.afterEach((to) => {
  if (to.path.indexOf("system") >= 0) {
    frameTabStore.addTab({ path: to.path, title: to.meta.title as string, name: to.name as string, isCloase: true, });
    editableTabsValue.value = to.name;
  }
});

</script>
<style>
.farm2-frame-tabs {
  padding-left: 8px;
  padding-top: 8px;
}

.farm2-frame-tabs .el-tabs__nav-scroll {
  overflow: hidden;
}

.farm2-frame-tabs .el-tabs {
  border: 0px;
}


.farm2-frame-tabs .el-tabs__item {
  background-color: #ffffff;
  padding: 10px;
  padding-top: 15px;
  height: 34px;
  border-radius: 0.5em 1.5em 0em 0em;
}

.dark .farm2-frame-tabs .el-tabs__item {
  background-color: #000000;
}

.farm2-frame-tabs .el-tabs--card>.el-tabs__header .el-tabs__nav {
  border-radius: 0.5em 1.5em 0em 0em;
  overflow: hidden;
  border: 2px;
}

.farm2-frame-tabs .el-tabs__header.is-top {
  margin: 0px;
  height: 34px;
}

.farm2-frame-tabs .el-tabs__item.is-top.is-active.is-top {
  background-color: var(--el-color-primary);
  color: #ffffff;
  border: 0px;
}
</style>
