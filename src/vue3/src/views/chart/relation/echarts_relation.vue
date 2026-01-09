<template>
  <div ref="chartRef" style="width: 100%; height: 100%;"></div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts/core';
import { GraphChart } from 'echarts/charts';
import { CanvasRenderer } from 'echarts/renderers';

// 注册所需模块
echarts.use([
  GraphChart,
  CanvasRenderer
]);
import { onMounted, ref, onBeforeUnmount, type PropType } from 'vue';

const chartRef = ref<HTMLElement | null>(null);
let chart: echarts.ECharts | null = null;

/**
 *      data: [
          {id: 'id1', name: '技术文档', symbolSize: 70, itemStyle: { color: '#91cc75' } },
          {id: 'id2', name: '培训材料', symbolSize: 20 },
        ],
        links: [
          { source: 'id1', target: 'id2', lineStyle: { color: '#91cc75' } },
        ]
 */
const props = defineProps({
  data: {
    type: Array as () => {
      id: string;
      name: string;
      symbolSize: number;
      itemStyle?: { color: string };
    }[],
    required: true
  },
  link: {
    type: Array as () => {
      source: string;
      target: string;
      lineStyle?: { color: string };
    }[],
    required: true
  },
  mapDraggAble: {
    type: Boolean,
    required: false,
    default: false,
  },
  clickHandle: {
    type: Function as PropType<(id: string) => void>,
    required: true
  }
});
const initChart = () => {
  if (!chartRef.value) return;

  chart = echarts.init(chartRef.value);

  // 力引导布局配置
  const option = {
    // 移除了 tooltip 配置，完全禁用提示框
    // tooltip: {},
    series: [
      {
        name: '知识分类',
        // 👇 添加 blur 配置：控制非焦点节点/边的透明度
        blur: {
          itemStyle: {
            opacity: 0.4  // 设置未被聚焦的节点透明度（0 = 完全透明，1 = 不透明）
          },
          lineStyle: {
            opacity: 0.4  // 设置未被聚焦的边的透明度
          },
          label: {
            show: true,
            opacity: 0.4,
            color: '#ffffff',
            textBorderColor: '#cccccc',
            textBorderWidth: 2,
          }
        },
        type: 'graph',
        layout: 'force', // 使用力引导布局
        force: {
          repulsion: 200,   // 增大斥力，防止节点飞太远
          edgeLength: 100,
          layoutAnimation: true // 显示布局动画
        },
        roam: props.mapDraggAble, // 允许缩放和平移
        draggable: true, // 节点可拖拽
        symbolSize: 60, // 节点大小
        emphasis: {
          focus: 'adjacency',
          lineStyle: {
            width: 4
          }
        },
        label: {
          show: true,
          formatter: '{b}',
          color: '#ffffff',
          textBorderColor: '#333333',
          textBorderWidth: 1,
          fontSize: 12,
        },
        edgeLabel: {
          show: false
        },
        lineStyle: {
          color: '#8e9ed7', // 设置连线颜色为 #5470c6
          width: 2,// 可选：设置线宽
          curveness: 0.1 // 可选：轻微弧度，让图更美观（0 为直线）
        },
        itemStyle: {
          color: '#8e9ed7'  // 所有节点默认使用这个颜色
        },
        data: props.data,
        links: props.link
      }
    ]
  };

  chart.setOption(option);

  // 添加点击事件监听器
  chart.on('click', function (params) {
    // params 是一个对象，包含有关点击事件的详细信息
    // params.name 是被点击的节点的名称
    // params.data 是节点的原始数据对象
    // params.dataType 是 'node' 或 'edge'
    if (params.dataType === 'node') {
      // 只对节点点击做出反应
      const clickedData = params.data as { id: string };
      props.clickHandle(clickedData.id);
    }
    // 如果您也想处理边的点击，可以添加 else if (params.dataType === 'edge') 的判断
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
  setTimeout(() => {
    handleResize();
  }, 1000);
});

onBeforeUnmount(() => {
  if (chart) {
    // 移除点击事件监听器以避免内存泄漏
    chart.off('click');
    chart.dispose();
    chart = null;
  }
  window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>
/* 可以根据需要添加样式 */
</style>
