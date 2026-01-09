<template>
  <!--当前页面展示进度-->
  <div v-if="processVisibleRef">
    <el-progress :percentage="processState.percent" status="success" :title="keyRef" :stroke-width="15" striped
      striped-flow :duration="10">
      <span style="font-size: 12px;">{{ processState.msg }}</span>
    </el-progress>
  </div>
  <!--弹出窗口展示进度-->
  <el-dialog v-model="dialogVisibleRef" title="任务处理中..." destroy-on-close width="500" :before-close="() => { }">
    <span>{{ keyRef }}</span>
    <div style="margin-bottom: 8px;">{{ processState.msg }}</div>
    <el-progress :percentage="processState.percent" :stroke-width="15" striped striped-flow :duration="10" />
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="dialogVisibleRef = false">
          后台执行
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import notice from '@/components/msg/FarmNotice';
import farmProcessUtils from '@/hook/farmProcessUtils';
import { onUnmounted, reactive, ref } from 'vue';
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const props = defineProps({
  successHandle: {
    type: Function,
    required: false,
  }
});
const keyRef = ref('');
let eventSource: EventSource | null;
const processState = reactive({
  isShow: false,
  percent: 1,
  msg: ''
});
const dialogVisibleRef = ref(false);
const processVisibleRef = ref(false);
// 定义一个方法，打开弹出进度展示
const openWin = (key: string, processType: string) => {
  keyRef.value = key;
  dialogVisibleRef.value = true;
  Object.assign(processState, { isShow: false, 'msg': '', 'percent': 0 })
  eventSource = farmProcessUtils.processHandle(key, processType, (msg: string, percent: number) => {
    Object.assign(processState, { isShow: true, 'msg': msg, 'percent': percent })
    if (processState.percent >= 100) {
      notice.bySuccess(processState.msg);
      dialogVisibleRef.value = false;
      if (props.successHandle) {
        props.successHandle();
      }
    }
  });
};
/**
 * 直接展示进度
 */
const show = (key: string, processType: string) => {
  keyRef.value = key;
  processVisibleRef.value = true;
  Object.assign(processState, { isShow: false, 'msg': '', 'percent': 0 })
  eventSource = farmProcessUtils.processHandle(key, processType, (msg: string, percent: number) => {
    Object.assign(processState, { isShow: true, 'msg': msg, 'percent': percent })
    if (processState.percent >= 100) {
      notice.bySuccess(processState.msg);
      processVisibleRef.value = false;
      if (props.successHandle) {
        props.successHandle();
      }
    }
  });
};

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
onUnmounted(() => {
  if (eventSource) {
    eventSource.close();
  }
});
// 暴露方法给父组件
defineExpose({
  openWin, show
});
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
