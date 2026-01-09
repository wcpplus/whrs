<template>
  <el-drawer v-model="isShowWin" class="farm2-data-form" style="background-color: var(--el-farms-win-bg-color);"
    :append-to-body="true" :with-header="false" size="850px">
    <div class="title">流程设计</div>
    <div style="background-color: #ffffff; margin: 20px; border-radius: 1em; padding: 20px;">

      <el-button v-if="menuCtrl.insertNode" type="primary" @click="addNode('insert_before')" icon="Back"
        plain>插入任务</el-button>
      <el-button v-if="menuCtrl.addNode" type="primary" @click="addNode('insert_next')" icon="Right"
        plain>添加任务</el-button>
      <el-button v-if="menuCtrl.brotherNode&&false" type="primary" @click="addNode('parallel_brother')" icon="BottomRight"
        plain>添加分支</el-button>
      <el-button type="success" v-if="menuCtrl.bindCovertor" @click="bindConvertor()" icon="Edit">绑定转换器</el-button>
      <el-button type="success" v-if="menuCtrl.bindCovertor" @click="openNodeForm()" icon="SetUp">任务参数</el-button>


      <el-button type="primary" icon="RefreshRight" @click="refreshFlow()" plain></el-button>
      <el-button v-if="menuCtrl.delNode" type="danger" @click="delNode()" icon="Delete" plain>删除</el-button>
      <el-button type="danger" icon="DeleteFilled" @click="delAllNode()" plain>清空</el-button>
    </div>

    <div is-loading="isLoading" style="background-color: #ffffff;   margin: 20px; border-radius: 1em; padding: 20px;">
      <!-- 绑定ref到chart元素 -->
      <div ref="chartRef" style="width: 100%; height: 400px;"></div>
    </div>
    <div style="background-color: #ffffff;   margin: 20px; border-radius: 1em; padding: 20px;"> 当前选中：{{ chooseNodeId }}
    </div>
  </el-drawer>
  <wdapconvertor_choose_page ref="chooseWinRef"></wdapconvertor_choose_page>
  <wdapflow_flow_node_paras ref="nodeParasRef"></wdapflow_flow_node_paras>
</template>

