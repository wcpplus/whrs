<template>
  <el-container>
    <el-header>
      <!--导航条 -->
      <Data_navigate></Data_navigate>
    </el-header>
    <el-main>
      <el-row style="margin-top:20px;"><el-col :span="6"></el-col>
        <el-col :span="12">
          <div class="grid-content ep-bg-purple">
            <div class="lucene-content" v-loading="loadingFlag">
              <div v-if="officeInfo.live&&officeInfo.port!=0"
                style="display: flex; align-items: center; justify-content: center; text-align: center; color: green;">
                <span style="font-size: 32px;">soffice服务可用</span> <el-icon style="font-size: 48px;" class="is-loading">
                  <Setting />
                </el-icon>
              </div>
              <div v-if="!officeInfo.live&&officeInfo.port!=0"
                style="display: flex; align-items: center; justify-content: center; text-align: center; color: red;">
                <span style="font-size: 32px;">soffice连接失败</span> <el-icon style="font-size: 48px;">
                  <Setting />
                </el-icon>
              </div>
              <hr />
              <div class="title">服务地址/端口 <span>{{ officeInfo.ip }}:{{ officeInfo.port }}</span></div>
              <div class="title">服务器环境 <span>{{ officeInfo.os }}</span></div>
              <div class="code"><code> 启动脚本：</code><br /><br />{{ officeInfo.startCmd }} </div>
              <div class="code"> <code> 停止脚本：</code><br /><br />{{ officeInfo.stopCmd }}</div>
              <div class="title" style="display: flex;justify-content: center;">
                <el-button type="danger" @click="stop()">停止服务</el-button>
                <el-button type="success" @click="start()">启动服务</el-button>
                <el-button type="info" @click="loadInfo()">刷新状态</el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import Data_navigate from '@/components/datas/data_navigate.vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import notice from '@/components/msg/FarmNotice';
import type { DataResponse } from '@/types/commons/DataResponse';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
const loadingFlag = ref(false);//加载中遮罩
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const officeInfo = reactive({
  ip: '', port: 0, startCmd: '', stopCmd: '', live: false, os: ''
});
const loadInfo = () => {
  farm2Request.submit('api/soffice/info', RequestTypeEnum.post, loadingFlag)
    .then((response: DataResponse) => {
      Object.assign(officeInfo, (response.data as { ip: string, port: number, startCmd: string, stopCmd: string, live: boolean, os: string }));
    }).catch((msg: string) => {
      notice.byError(msg);//报错
    });
}

const stop = () => {
  farm2Request.submit('api/soffice/stop', RequestTypeEnum.post, loadingFlag)
    .then(() => {
      loadInfo();
    }).catch((msg: string) => {
      notice.byError(msg);
    });
}

const start = () => {
  farm2Request.submit('api/soffice/start', RequestTypeEnum.post, loadingFlag)
    .then(() => {
      loadInfo();
    }).catch((msg: string) => {
      notice.byError(msg);
    });
}

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
onMounted(() => {
  loadInfo();
})
</script>
<style scoped>
/* 样式可以根据需要添加 */
.lucene-content {
  width: 70%;
  margin: auto;
  color: #555555;
  border-radius: 8px;
  background-color: #ffffff;
  text-align: center;
  padding: 30px;

  .title {
    margin-top: 20px;
    font-size: 16px;
  }

  .code {
    word-wrap: break-word;
    background-color: #000000;
    padding: 20px;
    color: #ffffff;
    text-align: left;
    margin-bottom: 20px;
    border-radius: 1em;
    margin-top: 20px;
  }
}
</style>
