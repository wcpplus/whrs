<template>
  <div ref="chartRef" style="width: 100%; height: 100%;"></div>
</template>
<script setup lang="ts">
// >>> 按需引入 ECharts 核心和所需模块 <<<
import * as echarts from 'echarts/core';
import { PieChart } from 'echarts/charts';
import {
  TooltipComponent,
  LegendComponent
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';

// 注册用到的组件
echarts.use([
  PieChart,
  TooltipComponent,
  LegendComponent,
  CanvasRenderer
]);
import { onMounted, ref, onBeforeUnmount, type PropType } from 'vue';


const props = defineProps({
  title: {
    type: String,
    required: false
  },
  data: {
    type: Array as () => { value: number; name: string, id: string, point: number }[],
    required: false
  },
  clickHandle: {
    type: Function as PropType<(id: string) => void>,
    required: false
  }
});


const chartRef = ref<HTMLElement | null>(null);
let chart: echarts.ECharts | null = null;


const initChart = () => {
  // 确保 DOM 元素已挂载
  if (!chartRef.value) return;

  // 初始化图表
  chart = echarts.init(chartRef.value);

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    legend: {
      orient: 'horizontal',
      top: 160,                  // ← 放到顶部
      left: 'right',
      z: 10,                    // ← 提升层级
      textStyle: {
        fontSize: 12
      },
      data: props.data || ['演示1', '演示2', '演示3', '演示4', '演示5']
    },
    series: [
      {
        name: props.title || '数据',
        type: 'pie',
        radius: ['30%', '75%'],   // ← 缩小外半径，避免边缘挤压
        center: ['50%', '40%'],   // ← 上移一点
        itemStyle: {
          borderRadius: 8
        },
        label: { show: false },
        labelLine: { show: false },
        data: props.data || [
          { value: 20, name: '演示1', id: 'demo1' },
          { value: 20, name: '演示2', id: 'demo2' },
          { value: 20, name: '演示3', id: 'demo3' },
          { value: 20, name: '演示4', id: 'demo4' },
          { value: 20, name: '演示5', id: 'demo5' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };

  chart.setOption(option);
};

// 监听窗口大小变化
const handleResize = () => {
  if (chart) {
    chart.resize();
  }
};

onMounted(() => {
  // 确保在 DOM 挂载后初始化
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
/* 可以根据需要添加样式 */
</style>
