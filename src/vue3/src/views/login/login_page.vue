<template>
  <div v-if="!isMobile" class="common-layout" style="height: 100%;">
    <!--PC端-->
    <el-container>
      <el-main style="background-color: var(--el-color-primary);overflow: hidden;position: relative;">
        <img src="/images/login/bg2.jpg" style="height: 100%;min-width: 100%;opacity: 0.7;" />
        <!-- 居中的白色大字 -->
        <div style="position: absolute; top: 40%;opacity: 0.8; left: 50%;word-break: keep-all; transform: translate(-50%, -50%); color: white;
          font-weight: 700; font-size: 100px; text-align: center; width: 100%;">
          <img :src="config.logoPath" alt="Logo" style="height: 74px;opacity: 0.7;border-radius: 20px;" />
          <span>{{ config.title }}</span>
          <div style="font-size: 24px;">{{ config.foot }}</div>
        </div>
      </el-main>
      <el-aside width="550px">
        <login_box></login_box>
        <div class="skc_tip_box" v-if="config.tip && config.tip != 'NONE'" v-html="config.tip"></div>
      </el-aside>
    </el-container>
  </div>
  <div v-if="isMobile">
    <!--移动端-->
    <login_box></login_box>
    <div class="skc_tip_box" v-if="config.tip && config.tip != 'NONE'" v-html="config.tip"></div>
  </div>
</template>
<script setup lang="ts">
import { computed, reactive } from 'vue';
import login_box from './login_box.vue';
import { useDeviceStore } from '@/store/useDeviceStore';
import { useSystemConfigStore } from '@/store/useSystemConfigStore';

const sysConfig = useSystemConfigStore();
const deviceStore = useDeviceStore();
const isMobile = computed(() => { return deviceStore.$state.isMobile });
const config = reactive({
  tip: '', title: "", logoPath: "", foot: ""
});
sysConfig.getParameter("farm2.config.sys.tip.login.html", (val) => {
  config.tip = val as string;
})
sysConfig.getParameter("farm2.config.sys.title.head", (value) => {
  config.title = value as string;
});
sysConfig.getParameter("farm2.config.sys.title.foot", (value) => {
  config.foot = value as string;
});
sysConfig.getParameter("logoPath", (value) => {
  config.logoPath = value as string;
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style>
.skc_tip_box {
  text-align: left;
  color: var(--el-color-info-dark-2);
  margin: auto;
  margin-top: 20px;
  max-width: 260px;
  background-color: var(--el-color-warning-light-9);
  padding: 20px;
  border-radius: 10px;
  border: 1px dashed var(--el-color-warning-dark-2);
  font-size: 14px;
}
</style>
