<template>
  <Data_navigate></Data_navigate>
  <el-row>
    <el-col :span="12">
      <div style="background-color: #ffffff;  border-radius: 1em;width:100%;height: 300px;">
        <Echarts_pie_rose></Echarts_pie_rose>
      </div>
    </el-col>
    <el-col :span="12">
      <div style="background-color: #ffffff; border-radius: 1em; width:100%;height: 300px;">
        <echarts_relation :data="datas.data" :link="datas.links" :click-handle="relationClickHandle"></echarts_relation>
      </div>
    </el-col>
  </el-row>
</template>
<script setup lang="ts">
import Data_navigate from '@/components/datas/data_navigate.vue';
import Echarts_pie_rose from '@/views/chart/pie/echarts_pie_rose.vue';
import echarts_relation from '@/views/chart/relation/echarts_relation.vue';

const datas = {
  data: [
    { id: 'id1', name: '技术文档', category: 1, symbolSize: 70, itemStyle: { color: '#91cc75' } },
    { id: 'id2', name: '项目资料', category: 0, symbolSize: 20 }
  ],
  links: [
    { source: 'id1', target: 'id2', lineStyle: { color: '#91cc75' } },
  ]
}
//------------------------------
const relationClickHandle = (id: string) => {
  alert(id);
}


</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
