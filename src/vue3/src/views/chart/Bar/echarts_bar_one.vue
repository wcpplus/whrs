<template>
  <div ref="chartRef" style="width: 100%; height: 100%;"></div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts/core';
import { BarChart } from 'echarts/charts'; // 🔸 改为 BarChart
import {
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';

echarts.use([
  BarChart, // 🔸 使用 BarChart
  TooltipComponent,
  LegendComponent,
  GridComponent,
  CanvasRenderer
]);

import { onMounted, ref, onBeforeUnmount, type PropType } from 'vue';

// 🔹 定义题型数据结构
const props = defineProps({
  title: {
    type: String,
    required: false
  },
  yTitle: {
    type: String,
    required: false,
    default: '',
  },
  data: {
    type: Array as () => { value: number; name: string; color?: string }[],
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

  const rawData = props.data || [
    { name: '单选', value: 20 },
    { name: '多选', value: 10 },
    { name: '判断', value: 5 },
    { name: '填空', value: 8 },
    { name: '简答', value: 3 }
  ];

  // 构造带颜色的 series data
  const seriesData = rawData.map(item => ({
    name: item.name,
    value: item.value,
    itemStyle: {
      color: (item as { color: string }).color || '#5470c6' // 支持传入 color
    }
  }));

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    grid: {
      left: '10%',
      right: '15%',
      bottom: 70,
      top: '15%'
    },
    xAxis: {
      type: 'category',
      data: rawData.map(item => item.name),
      axisLabel: { fontSize: 12 },
      nameLocation: 'middle',
      nameGap: 30
    },
    yAxis: {
      type: 'value',
      name: props.yTitle,
      minInterval: 1,
      axisLabel: {
        formatter: (value: number) => Math.round(value).toString()
      }
    },
    legend: {
      data: [props.title || '演示数据']
    },
    series: [
      {
        name: props.title || '演示数据',
        type: 'bar',
        barWidth: '60%',
        // 移除全局 itemStyle.color，因为每个柱子有自己的颜色
        label: {
          show: true,
          position: 'top',
          fontSize: 11
        },
        data: seriesData // 👈 使用带 itemStyle 的数据
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
