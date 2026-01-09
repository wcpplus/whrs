<template>
  <div class="farm2-tree" :key="componentKey" style="padding: 0px;overflow: hidden;">
    <div style="padding: 20px;padding-bottom: 12px; padding-top: 15px; background-color: var( --el-skc-theme-color);color: #ffffff;display: flex;">
      <div style="font-size: 14px;"> 当前：{{ currentTitle.name }}</div>
      <el-button style="margin-left: auto; " size="small" type="default" @click="reloadData" icon="Refresh"
        circle></el-button>
    </div>
    <div style="padding: 20px;">
      <el-tree :default-expanded-keys="['NONE']" node-key="ID" draggable :expand-on-click-node="false" :load="loadNode"
        lazy :props="defaultProps" @node-click="handleNodeClick">
        <template v-slot="{ node, data }">
          <!--此处可自定义图标-->
          <el-icon v-if="data.icon" :size="16">
            <span v-html="data.icon"></span>
          </el-icon>&nbsp;
          {{ node.label }}
        </template>
      </el-tree>
    </div>
  </div>
</template>
<script setup lang="ts">
import farm2Request from '@/service/remoteRequests/Farm2Request';
import type { DataResult } from '@/types/commons/DataResult';
import notice from '@/components/msg/FarmNotice';
import { inject, onMounted, reactive, ref } from 'vue';
import type { Emitter, EventType } from 'mitt';
const eventBus = inject('eventBus') as Emitter<Record<EventType, unknown>>;
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
const props = defineProps({
  //加载远程树URL
  loadTreeUrl: {
    type: String,
    required: true
  },
  //mitt树节点点击事件key
  chooseNodeMittKey: {
    type: String,
    required: true
  },
  //mitt树节点刷新事件key
  treeReloadMittKey: {
    type: String,
    required: true
  },
  //是否显示控件
  isShowCtrl: {
    type: Boolean,
    required: true
  }
});
const currentTitle = reactive({ name: '无', id: '' });//当前选中节点
const componentKey = ref(0);//组件KEY，用来刷新树
//点击节点
const handleNodeClick = (data: { NAME: string, ID: string }) => {
  currentTitle.name = data.NAME;
  currentTitle.id = data.ID;
  eventBus.emit(props.chooseNodeMittKey, data);
}
const defaultProps = {
  children: 'children',
  label: 'NAME',
  isLeaf: 'isLeaf'
}
//刷新组件
const reloadData = () => {
  componentKey.value++;//改变componentKey后组件会自动刷新
}
const myMap = new Map();
/**
 * 加载远程数据
 * @param node
 * @param resolve
 */
const loadNode = (node: { data: { ID: string } }, resolve: (data: unknown) => void) => {
  myMap.set(node.data.ID, { id: node.data.ID, node: node, resolve: resolve });
  const theRules = [];
  if (node.data.ID) {
    theRules.push({ key: 'parentid', value: node.data.ID, compara: '=' });
  }
  farm2Request.search(props.loadTreeUrl, { rules: theRules })
    .then((remoteResult: DataResult) => {
      resolve(remoteResult.data);
    })
    .catch((msg: Error) => {
      notice.byError(msg.message);
    });
}

/**
 * 通过id刷新节点
 *  */
const loadNodeByID = () => {
  let ID = currentTitle?.id;
  if (!ID) {
    ID = "NONE";
  }
  if (myMap.get(ID)) {
    const { node, resolve } = myMap.get(ID);
    loadNode(node, resolve);
  } else {
    reloadData();
  }
}

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
onMounted(() => {
  eventBus.on(props.treeReloadMittKey, () => {//注册打开form表单方法
    loadNodeByID();
  })
});
</script>
<style scoped>
/* 样式可以根据需要添加 */
</style>
