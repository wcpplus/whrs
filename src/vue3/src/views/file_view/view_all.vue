<template>
  <div :style="props.scrollAble ? 'height: 100%;' : ''">
    <div v-if="props.fileModel == 'THUMBNAIL_FILE'" style="height: 100%;">
      <view_thumbnail :fileid="props.fileid" :is-know-view="props.isKnowView" :extend-file-id="props.extendFileId">
      </view_thumbnail>
    </div>
    <div v-if="props.fileModel == 'TEXT_FILE'" style="height: 100%;">
      <view_text :extend-file-id="props.extendFileId"></view_text>
    </div>
    <div v-if="props.fileModel == 'IMGS_FILE'" style="height: 100%;">
      <view_imgs :extend-file-id="props.extendFileId" :scroll-able="props.scrollAble"></view_imgs>
    </div>
    <div v-if="props.fileModel == 'PDF_FILE'" :style=" props.scrollAble?' height: 100%;':'height:calc(100vh - 130px);'" >
      <view_pdf_iframe :extend-file-id="props.extendFileId"></view_pdf_iframe>
    </div>
    <div v-if="props.fileModel == 'AUDIO_MP3_FILE'" style="height: 100%;">
      <view_mp3 :extend-file-id="props.extendFileId"></view_mp3>
    </div>
    <div v-if="props.fileModel == 'VIDEO_MP4_FILE'" style="height: 100%;">
      <view_mp4 :extend-file-id="props.extendFileId"></view_mp4>
    </div>
    <div v-if="props.fileModel == 'HTML_FILE'" style="height: 100%;">
      <view_html :extend-file-id="props.extendFileId" :scroll-able="props.scrollAble"></view_html>
    </div>
  </div>
</template>
<script setup lang="ts">
import view_html from '@/views/file_view/view_html.vue';
import view_mp4 from '@/views/file_view/view_mp4.vue';
import view_mp3 from '@/views/file_view/view_mp3.vue';
import view_pdf_iframe from '@/views/file_view/view_pdf_iframe.vue';
import view_thumbnail from '@/views/file_view/view_thumbnail.vue';
import view_text from '@/views/file_view/view_text.vue';
import view_imgs from '@/views/file_view/view_imgs.vue';
const props = defineProps({
  fileModel: {
    type: String,
    required: true
  },
  extendFileId: {
    type: String,
    required: true
  }, isKnowView: {
    type: Boolean,
    required: false
  }, fileid: {
    type: String,
    required: false
  },
  scrollAble: {
    type: Boolean,
    required: false,
    default: true,
  },
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
