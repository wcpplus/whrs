<template>
  <img :style="'height: ' + props.width + 'px;width: ' + props.width + 'px;max-width: none;'"
    :src="'/images/photo/photo' +  (props.photoId ? props.photoId : '6')+ '.png'" />
</template>
<script setup lang="ts">

const props = defineProps({
  userKey: {
    type: String,
    required: false
  },
  photoId: {
    type: String,
    required: true
  },
  width: {
    type: Number,
    required: true
  }
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
