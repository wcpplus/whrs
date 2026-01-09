<template>
  <div v-loading="loadingFlag">
    <Data_navigate></Data_navigate>
    <Admin_hello></Admin_hello>
    <Admin_user_info></Admin_user_info>
    <Admin_home></Admin_home>
    <el-row :gutter="20" style="margin: 0px;padding-right: 30px;padding-top: 20px;" v-if="currentUser.isAdmin">
      <home_ctrls color-type="blue" icon="Odometer" title="当前监控" :func="loadInfo" :num="filesInfo.use"></home_ctrls>
      <home_data_num color-type="blue" icon="Files" title="缓存数量 " :func="clearCache"
        :functitle="'清空缓存' + cacheInfo.use + '条'">
      </home_data_num>
      <home_percent icon="User" :persent="diskInfo.percent" :title1="'磁盘容量'" :title2="diskInfo.title"
        :title3="'附件:' + filesInfo.all">
      </home_percent>
      <home_percent icon="Files" :persent="ramInfo.percent" :title1="'内存容量'" :title2="'缓存:' + cacheInfo.use + '条'">
      </home_percent>
      <home_percent icon="Fold" :persent="visitInfo.percent" title1="访问量" :show-persent="false"
        :title2="visitInfo.title + ':' + visitInfo.use + '次'">
      </home_percent>
      <home_percent v-if="false" icon="Files" :persent="EventInfo.percent" :title1="'事件队列'"
        :title2="'等待:' + EventInfo.use + '条'">
      </home_percent>
      <home_data_num color-type="green" icon="GobletFull" title="临时文件" :num="filesInfo.use"></home_data_num>
      <home_data_num color-type="green" icon="Fold" title="事件日志" :num="elogInfo.all"></home_data_num>
      <home_data_num color-type="green" icon="User" title="在线用户" :num="onlineInfo.all"></home_data_num>
    </el-row>
  </div>
</template>
<script setup lang="ts">
import home_ctrls from './components/home_ctrls.vue'
import Data_navigate from '@/components/datas/data_navigate.vue';
import home_data_num from './components/home_data_num.vue';
import home_percent from './components/home_percent.vue';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { onMounted, reactive, ref } from 'vue';
import type { DataResponse } from '@/types/commons/DataResponse';
import type { ResourceInfo } from './types/ResourceInfo';
import { useSystemConfigStore } from '@/store/useSystemConfigStore';
import Admin_home from './admin_home/admin_home.vue';
import farmUserUtils from '@/hook/farmUserUtils';
import type { User } from '@/types/system/user';
import Admin_hello from './admin_home/admin_hello.vue';
import Admin_user_info from './admin_home/admin_user_info.vue';
const systemConfig = useSystemConfigStore();
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const currentUser: User = reactive({});

onMounted(() => {
  farmUserUtils.loadUser((user: User) => {
    Object.assign(currentUser, user);
  });
})
const diskInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});

const filesInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});
const ramInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});

const cacheInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});

const onlineInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});

const elogInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});

const cpuInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});
const visitInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});

const userInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});
const knowInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});

const taskInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});

const courseInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});

const cardInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});

const TypesInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});
const EventInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});

const TagsInfo = reactive(<ResourceInfo>{
  all: 0,
  use: 0,
  percent: 0,
  title: ''
});


const loadingFlag = ref(false);//加载中遮罩
const loadInfo = () => {
  Farm2Request.submit("api/current/resource", RequestTypeEnum.post, loadingFlag, {}).then((value: DataResponse) => {
    Object.assign(diskInfo, (value.data as { disk: ResourceInfo }).disk);
    Object.assign(filesInfo, (value.data as { files: ResourceInfo }).files);
    Object.assign(ramInfo, (value.data as { ram: ResourceInfo }).ram);
    Object.assign(cacheInfo, (value.data as { cache: ResourceInfo }).cache);
    Object.assign(onlineInfo, (value.data as { online: ResourceInfo }).online);
    Object.assign(elogInfo, (value.data as { elog: ResourceInfo }).elog);
    Object.assign(cpuInfo, (value.data as { cpu: ResourceInfo }).cpu);
    Object.assign(visitInfo, (value.data as { visit: ResourceInfo }).visit);
    Object.assign(userInfo, (value.data as { user: ResourceInfo }).user);
    Object.assign(knowInfo, (value.data as { knows: ResourceInfo }).knows);
    Object.assign(TagsInfo, (value.data as { tags: ResourceInfo }).tags);
    Object.assign(TypesInfo, (value.data as { types: ResourceInfo }).types);
    Object.assign(EventInfo, (value.data as { event: ResourceInfo }).event);

    Object.assign(taskInfo, (value.data as { tasks: ResourceInfo }).tasks);
    Object.assign(courseInfo, (value.data as { courses: ResourceInfo }).courses);
    Object.assign(cardInfo, (value.data as { cards: ResourceInfo }).cards);
  })
}

const clearCache = () => {
  Farm2Request.submit("api/current/clearcache", RequestTypeEnum.post, loadingFlag, {}).then(() => {
    systemConfig.clearCache();
    loadInfo();
  });
}



//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style></style>
