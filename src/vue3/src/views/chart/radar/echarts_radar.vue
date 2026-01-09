<template>
  <div ref="chartRef" style="width: 100%; height: 100%;"></div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts/core';
import { RadarChart } from 'echarts/charts';
import {
  TooltipComponent,
  LegendComponent
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';

echarts.use([
  RadarChart,
  TooltipComponent,
  LegendComponent,
  CanvasRenderer
]);

import { onMounted, ref, onBeforeUnmount } from 'vue';

const props = defineProps({
  title: {
    type: String,
    required: false,
    default: '演示1'
  },
  title1: {
    type: String,
    required: false
  },
  data: {
    type: Array as () => { value: number; name: string }[],
    required: false
  },
  data1: {
    type: Array as () => { value: number; name: string }[],
    required: false
  }
});
const chartRef = ref<HTMLElement | null>(null);
let chart: echarts.ECharts | null = null;

// 获取默认演示数据
const getDefaultData = () => [
  { value: 20, name: '演示1' },
  { value: 20, name: '演示2' },
  { value: 20, name: '演示3' },
  { value: 20, name: '演示4' },
  { value: 20, name: '演示5' }
];

const initChart = () => {
  if (!chartRef.value) return;
  chart = echarts.init(chartRef.value);

  const data0 = props.data || getDefaultData();
  const data1 = props.data1 || [
    { value: 0, name: '' },
    { value: 0, name: '' },
    { value: 0, name: '' },
    { value: 0, name: '' },
    { value: 0, name: '' }
  ];

  // 使用 data0 的 name 作为 indicator（假设两组维度一致）
  const indicators = data0.map(item => ({ name: item.name, max: 100 }));

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: (params: { seriesName: string, name: string, value: number }) => {
        return `${params.seriesName}<br/>${params.name}: ${params.value}%`;
      }
    },
    legend: {
      show: true,
      top: 'top',
      textStyle: { fontSize: 12 }
    },
    radar: {
      shape: 'polygon',
      indicator: indicators,
      center: ['50%', '55%'],
      radius: '60%'
    },
    series: [
      {
        name: props.title || '个人',
        type: 'radar',
        areaStyle: { opacity: 0.6 },
        data: [{ value: data0.map(d => d.value), name: props.title || '个人' }]
      },
      props.title1 ? {

        name: props.title1 || '',
        type: 'radar',
        // 移除或大幅降低填充
        areaStyle: { opacity: 0.2 }, // 原来是 0.4，现在几乎看不见填充
        lineStyle: {
          width: 3,          // 更细
          type: 'dashed',    // 虚线，视觉上更弱
          opacity: 0.9       // 线条也稍淡
        },
        // 可选：让点更小或隐藏
        symbol: 'none',      // 隐藏顶点圆点
        data: [{ value: data1.map(d => d.value), name: props.title1 || '平均' }]
      } : undefined
    ]
  };

  chart.setOption(option);
};

const handleResize = () => {
  if (chart) {
    chart.resize();
  }
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

<style scoped></style>
