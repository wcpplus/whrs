<template>
  <div style="color: #999999;padding: 10px;padding-left: 0px;">
    <el-icon style="margin-right: 4px;top: 2px;">
      <InfoFilled />
    </el-icon>{{ props.tip }}
  </div>
</template>
<script setup lang="ts">
const props = defineProps({
  tip: {
    type: String,
    required: true,
  }
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
