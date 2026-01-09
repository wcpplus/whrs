<template>
  <Data_navigate></Data_navigate>

  <div style="background-color: #ffffff; margin: 20px; border-radius: 1em; padding: 20px;">

    <el-button-group class="ml-4" style="margin-left: 4px;">
      <el-button type="primary" @click="addNode" icon="DArrowLeft" plain>插入任务</el-button>
      <el-button type="primary" @click="changeNode" icon="DArrowRight" plain>添加任务</el-button>
      <el-button type="primary" @click="changeNode" icon="Share" plain>添加</el-button>
    </el-button-group>

    <el-button-group class="ml-4" style="margin-left: 20px;">
      <el-button type="success" v-if="buttonShow" icon="Edit">绑定转换器</el-button>
    </el-button-group>

    <el-button-group class="ml-4" style="margin-left: 20px;">
      <el-button type="danger" icon="Delete" plain>删除</el-button>
      <el-button type="danger" icon="DeleteFilled" plain>清空</el-button>
    </el-button-group>

    <el-button-group class="ml-4" style="margin-left: 20px;">
      <el-button type="danger" @click="onTest1" icon="Delete" plain>其他测试32进制</el-button>
    </el-button-group>
  </div>

  <div style="background-color: #ffffff; margin: 20px; border-radius: 1em; padding: 20px;">
    <!-- 绑定ref到chart元素 -->
    <div ref="chartRef" style="width: 100%; height: 400px;"></div>
  </div>
</template>

<script setup lang="ts">
import Data_navigate from '@/components/datas/data_navigate.vue';
import * as echarts from 'echarts/core';
import { GraphChart } from 'echarts/charts';
import { CanvasRenderer } from 'echarts/renderers';
echarts.use([GraphChart, CanvasRenderer]);
import { onMounted, ref, onBeforeUnmount } from 'vue';
import testData from './utils/data'
import farmBase36Utils from '@/hook/farmBase36Utils';
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//图表dom元素
const buttonShow = ref(true);
// 创建一个引用
const chartRef = ref<HTMLElement | null>(null);
// 图表实例
let chart: echarts.ECharts | null = null;
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


let newY = 300;

/**
 * 改变节点
 */
const changeNode = () => {
  newY = newY + 2;
  testData.nodes.push(
    {
      name: "newSTART" + newY,
      subTitle: "newNoode" + newY,
      x: 20,
      y: newY,
      itemStyle: {
        color: "#666666",
        borderColor: "",
        borderWidth: 0,
      },
    }
  );
  refreshChart();
}

/**
 * 添加节点
 */
const addNode = () => {
  testData.nodes[0].subTitle = (testData.nodes[0].subTitle) + "1";
  refreshChart();
}
/**
 */
const onTest1 = () => {
  console.log(farmBase36Utils.convertBase36ToDecimal("cff2"));
}

/**
 * 刷新图表数据
 */
const refreshChart = () => {
  chart!.setOption({
    series: [{
      data: testData.nodes,
      links: testData.links
    }]
  });
}

/**
 * 创建图表
 */
const initChart = () => {
  chart = echarts.init(chartRef.value);
  const option = {
    series: [{
      type: 'graph',
      layout: 'none',
      symbolSize: 30,
      roam: true,
      edgeSymbol: ['circle', 'arrow'],
      edgeSymbolSize: [4, 10],
      edgeLabel: {
        fontSize: 20
      },
      data: testData.nodes,
      label: {
        show: true,
        position: 'bottom',
        formatter: function (params: { data: { subTitle: unknown; }; }) {
          return params.data.subTitle;
        }
      },
      links: testData.links
      ,
      lineStyle: {
        opacity: 0.9,
        width: 2,
        curveness: 0
      }
    }]
  };
  chart.setOption(option);
  chart.off('click');
  chart.on('click', function (params) {
    clickChartEvent(params);
  });
}


/**
 * 用户点击连线或节点
 * @param eparams
 */
const clickChartEvent = (eparams: unknown) => {
  const params = eparams as { data: { source: string, target: string, name: string } };
  if (params.data.source && params.data.target) {
    //点击连线

  }
  if (params.data.name) {
    //点击节点

  }
  initChartNodeAndLinkStyle(params);
  refreshChart();
}


/**
 * 构造图节点和连线样式
 * @param params
 */
const initChartNodeAndLinkStyle = (params: { data: { source: string, target: string, name: string } }) => {
  for (const node of testData.nodes) {
    if (node.name == params.data.name) {
      node.itemStyle.borderColor = '#a0c69d';
      node.itemStyle.borderWidth = 8;
    } else {
      node.itemStyle.borderColor = '#ffffff';
      node.itemStyle.borderWidth = 0;
    }
  }
  for (const link of testData.links) {
    if (link.source == params.data.source
      && link.target == params.data.target) {
      link.lineStyle = {
        color: '#a0c69d',
        shadowColor: '#a0c69d',
        shadowBlur: 2,
      };
      refreshChart();
    } else {
      link.lineStyle = {
        color: "#999999",
        shadowColor: "#ffffff",
        shadowBlur: 1,
      };
    }
  }
}




// 监听窗口大小变化并更新图表
const handleResize = () => {
  if (chart) {
    chart.resize();
  }
}



//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
onMounted(() => {
  initChart();
  window.addEventListener('resize', handleResize);
});
// 在组件销毁前移除resize监听器并销毁图表实例
onBeforeUnmount(() => {
  if (chart) {
    chart.dispose(); // 销毁图表实例
  }
  window.removeEventListener('resize', handleResize); // 移除resize监听器
});
</script>

<style scoped>
/* 样式可以根据需要添加 */
</style>
