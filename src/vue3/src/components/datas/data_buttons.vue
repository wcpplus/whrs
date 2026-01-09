<template>
  <div class="farm2-data-buttons-operators">
    <el-button type="primary" @click="openDataFormClick" plain><el-icon>
        <Plus />
      </el-icon>创建</el-button>
    <el-button type="danger" plain><el-icon>
        <Delete />
      </el-icon>删除</el-button>
  </div>
</template>
<script setup lang="ts">
import { getCreatType, type FormParam } from '@/types/commons/FormType';
import { PageEvent } from '@/types/commons/PageEvent';
import type { Emitter, EventType } from 'mitt';
import { inject } from 'vue';
const eventBus = inject('eventBus') as Emitter<Record<EventType, unknown>>;
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//打开数据表单窗口-事件
const openDataFormClick = () => {
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getCreatType() } });
}
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
