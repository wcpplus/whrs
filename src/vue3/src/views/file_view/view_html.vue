<template>
  <div style="height: 100%;background-color: #ffffff; " v-loading="isLoading">
    <div class="skc-openoffice-outbox" ref="scrollContainer">
      <div class="openoffice_box farm-content skc-wdap-html" v-html="htmlVal">
      </div>
    </div>
  </div>
  <!-- 返回顶部按钮 -->
  <button class="back-to-top-btn" v-if="scrollAble" @click="scrollToTop">返回</button>
  <view_viewer_image ref="imgViewerRef"></view_viewer_image>
</template>
<script setup lang="ts">
import { nextTick, onMounted, ref, watch } from 'vue';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import notice from '@/components/msg/FarmNotice';
import type { DataResponse } from '@/types/commons/DataResponse';
import view_viewer_image from './view_viewer_image.vue';
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const imgViewerRef = ref();
const props = defineProps({
  extendFileId: {
    type: String,
    required: true
  },
  scrollAble: {
    type: Boolean,
    required: false,
    default: true,
  }
});
const htmlVal = ref('');
const isLoading = ref(false);//加载状态：true|false
const scrollContainer = ref<HTMLElement | null>(null); // 引用滚动容器
watch(() => {
  return props.extendFileId;
}, () => {
  loadText();
});
// 滚动到顶部的方法
const scrollToTop = () => {
  if (scrollContainer.value) {
    scrollContainer.value.scrollTop = 0;
  }
}
const loadText = () => {
  Farm2Request.submit('api/exfiles/download/html/' + props.extendFileId, RequestTypeEnum.post, isLoading)
    .then((response: DataResponse) => {
      htmlVal.value = Farm2Request.initBasePathToHtml(((response as { data: { html: string } }).data.html));
      nextTick(() => bindImgClickEvents());
    }).catch((msg: string) => {
      notice.byError(msg);//报错
    });
}


const bindImgClickEvents = () => {
  if (!scrollContainer.value) return;
  const imgs = scrollContainer.value.querySelectorAll<HTMLElement>('.farm-content img');
  imgs.forEach(img => {
    img.addEventListener('click', handleImageClick as EventListenerOrEventListenerObject);
  });
}

const handleImageClick = (event: MouseEvent) => {
  const imgElement = event.target as HTMLImageElement;
  console.log('Image clicked:', imgElement.src);
  // 处理图片点击逻辑，例如打开一个查看大图的组件
  imgViewerRef.value.show(imgElement.src);
}


//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
onMounted(() => {
  loadText();
});
</script>
<style scoped>
/* 样式可以根据需要添加 */
.skc-openoffice-outbox {
  height: calc(100% - 4px);
  width: 100%;
  overflow: auto;
  border-top: 2px solid var(--bs-primary-border-subtle);
  border-bottom: 2px solid var(--bs-primary-border-subtle);
}


:deep(.openoffice_box center) {
  font-size: 14px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  /* 设置链接之间的间距 */
}

:deep(.openoffice_box center a) {
  background-color: var(--el-color-primary);
  color: white;
  border: none;
  border-radius: 5px;
  padding: 4px 8px;
  text-decoration: none;
}

:deep(.openoffice_box center H1) {
  display: none;
}

:deep(.openoffice_box table) {
  border: 1px double #dddddd;
}

:deep(.openoffice_box table td) {
  border: 1px double #dddddd;
}

.back-to-top-btn {
  position: fixed;
  bottom: 100px;
  right: 20px;
  background-color: var(--el-color-primary);
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 14px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  opacity: 0.3;
}
</style>
