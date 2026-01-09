<template>
  <el-dialog v-model="showWinRef" style="background-color: #ffffff;" title="任务参数" width="300">
    <el-form v-loading="loadingFlag" :model="form" label-width="auto" ref="ruleFormRef">
      <el-form-item label="超时时间:" prop="timeout">
        <el-input-number v-model="form.timeout" :min="0" :max="3600" />（秒）
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button v-if="form.id" @click="submitForm" type="primary">
          提交更新
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { reactive, ref } from 'vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import notice from '@/components/msg/FarmNotice';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import type { DataResponse } from '@/types/commons/DataResponse';
const showWinRef = ref(false);
const loadingFlag = ref(false);//加载中遮罩

// 表单默认值
const form = reactive({
  id: '',
  timeout: ''
})
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

const openWin = (nodeId: string) => {
  console.log(nodeId);
  showWinRef.value = true;
  loadParas(nodeId);
}
const loadParas = (nodeId: string) => {
  farm2Request
    .submit('api/wdapflownode/' + nodeId, RequestTypeEnum.get, loadingFlag)
    .then((response: DataResponse) => {
      Object.assign(form, response.data);
      //console.log(response.data);
    })
    .catch((msg: string) => {
      notice.byError(msg); // 报错
    });
}
const submitForm = () => {
  farm2Request
    .submit('api/wdapflownode/' + form.id, RequestTypeEnum.put, loadingFlag, { timeout: form.timeout })
    .then(() => {
      showWinRef.value = false;
    })
    .catch((msg: string) => {
      notice.byError(msg); // 报错
    });
}



//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

// 暴露方法给父组件
defineExpose({
  openWin,
});
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
