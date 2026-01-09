<template>
  <div class="farm2-data-navigate-box">
    <div class="farm2-data-navigate">
      <div v-if="props.title" style="padding: 20px;color: #ffffff;padding-top: 16px;padding-bottom: 16px;">
        {{ props.title }}
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const props = defineProps({
  //加载远程树URL
  title: {
    type: String,
    required: false
  }
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style>
/* 样式可以根据需要添加 */
.farm2-data-navigate-box {
  padding-bottom: 20px;
}

.farm2-data-navigate {
  padding: 4px;
  background-color: var(--el-color-primary);
}

.farm2-data-navigate .el-breadcrumb__item {
  font-size: 1.0em;
}
</style>
