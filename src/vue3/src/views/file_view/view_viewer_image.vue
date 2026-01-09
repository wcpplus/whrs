<template>
  <div style="height: 0; overflow: hidden;">
    <div ref="imageContainer">
      <img v-for="(img, index) in images"
      :key="index"
      :src="img.minSrc"
      :data-original="img.maxSrc"
      style="visibility: hidden;" />
    </div>
  </div>
</template>

<script setup lang="ts">
import 'viewerjs/dist/viewer.css';
import Viewer from 'viewerjs';
import { nextTick, onUnmounted, reactive, ref } from 'vue';
import type { ViewImg } from '@/types/file/viewImg'

const imageContainer = ref<HTMLElement | null>(null);
let viewer: Viewer | null = null;
const images: Array<ViewImg> = reactive([]);

// 初始化查看器
const initViewer = (initialIndex: number = 0) => {
  if (viewer) {
    viewer.destroy();
  }

  if (imageContainer.value) {
    viewer = new Viewer(imageContainer.value, {
      url: 'data-original',
      inline: false,
      zIndex: 9999,
      initialViewIndex: initialIndex,  // 指定初始显示的图片索引
      toolbar: {
        zoomIn: true,
        zoomOut: true,
        oneToOne: true,
        reset: true,
        prev: true,
        next: true,
        rotateLeft: true,
        rotateRight: true,
        flipHorizontal: true,
        flipVertical: true
      }
    });
  }
};

// 展示图片组并指定当前图片
const showImages = (imgArray: ViewImg[], initialIndex: number = 0) => {
  images.length = 0;
  Object.assign(images, imgArray);
  nextTick(() => {
    initViewer(initialIndex);
    if (viewer && imgArray.length > 0) {
      viewer.show();
    }
  });
};

// 展示单张图片（兼容旧用法）
const show = (src: string) => {
  showImages([{ minSrc: src, maxSrc: src }]);
};

onUnmounted(() => {
  if (viewer) {
    viewer.destroy();
  }
});

defineExpose({
  show,
  showImages
});
</script>
