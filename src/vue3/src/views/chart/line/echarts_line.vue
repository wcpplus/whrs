<template>
  <div ref="chartRef" style="width: 100%; height: 100%;"></div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts/core';
import { LineChart } from 'echarts/charts'; // 🔸 改为 LineChart
import {
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';

echarts.use([
  LineChart, // 🔸 使用 LineChart
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
}

const props = defineProps({
  title: {
    type: String,
    required: false
  },
  data: {
    type: Array as () => { value: number; name: string; }[],
    required: false
  },
  clickHandle: {
    type: Function as PropType<(id: string) => void>,
    required: false
  }
});

const chartRef = ref<HTMLDivElement | null>(null);
let chart: echarts.ECharts | null = null;

const initChart = () => {
  if (!chartRef.value) return;

  chart = echarts.init(chartRef.value);

  // 🔹 原始数据
  const rawData: QuestionTypeItem[] = props.data || [
    { name: '单选', value: 20 },
    { name: '多选', value: 10 },
    { name: '判断', value: 5 },
    { name: '填空', value: 8 },
    { name: '简答', value: 3 }
  ];

  const questionTypes = rawData.map(item => item.name);
  const counts = rawData.map(item => item.value);

  // 如果你想显示“总分”，应该是 item.value * item.point
  // const totalScores = rawData.map(item => item.value * item.point);

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' } // 折线图常用 cross
    },
    grid: {
      left: '10%',
      right: '15%',
      bottom: 70,
      top: '15%'
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
        name: '',
        position: 'left',
        axisLabel: { formatter: '{value}' }
      },
      {
        type: 'value',
        name: '',
        position: 'right',
        axisLabel: { formatter: '{value} 分' }
      }
    ],
    legend: {
      data: [props.title || '演示数据']
    },
    series: [
      {
        name: props.title || '演示数据',
        type: 'line', // 🔸 改为 line
        smooth: true, // 可选：平滑曲线
        symbol: 'circle',
        symbolSize: 6,
        lineStyle: { width: 2 },
        itemStyle: { color: '#5470c6' },
        label: {
          show: true,
          position: 'top',
          fontSize: 11
        },
        data: counts
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
