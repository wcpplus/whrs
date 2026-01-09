<template>
  <div class="farm2-data-page">
    <el-pagination v-model:current-page="currentPage4" :page-size="props.remoteData.pageSize"
      :page-sizes="[ 10, 100, 200]" :size="size" :disabled="disabled" :background="background"
      layout="total, sizes, prev, pager, next, jumper" :total="props.remoteData.totalSize"
      @size-change="handleSizeChange" @current-change="handleCurrentChange" />
  </div>
</template>
<script setup lang="ts">
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
import type { DataQuery } from '@/types/commons/DataQuery';
import type { DataResult } from '@/types/commons/DataResult';
import { PageEvent } from '@/types/commons/PageEvent';
import type { ComponentSize } from 'element-plus'
import type { Emitter, EventType } from 'mitt';
import { inject, ref } from 'vue';
const eventBus = inject('eventBus') as Emitter<Record<EventType, unknown>>;
const props = defineProps<{ remoteData: DataResult }>();
const currentPage4 = ref(1)
const size = ref<ComponentSize>('default')
const background = ref(false)
const disabled = ref(false)

const handleSizeChange = (val: number) => {
  const query: DataQuery = {};
  query.pageSize = val;
  eventBus.emit(PageEvent.data_do_query, query);
}
const handleCurrentChange = (val: number) => {
  const query: DataQuery = {};
  query.page = val;
  eventBus.emit(PageEvent.data_do_query, query);
}
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
