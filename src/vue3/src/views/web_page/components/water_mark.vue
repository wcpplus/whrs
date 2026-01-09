<!-- GlobalWatermark.vue -->
<template>
  <div style="display: none;"></div>
</template>
<script setup lang="ts">
import { onMounted, onUnmounted, ref, watch } from 'vue';
import farmUserUtils from '@/hook/farmUserUtils';
import type { User } from '@/types/system/user';
import farmTimeUtils from '@/hook/farmTimeUtils';
import farmConfig from '@/hook/farmConfig';
import farmHtmlConfig from '@/hook/farmHtmlConfig';

const watermarkElement = ref<HTMLElement | null>(null);
const textContent = ref('');// 默认水印文本

// 监听文本变化，重新创建水印
watch([textContent], () => {
  if (textContent.value) {
    removeWatermark();
    createWatermark(textContent.value);
  }
}, { immediate: true });

// 创建水印 - 优化版
const createWatermark = (text: string) => {
  const canvas = document.createElement('canvas');
  const ctx = canvas.getContext('2d');
  const ratio = window.devicePixelRatio || 1;

  // 增大画布尺寸，优化水印间距
  const canvasWidth = 300 * ratio;
  const canvasHeight = 200 * ratio;
  canvas.width = canvasWidth;
  canvas.height = canvasHeight;

  if (!ctx) return;

  // 1. 优化文字样式
  ctx.fillStyle = 'rgba(100, 100, 100, 0.08)'; // 更柔和的灰色，透明度适中
  ctx.font = `${16 * ratio}px 'Microsoft Yahei', Arial, sans-serif`; // 更美观的中文字体
  ctx.textAlign = 'center';
  ctx.textBaseline = 'middle';

  // 2. 优化旋转角度（-20度更美观）
  ctx.translate(canvasWidth / 2, canvasHeight / 2);
  ctx.rotate(-Math.PI / 9); // 倾斜 -20 度

  // 3. 处理长文本，自动换行
  const lines = splitTextToLines(ctx, text, 200 * ratio);
  const lineHeight = 24 * ratio;

  // 4. 居中绘制多行文本
  lines.forEach((line, index) => {
    const y = (index - (lines.length - 1) / 2) * lineHeight;
    ctx.fillText(line, 0, y);
  });

  // 5. 创建水印容器，优化样式
  watermarkElement.value = document.createElement('div');
  watermarkElement.value.style.cssText = `
    pointer-events: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-image: url(${canvas.toDataURL('image/png')});
    background-repeat: repeat;
    background-size: 300px 200px; /* 匹配画布尺寸（不含ratio） */
    background-position: 0 0;
    z-index: 2147483647;
    opacity: 1;
    mix-blend-mode: normal;
  `;
  watermarkElement.value.id = '__global_watermark__';

  // 确保添加到body最顶层
  document.body.appendChild(watermarkElement.value);
};

// 文本换行处理函数
const splitTextToLines = (ctx: CanvasRenderingContext2D, text: string, maxWidth: number) => {
  const words = text.split(/(\s+)/); // 按空格分割，保留空格
  const lines: string[] = [];
  let currentLine = '';

  for (const word of words) {
    const testLine = currentLine + word;
    const metrics = ctx.measureText(testLine);
    const testWidth = metrics.width;

    if (testWidth > maxWidth && currentLine) {
      lines.push(currentLine);
      currentLine = word;
    } else {
      currentLine = testLine;
    }
  }

  if (currentLine) {
    lines.push(currentLine);
  }

  // 如果单行过长（无空格），强制截断
  return lines.map(line => {
    if (ctx.measureText(line).width > maxWidth) {
      return line.substring(0, 15) + '...'; // 超长文本截断
    }
    return line;
  });
};

// 移除水印
const removeWatermark = () => {
  if (watermarkElement.value) {
    try {
      document.body.removeChild(watermarkElement.value);
    } catch (e) {
      console.warn('水印元素已被移除', e);
    }
    watermarkElement.value = null;
  }
};

onMounted(() => {
  if (farmHtmlConfig.isWaterShow()) {
    farmUserUtils.loadUser((user: User) => {
      farmConfig.loadPara("farm2.config.sys.title.foot", (val) => {
        // 优化文本格式，增加分隔符
        const sysName = val || '内部系统';
        const userName = user.loginname || '未登录';
        const date = farmTimeUtils.dateTo14DigitString(new Date());
        textContent.value = `${sysName} | ${userName} | ${date}`;
      });
    });
  }
});

onUnmounted(() => {
  removeWatermark(); // 组件卸载时移除水印
});
</script>

<style scoped>
/* 可选：如果需要对水印样式进行额外调整 */
</style>
