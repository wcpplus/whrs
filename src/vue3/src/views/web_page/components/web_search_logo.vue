<template>
  <div style="margin-top: 15vh;text-align: center;" :style="{marginTop:isMobile?'8vh':'15vh'}">
    <div style="display: flex;align-items: center;justify-content:center;font-size: 42px;">
      <a style="text-decoration: none;" :href="urlRef">
        <img :src="config.logoPath"
          style="border-radius: 12px 32px 12px 12px;height: 36px;cursor: pointer;margin-top: -6px;" />
        <span style="margin-left: 10px;color:#25396f; font-weight: 700;font-size: 36px; cursor: pointer;">{{ config.title
        }}</span></a>
    </div>
  </div>
</template>
<script setup lang="ts">
import farmRoutUtils from '@/hook/farmRoutUtils';
import { useDeviceStore } from '@/store/useDeviceStore';
import { useSystemConfigStore } from '@/store/useSystemConfigStore';
import { computed, onMounted, reactive, ref } from 'vue';
const deviceStore = useDeviceStore();
const isMobile = computed(() => { return deviceStore.$state.isMobile });
const urlRef = ref('');
farmRoutUtils.getHomeUrl((url) => {
  urlRef.value = url;
})
const sysConfig = useSystemConfigStore();
const config = reactive({ title: "", logoPath: "" });
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
onMounted(() => {
  sysConfig.getParameter("farm2.config.sys.title.head", (value) => {
    config.title = value as string;
  });
  sysConfig.getParameter("logoPath", (value) => {
    config.logoPath = value as string;
  });
});
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
