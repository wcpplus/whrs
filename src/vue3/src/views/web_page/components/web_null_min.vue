<template>
  <div style="color: #909399;padding: 20px;text-align: center;max-width: 100%;"><el-icon style="margin-right: 8px;">
      <Monitor />
    </el-icon>
    <span>{{ props.tip }}</span>
  </div>
</template>
<script setup lang="ts">
const props = defineProps({
  tip: {
    type: String,
    required: true
  }
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
