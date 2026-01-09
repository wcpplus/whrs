<template>
  <div class="video-container">
    <!-- Plyr 播放器容器 -->
    <video ref="videoPlayer" style="width: 100%;max-width:100vw; max-height: 100vh; " controls>
    </video>
  </div>
</template>
<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue';
import Plyr from 'plyr';
import 'plyr/dist/plyr.css';
import Farm2Request from '@/service/remoteRequests/Farm2Request';

// 获取 video 元素的引用
const videoPlayer = ref<HTMLVideoElement | null>(null);

// 定义 props
const props = defineProps({
  extendFileId: {
    type: String,
    required: true
  }
});

let player: Plyr | null = null;

onMounted(() => {
  if (videoPlayer.value) {
    // 初始化 Plyr 播放器
    player = new Plyr(videoPlayer.value);
    // 动态设置视频源
    const source = document.createElement('source');
    source.setAttribute('src', Farm2Request.getFullPath('api/exfiles/download/media/' + props.extendFileId + '.mp4'));
    source.setAttribute('type', 'video/mp4');
    if (videoPlayer.value) {
      videoPlayer.value.appendChild(source);
      videoPlayer.value.load(); // 加载新的视频源
    }
  }
});

onUnmounted(() => {
  if (player) {
    player.destroy(); // 销毁 Plyr 实例以避免内存泄漏
  }
});
</script>

<style scoped>
/* 样式可以根据需要添加 */
.video-container {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #000000;
  height: 100%;
  padding: 20px;
}
</style>
<style>
.video-container .plyr--audio {
  display: block;
  border-radius: 1em;
}

.video-container .plyr {
  max-height: 100%;
}
</style>
