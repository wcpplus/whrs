<template>
  <!-- 返回顶部按钮 -->
  <div class="toggle-button" title="返回顶部" v-if="!isMobile">
    <img @click="backTop" src="/icon/goTop.png" alt="AI Icon">
  </div>
</template>
<script setup lang="ts">
import { useDeviceStore } from '@/store/useDeviceStore';
import { computed} from 'vue';
const deviceStore = useDeviceStore();
const isMobile = computed(() => { return deviceStore.$state.isMobile });
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
/**
 * 返回页面顶部
 */
const backTop = () => {
  const scrollContainer = document.querySelector('.knowScrollWin') as HTMLElement | null;
  if (scrollContainer) {
    scrollContainer.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
    return;
  }
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  });
}
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
.toggle-button {
  position: fixed;
  top: calc(100vh - 260px);
  right: 20px;
  width: 50px;
  height: 40px;
  z-index: 100;
  opacity: 0.3;

  img {
    width: 48px;
    height: 48px;
    cursor: pointer;
    margin-right: 8px;
  }
}
</style>