<script setup lang="ts">
import wdapflow_flow_node_paras from './wdapflow_flow_node_paras.vue';
import * as echarts from 'echarts/core';
import { GraphChart } from 'echarts/charts'; // 你用的是 graph
import {
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers'; // 👈 关键：引入 Canvas 渲染器
echarts.use([
  GraphChart,
  // TooltipComponent, // 按需启用
  CanvasRenderer, // 👈 必须注册！
]);
import { onMounted, ref, onBeforeUnmount, nextTick, watch, reactive } from 'vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import type { DataResponse } from '@/types/commons/DataResponse';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import notice from '@/components/msg/FarmNotice';
import { type FlowData } from '../utils/flowData';
import { ElMessageBox } from 'element-plus';
import wdapconvertor_choose_page from './wdapconvertor_choose_page.vue';
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const isLoading = ref(false);//加载状态：true|false
const isShowWin = ref(false);
const chooseWinRef = ref();
const nodeParasRef=ref();//节点参数表单
const flowData = <FlowData>{};//流程图数据
// 创建一个引用
const chartRef = ref<HTMLElement | null>(null);
// 图表实例
let chart: echarts.ECharts | null = null;
let thisFlowid = '';//流程ID
const chooseNodeId = ref('');//点击节点
//控制按钮展示
const menuCtrl = reactive({
  insertNode: false,//插入
  addNode: false,//添加
  brotherNode: false,//条件
  delNode: false,//删除节点
  bindCovertor: false,//绑定转换器
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


watch(chooseNodeId, (newValue) => {
  if (newValue == 'START') {
    menuCtrl.insertNode = false;
    menuCtrl.addNode = true;
    menuCtrl.brotherNode = false;
    menuCtrl.delNode = false;
    menuCtrl.bindCovertor = false;
  } else if (newValue == 'END') {
    menuCtrl.insertNode = true;
    menuCtrl.addNode = false;
    menuCtrl.brotherNode = false;
    menuCtrl.delNode = false;
    menuCtrl.bindCovertor = false;
  } else {
    menuCtrl.insertNode = true;
    menuCtrl.addNode = true;
    menuCtrl.brotherNode = true;
    menuCtrl.delNode = true;
    menuCtrl.bindCovertor = true;
  }
  if (!newValue || newValue == '') {
    menuCtrl.insertNode = false;
    menuCtrl.addNode = false;
    menuCtrl.brotherNode = false;
    menuCtrl.delNode = false;
    menuCtrl.bindCovertor = false;
  }
});
/**
 * 绑定转换器
 */
const bindConvertor = () => {
  chooseWinRef.value.openWin((convertorId: string) => {
    farm2Request.submit('api/wdapflow/bindconvertor', RequestTypeEnum.post, isLoading,
      { id1: chooseNodeId.value, id2: convertorId }).then(() => {
        chooseNodeId.value = '';
        chooseWinRef.value.closeWin();
        loadFlow();
      }).catch((msg: string) => {
        notice.byError(msg);//报错
      });
  });
}
/**
 * 打开任务参数表单
 */
const openNodeForm=()=>{
  nodeParasRef.value.openWin(chooseNodeId.value);
}

/**
 * 添加节点
 */
const addNode = (model: string) => {
  farm2Request.submit('api/wdapflow/addnode',
    RequestTypeEnum.post,
    isLoading, { flowid: thisFlowid, baseNodeId: chooseNodeId.value, flowModel: model }).then(() => {
      chooseNodeId.value = '';
      loadFlow();
    }).catch((msg: string) => {
      notice.byError(msg);//报错
    });
}
/**
 * 删除节点
 * @param model
 */
const delNode = () => {
  farm2Request.submit('api/wdapflow/delnode',
    RequestTypeEnum.post,
    isLoading, { id: chooseNodeId.value }).then(() => {
      loadFlow();
      chooseNodeId.value = '';
    }).catch((msg: string) => {
      notice.byError(msg);//报错
    });
}
/**
 * 刷新流程图
 * @param model
 */
const refreshFlow = () => {
  if (chart) {
    chart.dispose();
  }
  initChart();
}
/**
 * 清空节点
 * @param model
 */
const delAllNode = () => {
  ElMessageBox.confirm('确认删除全部节点?', { icon: 'QuestionFilled' })
    .then(() => {
      farm2Request.submit('api/wdapflow/delallnode',
        RequestTypeEnum.post,
        isLoading, { id: thisFlowid }).then(() => {
          loadFlow();
          chooseNodeId.value = '';
        }).catch((msg: string) => {
          notice.byError(msg);//报错
        });
    }).catch(() => { });
}

/**
 * 刷新图表数据
 */
const refreshChart = () => {
  if (!chart) {
    initChart();
  }
  chart!.setOption({
    series: [{
      data: flowData.nodes,
      links: flowData.links
    }]
  });
}

const loadFlow = () => {
  farm2Request.submit('api/wdapflow/loadflow', RequestTypeEnum.post, isLoading, { id: thisFlowid }).then((response: DataResponse) => {
    Object.assign(flowData, response.data);
    refreshChart();
  }).catch((msg: string) => {
    notice.byError(msg);//报错
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
      data: flowData.nodes,
      label: {
        show: true,
        position: 'bottom',
        formatter: function (params: { data: { subTitle: unknown; }; }) {
          return params.data.subTitle;
        }
      },
      links: flowData.links
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
    chooseNodeId.value = '';
  }
  if (params.data.name) {
    //点击节点
    chooseNodeId.value = params.data.name;
  }
  initChartNodeAndLinkStyle(params);
  refreshChart();
}


/**
 * 构造图节点和连线样式
 * @param params
 */
const initChartNodeAndLinkStyle = (params: { data: { source: string, target: string, name: string } }) => {
  for (const node of flowData.nodes) {
    if (node.name == params.data.name) {
      node.itemStyle!.borderColor = '#a0c69d';
      node.itemStyle!.borderWidth = 8;
    } else {
      node.itemStyle!.borderColor = '#ffffff';
      node.itemStyle!.borderWidth = 0;
    }
  }
  for (const link of flowData.links) {
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
/**
 * 打开流程设计窗口
 * @param isOpen
 */
const openWin = (flowid: string) => {
  isShowWin.value = true;
  thisFlowid = flowid;
  nextTick(() => {
    if (chart) {
      chart.dispose();
      initChart();
      loadFlow();
    } else {
      loadFlow();
    }
    chooseNodeId.value = '';
  });
};

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
onMounted(() => {
  window.addEventListener('resize', handleResize);
});
// 在组件销毁前移除resize监听器并销毁图表实例
onBeforeUnmount(() => {
  if (chart) {
    chart.dispose(); // 销毁图表实例
  }
  window.removeEventListener('resize', handleResize); // 移除resize监听器
});

defineExpose({
  openWin,
});

</script>

<style scoped>
/* 样式可以根据需要添加 */
</style>
