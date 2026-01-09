<template>
  <iframe :src="url" style="width: 100%;height: calc(100% - 4px);border:0px solid"></iframe>
</template>
<script setup lang="ts">
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { ref, watch } from 'vue';
const props = defineProps({
  extendFileId: {
    type: String,
    required: true
  }
});
watch(() => {
  return props.extendFileId;
}, () => {
  url.value = Farm2Request.getFullPath('api/exfiles/download/pdf/' + props.extendFileId + ".pdf");
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const url = ref(Farm2Request.getFullPath('api/exfiles/download/pdf/' + props.extendFileId + ".pdf"));
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
