<template>
  <el-empty v-if="page > 0" :description="props.tip" :image-size="200" />
</template>
<script setup lang="ts">
const props = defineProps({
  tip: {
    type: String,
    required: true
  },
  page: {
    type: Number,
    required: false,
    default: 1
  }
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
