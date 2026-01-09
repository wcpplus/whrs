<template>
  <div style="display: flex;">
    <el-pagination :current-page="props.dataResult.page" @current-change="handleCurrentChange" class="pagination"
      background :page-size="props.dataResult.pageSize" :page-count="props.dataResult.totalPage" :pager-count="5"
      layout="prev, pager, next" :total="props.dataResult.totalSize" />
    <div style="font-size: 14px; color: #aaaaaa;padding-top: 6px;padding-left: 20px;">
      共{{ props.dataResult.totalPage }}页/{{ props.dataResult.totalSize }}条记录
    </div>
  </div>

</template>
<script setup lang="ts">
import type { DataResult } from '@/types/commons/DataResult';
import type { PropType } from 'vue';


const props = defineProps({
  dataResult: {
    type: Object as PropType<DataResult>,
    required: true
  }, doPageHandle: {
    type: Function,
    required: true
  }
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const handleCurrentChange = (cpage: number) => {
  props.doPageHandle(cpage);
};
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
