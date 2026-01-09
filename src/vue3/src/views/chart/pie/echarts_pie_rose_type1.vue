<template>
  <div ref="chartRef" style="width: 100%; height: 100%;"></div>
</template>
<script setup lang="ts">
import * as echarts from 'echarts/core';
import { PieChart } from 'echarts/charts';
import {
  TooltipComponent,
  LegendComponent // 建议加上图例，提升可用性
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
echarts.use([
  PieChart,
  TooltipComponent,
  LegendComponent,
  CanvasRenderer
]);
import { onMounted, ref, onBeforeUnmount, type PropType, watch } from 'vue';

const chartRef = ref<HTMLElement | null>(null);
let chart: echarts.ECharts | null = null;

const props = defineProps({
  data: {
    type: Array as () => { value: number; name: string; id: string }[],
    required: true
  },
  clickHandle: {
    type: Function as PropType<(id: string) => void>,
    required: true
  }
});

// 动态更新图表（当 data 变化时）
watch(() => props.data, () => {
  if (chart) {
    initChart();
  }
}, { deep: true });

const initChart = () => {
  if (!chartRef.value) return;

  // 销毁旧实例（避免重复初始化）
  if (chart) {
    chart.dispose();
  }

  chart = echarts.init(chartRef.value);

  // 自定义颜色（柔和、协调的配色）
  const colorPalette = [
    '#5470C6', '#91CC75', '#FAC858', '#EE6666', '#73C0DE',
    '#3BA272', '#FC8452', '#9A60B4', '#EA7CCC', '#FF9F7F'
  ];

  const option = {
    // 图例（可选，建议开启）
    legend: {
      show: true,
      bottom: '2%',
      left: 'center',
      textStyle: {
        color: '#666'
      },
      itemGap: 12,
      itemWidth: 12,
      itemHeight: 12
    },

    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#eee',
      borderRadius: 8,
      padding: [8, 12],
      textStyle: {
        color: '#333',
        fontSize: 13
      },
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      formatter: (params: any) => {
        return `${params.marker} ${params.name}<br/>占比: ${params.percent}%<br/>数值: ${params.value}`;
      }
    },

    series: [
      {
        type: 'pie',
        radius: ['30%', '70%'], // 内外半径，形成环形（可改为 ['0%', '70%'] 变实心饼图）
        center: ['50%', '45%'],
        avoidLabelOverlap: true,
        itemStyle: {
          borderRadius: 6,
          borderColor: '#fff',
          borderWidth: 2,
          shadowColor: 'rgba(0, 0, 0, 0.1)',
          shadowBlur: 10
        },
        label: {
          show: true,
          position: 'outside',
          color: '#555',
          fontSize: 12,
          fontWeight: 'normal',
          lineHeight: 16,
          // eslint-disable-next-line @typescript-eslint/no-explicit-any
          formatter: (params: any) => {
            return params.percent > 3 ? `${params.name}\n${params.percent}%` : '';
          }
        },
        labelLine: {
          show: true,
          length: 10,
          length2: 10,
          smooth: 0.5,
          lineStyle: {
            width: 1,
            color: '#aaa'
          }
        },
        data: [...props.data].sort((a, b) => b.value - a.value), // 从大到小排序，更美观
        color: colorPalette,
        emphasis: {
          scale: true,
          shadowBlur: 20,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.2)'
        },
        animationType: 'scale',
        animationEasing: 'elasticOut',
        animationDelay: (idx: number) => idx * 100
      }
    ]
  };

  chart.setOption(option);

  // 点击事件
  chart.off('click'); // 避免重复绑定
  chart.on('click', (params) => {
    const clickedData = params.data as { id: string };
    if (clickedData && clickedData.id) {
      props.clickHandle(clickedData.id);
    }
  });
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

<style scoped>
/* 可以根据需要添加样式 */
</style>
