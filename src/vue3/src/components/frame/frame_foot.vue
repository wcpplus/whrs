<template>
  <div style="">
    <el-row id="farm2-frame-foot">
      <el-col :span="14" style="text-align: center;padding-top: 4px;">
        {{ footTitleRef }}-{{ footVersionRef }}
      </el-col>
      <el-col :span="10" style="text-align: right;padding-right: 20px;">
        <el-button size="small" @click="newWindwoToCurrentPage" plain>新窗口打开</el-button>
        <el-button v-if="helpUrlRef&&helpUrlRef!='NONE'" @click="farmRoutUtils.gotoPage(helpUrlRef,true)" size="small" type="primary"
          icon="QuestionFilled" plain>帮助&nbsp;|&nbsp;反馈</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script setup lang="ts">
import farmRoutUtils from '@/hook/farmRoutUtils';
import { useSystemConfigStore } from '@/store/useSystemConfigStore';
import { onMounted, ref } from 'vue';
const sysConfig = useSystemConfigStore();
const footTitleRef = ref('');
const footVersionRef = ref('');
const helpUrlRef = ref('');

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

function newWindwoToCurrentPage() {
  window.open(window.location.href, '_blank');
}
onMounted(() => {
  sysConfig.getParameter("farm2.config.sys.title.foot", (value) => {
    footTitleRef.value = value as string;
  });
  sysConfig.getParameter("farm2.conf.server.version", (value) => {
    footVersionRef.value = value as string;
  });
  sysConfig.getParameter("farm2.config.sys.help.url", (value) => {
    helpUrlRef.value = value as string;
  });
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
#farm2-frame-foot {
  color: #7c8db5 !important;
  font-size: 0.8em;
  height: 36px;
  text-align: center;
  padding-top: 6px;
  background-color: #ffffff;
  border-top: 1px solid #eee;
}


.dark #farm2-frame-foot {
  background-color: #000000;
  border-top: 1px solid #111111;
}
</style>
