<template>
  <div style="margin: 0px 0px 0px 0px;background-color: #ffffff;border-radius: 16px;overflow: hidden;">
    <div style="padding: 16px 20px;background-color: var(--el-skc-theme-color);
      font-size: 14px;color:#ffffff;display: flex;padding-bottom: 12px;">
      <div style="">岗位类别</div>
      <div style="margin-left: auto;display: flex;">
        <el-button size="small" @click="refrash" icon="Refresh" round>刷新</el-button>
        <el-button v-if="!props.isView" icon="Plus" style="width: 100%;" size="small" @click="openDataFormClick"
          round>添加类别</el-button>
      </div>
    </div>
    <div style=" overflow: auto; max-height:calc(50vh - 120px); " v-loading="loadingFlag">
      <div class="ptype_for_node_out">
        <div class="ptype_for_node" @click="refrash">
          <div><el-icon style="top: 2px;margin-right: 4px;">
              <Refresh />
            </el-icon>全部</div>
        </div>
      </div>
      <div class="ptype_for_node_out" v-for="value in dataResult.data" :key="value.ID">
        <div class="ptype_for_node" :class="currentIdRef == value.ID ? 'current' : ''"
          @click="setCurrentId(value.ID, value.NAME)">
          <div>{{ value.NAME }}</div>
          <div style="margin-left: auto;" >
            <span v-if="value.POSTNUM > 0" class="ptype_num"> {{ value.POSTNUM }}</span>

            <el-popover class="box-item" placement="bottom" v-if="!props.isView">
              <div>
                <div class="ptype_short_button"> <el-button @click="dataEditClick(value)" style="width: 100%;"
                    type="primary" size="small" round>修改类别</el-button></div>
                <div class="ptype_short_button"> <el-button style="width: 100%;" type="danger" size="small" round
                    @click="dataDeleteClick(value)">删除类别</el-button></div>
              </div>
              <template #reference> <el-button size="small" icon="MoreFilled" circle link /></template>
            </el-popover>
          </div>
        </div>
      </div>
      <Web_null_min tip="暂无数据" v-if="dataResult.totalSize <= 0" ></Web_null_min>
    </div>
  </div>
  <!--数据实体表单-->
  <Authptype_form_data></Authptype_form_data>
</template>
<script setup lang="ts">
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import type { DataQuery } from '@/types/commons/DataQuery';
import type { DataResult } from '@/types/commons/DataResult';
import notice from '@/components/msg/FarmNotice';
import { onBeforeMount, onBeforeUnmount, onMounted, provide, reactive, ref, type PropType } from 'vue';
import farmQueryUtils from '@/hook/farmQueryUtils';
import mitt from 'mitt';
import { PageEvent } from '@/types/commons/PageEvent';
import { getCreatType, getUpdateType, type FormParam } from '@/types/commons/FormType';
import { ElMessageBox } from 'element-plus';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import Authptype_form_data from './components/authptype_form_data.vue';
import Web_null_min from '@/views/web_page/components/web_null_min.vue';
const currentIdRef = ref('');
const eventBus = mitt();//事件总线
const loadingFlag = ref(true);//加载中遮罩
const dataQuery: DataQuery = reactive({});//查询条件
const dataResult: DataResult<{ ID: string, NAME: string, POSTNUM: number }> = reactive(farmQueryUtils.getDefaultResult()) as DataResult<{ ID: string, NAME: string, POSTNUM: number }>;//数据查询结果
const props = defineProps({
  isView: {
    type: Boolean,
    required: false,
    default: false,
  },
  chooseHandle: {
    type: Function as PropType<(id: string, name: string) => void>,
    required: true
  }
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

const refrash = () => {
  doDataQuery();
  props.chooseHandle('', '');
  currentIdRef.value = '';
}

/**
 * 删除一条-事件
 * @param row 选定行数据
 */
const dataDeleteClick = (row: { ID: string }) => {
  ElMessageBox.confirm('确认删除?', { icon: 'QuestionFilled' })
    .then(() => {
      doDataDelete([row.ID]);
    }).catch(() => { });
}
/**
 * 后台批量删除--HTTP(DELETE)
 * @param ids
 */
const doDataDelete = (ids: string[]) => {
  Farm2Request.submit(
    'api/authptype/batch',
    RequestTypeEnum.delete,
    loadingFlag, ids
  ).then(() => {
    doDataQuery();
  }).catch((msg: string) => {
    notice.byError(msg);
  });
}

/**
 * 打开数据更新窗口-事件
 * @param row 选定行数据
 */
const dataEditClick = (row: { ID: string }) => {
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getUpdateType(), id: row.ID, data: row } });
}
/**
 * 打开数据创建窗口-事件
 */
const openDataFormClick = () => {
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getCreatType() } });
}
/**
 * 后台检索查询--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  Farm2Request.search('api/authptype/query', Object.assign(dataQuery, query), loadingFlag)
    .then((remoteResult: DataResult) => {
      Object.assign(dataResult, remoteResult);
    })
    .catch((msg: string) => {
      notice.byError(msg);
    });
}
const setCurrentId = (id: string, name: string) => {
  currentIdRef.value = id;
  props.chooseHandle(id, name);
}
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
onMounted(() => {
  //页面加载后调用默认查询事件
  doDataQuery();
  //事件注册
  eventBus.on(PageEvent.data_do_query, (query) => {
    doDataQuery(query as DataQuery);//数据查询方法
  })
});
onBeforeMount(() => {
  provide('eventBus', eventBus);//子组件注入事件总线
});
onBeforeUnmount(() => {
  eventBus.all.clear();//移除事件总线
})
//设置当前菜单
defineExpose({
  setCurrentId,
});
</script>
<style scoped>
/* 样式可以根据需要添加 */
.ptype_for_node_out {
  font-size: 14px;
  border-bottom: 1px dashed #cccccc;

  .ptype_for_node {
    padding: 8px 20px;
    margin: 8px;
    border-radius: 14px;
    cursor: pointer;
    color: #666666;
    display: flex;

    .ptype_num {
      font-size: 12px;
      margin-right: 20px;
      color: var(--el-skc-theme-h-color);
    }
  }

  .ptype_for_node.current {
    background-color: var(--el-skc-theme-gray-color);
    color: #666666;
    font-weight: 700;
  }

  .ptype_for_node:hover {
    color: #666666;
    background-color: var(--el-skc-theme-gray-color);
  }

  .ptype_for_node.current:hover {
    background-color: var(--el-skc-theme-gray-color);
    color: #666666;
  }
}


.ptype_short_button {
  margin: 4px;
}
</style>
