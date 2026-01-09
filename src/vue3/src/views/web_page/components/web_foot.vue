<template>
  <div :style="!props.mini?'margin-top: 50px;padding-top: 20px;padding-bottom: 10px;height: 70px;':'height: 30px;padding-top: 4px;'"
    style="text-align: center;color: #999999;font-size: 14px; background-color: #ffffff;box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.05);">
    {{ footTitleRef }}-{{ footVersionRef }}
  </div>
  <Water_mark></Water_mark>
</template>
<script setup lang="ts">
import { useSystemConfigStore } from '@/store/useSystemConfigStore';
import { onMounted, ref } from 'vue';
import Water_mark from './water_mark.vue';
const sysConfig = useSystemConfigStore();
const footTitleRef = ref('');
const footVersionRef = ref('');
const props = defineProps({
  mini: {
    type: Boolean,
    required: false,
    default: false,
  }
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
onMounted(() => {
  sysConfig.getParameter("farm2.config.sys.title.foot", (value) => {
    footTitleRef.value = value as string;
  });
  sysConfig.getParameter("farm2.conf.server.version", (value) => {
    footVersionRef.value = value as string;
  });
});
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
