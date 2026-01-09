<template>
  <div ref="chartRef" style="width: 100%; height: 100%; "></div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts/core';
import { BarChart } from 'echarts/charts';
import {
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';

echarts.use([
  BarChart,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  CanvasRenderer
]);

import { onMounted, ref, onBeforeUnmount, type PropType } from 'vue';

// 🔹 定义题型数据结构
interface QuestionTypeItem {
  name: string;
  value: number;      // 题目数量
  point: number;   // 每题分值
}
const props = defineProps({
  data: {
    type: Array as () => { value: number; name: string, id: string, point: number }[],
    required: false
  }, clickHandle: {
    type: Function as PropType<(id: string) => void>,
    required: false
  }
});
const chartRef = ref<HTMLDivElement | null>(null);
let chart: echarts.ECharts | null = null;

const initChart = () => {
  if (!chartRef.value) return;

  chart = echarts.init(chartRef.value);

  // 🔹 原始数据（可从 props 或 API 获取）
  const rawData: QuestionTypeItem[] = props.data || [
    { name: '单选', value: 20, point: 2 },
    { name: '多选', value: 10, point: 3 },
    { name: '判断', value: 5, point: 1 },
    { name: '填空', value: 8, point: 2 },
    { name: '简答', value: 3, point: 5 }
  ];

  const questionTypes = rawData.map(item => item.name);
  const counts = rawData.map(item => item.value);
  const totalScores = rawData.map(item => item.point);



  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
    },
    grid: {
      left: '10%',
      right: '15%',
      bottom: 70,
      top: '15%',
    },
    xAxis: {
      type: 'category',
      data: questionTypes,
      axisLabel: { fontSize: 12 },
      nameLocation: 'middle',
      nameGap: 30
    },
    yAxis: [
      {
        type: 'value',
        name: '题目数量',
        position: 'left',
        axisLabel: { formatter: '{value}' }
      },
      {
        type: 'value',
        name: '总分值（分）',
        position: 'right',
        axisLabel: { formatter: '{value} 分' }
      }
    ],
    series: [
      {
        name: '题目数量',
        type: 'bar',
        barWidth: '30%',
        itemStyle: { color: '#5470c6' },
        label: {
          show: true,
          position: 'top',
          fontSize: 11
        },
        data: counts
      },
      {
        name: '总分值',
        type: 'bar',
        yAxisIndex: 1,
        barWidth: '30%',
        itemStyle: { color: '#91cc75' },
        label: {
          show: true,
          position: 'top',
          fontSize: 11,
          formatter: (params: { value: unknown; }) => `${params.value}分`
        },
        data: totalScores
      }
    ]
  };

  chart.setOption(option);
};

const handleResize = () => {
  chart?.resize();
};

onMounted(() => {
  initChart();
  window.addEventListener('resize', handleResize);
});

onBeforeUnmount(() => {
  if (chart) {
    chart.dispose();
    chart = null;
  }
  window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>
/* 确保父容器有高度 */
</style>
