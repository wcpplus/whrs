<template>
  <div style="text-align: center; padding: 20px;">
    <div style="font-size: 12px;text-align: left;" v-if="!props.isKnowView">
      <h1>缩略图使用方法：</h1>
      <el-alert title="api/exfiles/download/thumbnail[0:小图,1:中图,2:大图]/[扩展文件id|or|附件id]" type="success"
        :closable="false" />
      <h3>如下：</h3>
    </div>
    <el-image style="cursor: pointer;" @click="showImg()" v-if="!props.isKnowView"
      :src="Farm2Request.getFullPath('api/exfiles/download/thumbnail' + 0 + '/' + props.extendFileId)"
      fit="fill"></el-image>
    <div style="font-size: 12px ;" v-if="!props.isKnowView">
      {{ Farm2Request.getFullPath('api/exfiles/download/thumbnail' + 0 + '/' + props.extendFileId) }}
    </div>
    <el-image style="cursor: pointer;" @click="showImg()" v-if="!props.isKnowView"
      :src="Farm2Request.getFullPath('api/exfiles/download/thumbnail' + 1 + '/' + props.extendFileId)"
      fit="fill"></el-image>
    <div style="font-size: 12px ;" v-if="!props.isKnowView">
      {{ Farm2Request.getFullPath('api/exfiles/download/thumbnail' + 1 + '/' + props.extendFileId) }}
    </div>
    <el-image style="cursor: pointer;" @click="showImg()"
      :src="Farm2Request.getFullPath('api/exfiles/download/thumbnail' + 2 + '/' + props.extendFileId)"
      fit="fill"></el-image>
    <div style="font-size: 12px ;" v-if="!props.isKnowView">
      {{ Farm2Request.getFullPath('api/exfiles/download/thumbnail' + 2 + '/' + props.extendFileId) }}
    </div>
  </div>
  <view_viewer_image ref="imgViewerRef"></view_viewer_image>
</template>
<script setup lang="ts">
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import view_viewer_image from './view_viewer_image.vue';
import { ref } from 'vue';

const imgViewerRef = ref();

const props = defineProps({
  extendFileId: {
    type: String,
    required: true
  },
  isKnowView: {
    type: Boolean,
    required: false,
    default: false
  }, fileid: {
    type: String,
    required: false
  },
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

const showImg = () => {
  if (props.fileid) {
    imgViewerRef.value.show(Farm2Request.getFullPath('api/files/download/' + props.fileid));
  } else {
    imgViewerRef.value.show(Farm2Request.getFullPath('api/exfiles/download/thumbnail' + 2 + '/' + props.extendFileId));
  }
}

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
