<template>
  <el-dialog v-if="state.dialogVisible" :append-to-body="true"
    style="  background-color: var(--el-skc-theme-body-back-c);;" v-model="state.dialogVisible" width="800">
    <el-row :gutter="20" style="font-size: 18px">
      <el-col :span="form.userkey ? 12 : 24">
        <div style="text-align: center;height: 106px;">
          <div v-if="form.userkey" style="margin-bottom: 20px;margin-top: 50px;">更正打卡时间{{ form.time6 ?
            (form.time6.replaceAll(":",
              "") + '00') : '' }}
          </div>
          <div v-if="!form.userkey" style="margin-bottom: 20px;margin-top: 50px;font-size: 34px;">
            考勤打卡
          </div>
          <el-time-select v-if="form.userkey" format="HH:mm" v-model="form.time6" style="width: 240px" start="06:30"
            step="00:15" end="23:45" placeholder="选择测试时间" />
          <div style="font-size: 14px;color: red;padding-top: 10px;">
            <span v-if="form.error">{{ form.error }}</span>
          </div>
        </div>
        <div style="text-align: center;margin-top: 20px;margin-bottom: 100px;color:#999999">
          <div style="font-size: 32px;">{{ farmTimeUtils.format14ToHms(form.currentTime) }}</div>
          <div style="font-size: 32px;">{{ form.username }}</div>
          <div style="margin-bottom: 20px;">{{ form.userkey }}</div>
          <el-button type="success" size="large" @click="bitSubmit" round style="width: 200px;">考勤打卡</el-button>
        </div>
      </el-col>
      <el-col :span="12" style="border-left: 1px dashed #999999;" v-if="form.userkey">
        <div style="margin-top: 30px;text-align: center;height: 244px;">
          <div style="margin-bottom: 20px;margin-top: 50px;"> 更正考勤状态
          </div>
          <el-input v-model="updateForm.note" style="width: 240px" :rows="2" type="textarea" placeholder="更正原因" />
          <el-select v-model="updateForm.state" placeholder="选择状态" style="width: 240px;margin-top: 20px;">
            <el-option label="正常" value="0" />
            <el-option label="缺勤" value="3" />
            <el-option label="请假" value="4" />
            <el-option label="远程办公" value="5" />
          </el-select>
          <div style="font-size: 14px;color: red;padding-top: 10px;">
            <span v-if="updateForm.error">{{ updateForm.error }}</span>
          </div>
        </div>
        <div style="text-align: center;">
          <el-button type="success" size="large" @click="updateSubmit" round style="width: 200px;m">更正状态</el-button>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>
<script setup lang="ts">
import farmClientUtils from '@/hook/farmClientUtils';
import farmTimeUtils from '@/hook/farmTimeUtils';
import { reactive, onUnmounted } from 'vue';
const props = defineProps({
  refreshHandle: {
    type: Function,
    required: true,
  },
});
const state = reactive({
  dialogVisible: false,
});
const updateForm = reactive({
  note: '',
  state: '',
  error: '',
})
let intervalId: ReturnType<typeof setInterval> | undefined;
let baseDate: Date | null = null;
let startTime: number;

const bitSubmit = () => {
  if (!form.time6.trim()&&form.userkey) {
    form.error = ("请录入打卡时间");
    return;
  }
  farmClientUtils.postObject('api/attendancesummary/bitTest', { key: form.userkey, val: form.time8 + (form.time6.replaceAll(":", "") + '00') }, () => {
    state.dialogVisible = false;
    props.refreshHandle();
  });
}
const updateSubmit = () => {
  if (!updateForm.note.trim() || !updateForm.state.trim()) {
    updateForm.error = ("请录入更正原因和状态");
    return;
  }
  farmClientUtils.postObject('api/attendancesummary/updateState', { key: form.userkey, val: updateForm.state, note: updateForm.note, time: form.time8 }, () => {
    state.dialogVisible = false;
    props.refreshHandle();
  });
}


// 解析 14 位时间字符串
function parse14ToDateTime(s: string): Date | null {
  if (!s || s.length !== 14) return null;
  const y = parseInt(s.substring(0, 4));
  const m = parseInt(s.substring(4, 6)) - 1;
  const d = parseInt(s.substring(6, 8));
  const H = parseInt(s.substring(8, 10));
  const M = parseInt(s.substring(10, 12));
  const S = parseInt(s.substring(12, 14));
  return new Date(y, m, d, H, M, S);
}

const openWin = (isOpen: boolean, userkey?: string, username?: string, date8?: string) => {
  state.dialogVisible = isOpen;
  if (isOpen) {
    form.userkey = userkey || '';
    form.username = username || '';
    form.time8 = date8 || '';
    form.time6 = '';
    loadSysTime();
  } else {
    if (intervalId) clearInterval(intervalId);
    intervalId = undefined;
    baseDate = null;
  }
};

const form = reactive({
  time8: '',
  time6: '120000',
  userkey: '',
  username: '',
  currentTime: '',
  error: '',
});

const loadSysTime = () => {
  farmClientUtils.postObject('api/attendancesummary/systime', {}, (obj: unknown) => {
    const time14 = obj as string;
    form.currentTime = time14;
    baseDate = parse14ToDateTime(time14);
    startTime = Date.now();
    startInterval();
  });
};

const startInterval = () => {
  if (intervalId) clearInterval(intervalId);
  intervalId = setInterval(() => {
    if (!baseDate) return;
    const elapsed = Date.now() - startTime;
    const now = new Date(baseDate.getTime() + elapsed);
    const pad = (n: number) => n.toString().padStart(2, '0');
    form.currentTime = `${now.getFullYear()}${pad(now.getMonth() + 1)}${pad(now.getDate())}${pad(now.getHours())}${pad(now.getMinutes())}${pad(now.getSeconds())}`;
  }, 1000);
};

onUnmounted(() => {
  if (intervalId) clearInterval(intervalId);
});

defineExpose({ openWin });
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
