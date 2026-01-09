<template>
  <el-drawer v-model="isOpenWin" style="background-color: var(--el-farms-win-bg-color);max-width: 100%;"
    class="farm2-data-form" :append-to-body="true" :with-header="false" :size="isFullScreen ? 2000 : winWidth">
    <div class="header" v-if="!isFullScreen">
      <img src="/icon/file.png" alt="AI Icon" />
      <div style=" white-space: nowrap;  overflow: hidden; text-overflow: ellipsis;">
        {{ file.title }}
      </div>

      <el-button v-if="showView" style="margin-left: auto; " @click="() => { isFullScreen = true; }" type="success"
        plain icon="FullScreen" round>最大化</el-button>
    </div>
    <div class="content" v-loading="loadingFlag" :style="{ height: contentHeight }">

      <el-button v-if="isFullScreen" style="margin-left: auto;z-index: 999; position: absolute; top: 8px;right: 28px;"
        @click="() => { isFullScreen = false; }" type="info" plain icon="Close" round>还原</el-button>

      <div class="message" v-if="!showView" style="padding: 20px;">
        <div style="margin-top: 20px;"> 文件大小： {{ farmFileUnitTools.getFileSizeTitle(file.size) }}</div>
        <div style="line-height: 1.7em;">
          预览文件处理状态： {{ file.stateTitle }}
          <div title="预览文件转换进度" v-if="processState.isShow"> &nbsp;|&nbsp;{{
            processState.msg }}
          </div>
          <div title="预览文件转换进度" v-if="processState.isShow">
            <el-progress :percentage="processState.percent" :stroke-width="15" striped striped-flow :duration="10" />
          </div>
        </div>
      </div>
      <div class="message" v-if="showView" style="height: 100%;">
        <view_all :fileid="file.id" :is-know-view="true" :extend-file-id="file.extendFile.id"
          :file-model="file.extendFile.filemodel"></view_all>
      </div>
    </div>
    <div class="footer" v-loading="loadingFlag" v-if="!isFullScreen">
      <div class="input-container">
        <el-button v-if="pop.download" style="width: 30%;min-width: 100px;" @click="downFile()" type="success" plain
          icon="Download" round>下载附件</el-button>
        <el-button style="width: 30%;min-width: 100px;" @click="() => { isOpenWin = false; }" type="info" plain
          icon="Close" round>关闭窗口</el-button>
      </div>
    </div>
  </el-drawer>
</template>
<script lang="ts" setup>
import view_all from '@/views/file_view/view_all.vue'
import { computed, onUnmounted, reactive, ref } from 'vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import notice from '@/components/msg/FarmNotice';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import type { DataResponse } from '@/types/commons/DataResponse';
import farmRoutUtils from '@/hook/farmRoutUtils';
import farmFileUnitTools from '@/hook/farmFileUnitTools';
import farmProcessUtils from '@/hook/farmProcessUtils';
//------------------------------------------------------------------------------------
const contentHeight = computed(() => {

  return `calc(100vh - ${isFullScreen.value ? 8 : 124}px)`;

});
const isOpenWin = ref(false);
const pop = reactive({
  download: false
});
//预览文件转换进度
const processState = reactive({
  isShow: false,
  percent: 1,
  msg: ''
});

const isFullScreen = ref(false);
const isReloading = ref(false);
const winWidth = ref('300px')
const loadingFlag = ref(false);//加载中遮罩
const showView = ref(false);
const file = reactive({ title: '', id: '', stateTitle: '', size: 0, extendFile: { filemodel: '', id: '' } });
let eventSource: EventSource | null;

const openWin = (fileid: string, isview?: boolean) => {
  // 👇 新增：关闭上一个文件的 SSE 监听
  if (eventSource) {
    eventSource.close();
    eventSource = null;
    processState.isShow = false; // 可选：清空进度状态
  }
  if (isview == null) {
    isview = true;
  }
  isOpenWin.value = true;
  showView.value = false;
  winWidth.value = "300px";
  //加载附件信息
  farm2Request.submit('api/wfile/info', RequestTypeEnum.post, loadingFlag, { id: fileid }).then((response: DataResponse) => {
    const fileview = (response.data) as { title: string, state: string, stateTitle: string, size: number, extendFile: { filemodel: string, id: string } };
    file.title = fileview.title;
    //展示下载按钮
    file.id = fileid;
    file.size = fileview.size;
    //预览文件状态
    file.stateTitle = fileview.stateTitle;
    //是否有预览文件
    if (fileview.extendFile && isview) {
      //是否拥有预览权限
      if (true) {
        winWidth.value = "1000px";
        //展示预览内容
        file.extendFile.id = fileview.extendFile.id;
        file.extendFile.filemodel = fileview.extendFile.filemodel;
        showView.value = true;
      }

    }
    pop.download = true;
    if (!fileview.extendFile && (fileview.state == '1' || fileview.state == '0')) {
      //加载附件处理进度
      eventSource = farmProcessUtils.processHandle(file.id, 'CONVERT_EXFILE', (msg: string, percent: number) => {
        processState.isShow = true;
        processState.msg = msg;
        processState.percent = percent;
        if (processState.percent >= 100 && !isReloading.value) {
          isReloading.value = true;
          setTimeout(() => {
            openWin(fileid, isview);
            isReloading.value = false;
          }, 500);
        }
      });
    }
  }).catch((msg: string) => {
    notice.byError(msg);//报错
    isOpenWin.value = false;
  });
};

const downFile = () => {
  farmRoutUtils.downloadFile(file.id);
}
//---------------------------------------------------

onUnmounted(() => {
  eventSource?.close();
  eventSource = null; // 显式置空
});


defineExpose({
  openWin
});
</script>

<style scoped>
/* 全局样式 */
.header {
  padding: 10px;
  font-size: 16px;
  height: 42px;
  overflow: hidden;
  display: flex;
  align-items: center;
  color: #555555;
  font-weight: 700;
}

.header img {
  height: 24px;
  width: 24px;
  margin-right: 20px;
}

.content {
  border-radius: 1em;
  background-color: #ffffff;
  margin: 20px;
  margin-bottom: 4px;
  margin-top: 4px;
  overflow: auto;
}

.content .message {
  color: #999999;
  font-size: 14px;
}

.content ul {
  margin-left: -10px;
  line-height: 3em;
  padding-top: 20px;
}

.footer {
  font-size: 12px;
  padding: 10px;
}

.input-container {
  background-color: #ffffff;
  border: 1px solid #dddddd;
  border-radius: 8px 22px 22px 8px;
  padding: 10px;
  text-align: center;
}

.skc-chart-input {
  border: none !important;
  outline: none !important;
  resize: none;
  width: 200px;
  color: #555555;
}

.skc-chart-input:focus {
  border-color: transparent !important;
}
</style>
