<template>
  <el-drawer v-model="formCtrl.isOpenWin" class="farm2-data-form" :append-to-body="true" :with-header="false"
    size="900px">
    <div class="title">文件预览</div>
    <div style="height: calc(100% - 61px);">
      <div v-if="fileModel == 'THUMBNAIL_FILE'" style="height: 100%;">
        <view_thumbnail :extend-file-id="extendFileId"></view_thumbnail>
      </div>
      <div v-if="fileModel == 'TEXT_FILE'||fileModel == 'SEGMENT_FILE'" style="height: 100%;">
        <view_text :extend-file-id="extendFileId"></view_text>
      </div>
      <div v-if="fileModel == 'IMGS_FILE'" style="height: 100%;">
        <view_imgs :extend-file-id="extendFileId"></view_imgs>
      </div>
      <div v-if="fileModel == 'PDF_FILE'" style="height: 100%;">
        <view_pdf_iframe :extend-file-id="extendFileId"></view_pdf_iframe>
      </div>
      <div v-if="fileModel == 'AUDIO_MP3_FILE'" style="height: 100%;">
        <view_mp3 :extend-file-id="extendFileId"></view_mp3>
      </div>
      <div v-if="fileModel == 'VIDEO_MP4_FILE'" style="height: 100%;">
        <view_mp4 :extend-file-id="extendFileId"></view_mp4>
      </div>
      <div v-if="fileModel == 'HTML_FILE'" style="height: 100%;">
        <view_html :extend-file-id="extendFileId"></view_html>
      </div>
    </div>
  </el-drawer>
</template>
<script lang="ts" setup>
import view_html from '@/views/file_view/view_html.vue';
import view_mp4 from '@/views/file_view/view_mp4.vue';
import view_mp3 from '@/views/file_view/view_mp3.vue';
import { reactive, ref } from 'vue';
import view_pdf_iframe from '@/views/file_view/view_pdf_iframe.vue';
import view_thumbnail from '@/views/file_view/view_thumbnail.vue';
import view_text from '@/views/file_view/view_text.vue';
import view_imgs from '@/views/file_view/view_imgs.vue';
//------------------------------------------------------------------------------------
const formCtrl = reactive({
  isOpenWin: false,//窗口是否打开
});

const fileModel = ref('');//当前附件模型
const extendFileId = ref('');//扩展文件id
/**
 * 打开流程设计窗口
 * @param isOpen
 */
const openWin = (row: { ID: string, FILEMODEL: string }) => {
  formCtrl.isOpenWin = true;
  fileModel.value = row.FILEMODEL;
  extendFileId.value = row.ID;
};
//---------------------------------------------------

defineExpose({
  openWin
});
</script>
