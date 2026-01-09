<template>
  <div :title="textRef"><canvas ref="qrCodeCanvas"></canvas></div>
</template>
<script setup lang="ts">
import QRCode from 'qrcode';
import { onMounted, ref } from 'vue';
const props = defineProps({
  text: {
    type: String,
    required: true,
  },
  imgWidth: {
    type: Number,
    required: true,
  }
});

const textRef = ref('');
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const qrCodeCanvas = ref(); // Canvas 引用
// 生成二维码的方法
async function generateQRCode(inputText: string) {
  try {
    if (qrCodeCanvas.value) {
      textRef.value = inputText;
      await QRCode.toCanvas(qrCodeCanvas.value, inputText, { width: props.imgWidth });
    }
  } catch (error) {
    console.error('生成二维码失败:', error);
  }
}

onMounted(() => {
  generateQRCode(props.text);
})
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
