<template>
  <el-container>
    <el-header>
      <!--导航条 -->
      <Data_navigate></Data_navigate>
    </el-header>
    <el-main>
      <el-container>
        <el-container>
          <el-header>
            <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
            <Data_querys placeholder="名称" :isShowQueryPlus="true">
            </Data_querys>
            <div class="farm2-data-buttons-operators">
              <el-button type="primary" v-if="false" @click="openDataFormClick" plain><el-icon>
                  <Plus />
                </el-icon>创建</el-button>
              <el-button type="primary" @click="runClearTaskClick" plain><el-icon>
                  <Plus />
                </el-icon>运行清理任务</el-button>
              <el-button type="danger" @click="datasDeleteClick" plain><el-icon>
                  <Delete />
                </el-icon>删除</el-button>
            </div>
          </el-header>
          <el-main>
            <!--数据集-->
            <div class="farm2-data-table">
              <el-table v-loading="loadingFlag" :data="dataResult.data" :stripe="true"
                @selection-change="handleSelectionChange" :highlight-current-row="true" class="table"
                @sort-change="(data: SortInfo) => { doDataQuery(tableUtils.getSortQuery(data)) }">
                <el-table-column fixed="left" type="selection" width="55" />
                <el-table-column fixed="left" sortable="custom" prop="NOTE" label="描述" min-width="150" />
                <el-table-column sortable="custom" prop="USERKEY" label="操作人KEY" min-width="120" />
                <el-table-column sortable="custom" prop="CTIME" :formatter="dic.formatTime" label="时间"
                  min-width="160" />
                <el-table-column sortable="custom" prop="OBJTYPE" label="事件对象" min-width="120" />
                <el-table-column sortable="custom" prop="ACTIONTYPE" label="动作类型" min-width="120" />
                <el-table-column sortable="custom" prop="OBJID" label="对象ID" min-width="250" />
                 <el-table-column sortable="custom" prop="OUSERKEY" label="对象作者" min-width="120" />
                <el-table-column sortable="custom" prop="USERIP" label="操作人IP" min-width="120" />
                <el-table-column fixed="right" label="操作" min-width="200">
                  <template #default="scope">
                    <el-button link type="primary" icon="View" size="small" @click="dataViewClick(scope.row)">
                      查看
                    </el-button>
                    <el-button link type="primary" v-if="false" icon="Edit" size="small"
                      @click="dataEditClick(scope.row)">
                      修改
                    </el-button>
                    <el-button link type="danger" v-if="false" icon="Delete" size="small"
                      @click="dataDeleteClick(scope.row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-main>
          <el-footer>
            <!--分页框-->
            <Data_page :remoteData="dataResult" />
          </el-footer>
        </el-container>
      </el-container>
    </el-main>
  </el-container>
  <!--高级查询表单-->
  <EventLog_form_query></EventLog_form_query>
  <!--数据实体表单-->
  <EventLog_form_data></EventLog_form_data>
</template>
<script setup lang="ts">
import dic from './utils/dictinarys'
import Data_navigate from '@/components/datas/data_navigate.vue';
import Data_page from '@/components/datas/data_page.vue';
import { onBeforeMount, onBeforeUnmount, onMounted, provide, reactive, ref } from 'vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import Data_querys from '@/components/datas/data_querys.vue';
import notice from '@/components/msg/FarmNotice';
import QueryUtils from '@/hook/farmQueryUtils'
import mitt from 'mitt';
import { PageEvent } from '@/types/commons/PageEvent';
import type { SortInfo } from '@/types/elementplus/SortInfo';
import type { DataQuery } from '@/types/commons/DataQuery';
import type { DataResult } from '@/types/commons/DataResult';
import tableUtils from '@/hook/farmTableUtils';
import EventLog_form_query from './components/eventlog_form_query.vue';
import EventLog_form_data from './components/eventlog_form_data.vue';
import { getCreatType, getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { ElMessageBox } from 'element-plus';
//---------------------------------------------------------
//路由信息：
//import eventlog_page from "@/views/system/eventlog/eventlog_page.vue";
// {
//     path: "eventlog",
//     name: "eventlog_page",
//     component: eventlog_page,
//     meta: { title: "事件日志" },
//   },
//---------------------------------------------------------
const eventBus = mitt();//事件总线
const dataResult: DataResult = reactive(QueryUtils.getDefaultResult());//数据查询结果
const dataQuery: DataQuery = reactive({});//查询条件
const loadingFlag = ref(true);//加载中遮罩
const multipleSelection = ref([]);//当前选定行
const handleSelectionChange = (selection: []) => {
  multipleSelection.value = selection;//选定行事件
};
//---------------------------------------------------------
//---------------------------------------------------------
/**
 * 立即运行定时清理任务
 */
const runClearTaskClick = () => {
  farm2Request
    .submit('api/wsystask/clearLog', RequestTypeEnum.post, loadingFlag)
    .then(() => {
      notice.bySuccess("任务已执行")
    })
    .catch((msg: string) => {
      notice.byError(msg); // 报错
    });
}

/**
 * 后台检索查询--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  farm2Request.search('api/eventlog/query', Object.assign(dataQuery, query), loadingFlag)
    .then((remoteResult: DataResult) => {
      Object.assign(dataResult, remoteResult);
    })
    .catch((msg: Error) => {
      notice.byError(msg.message);
    });
}

/**
 * 后台批量删除--HTTP(DELETE)
 * @param ids
 */
const doDataDelete = (ids: string[]) => {
  Farm2Request.submit(
    'api/eventlog/batch',
    RequestTypeEnum.delete,
    loadingFlag, ids
  ).then(() => {
    doDataQuery();
  }).catch((msg: Error) => {
    notice.byError(msg.message);
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
 * 删除多条-事件
 * @param row
 */
const datasDeleteClick = () => {
  const selection: { ID: string; }[] = multipleSelection.value;
  const ids = selection.map(item => item.ID);
  ElMessageBox.confirm('当前选定' + ids.length + '条数据,确认删除?', { icon: 'QuestionFilled' })
    .then(() => {
      doDataDelete(ids);
    }).catch(() => { });
}



/**
 * 打开数据查看窗口-事件
 * @param row 选定行数据
 */
const dataViewClick = (row: { ID: string }) => {
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getViewType(), id: row.ID, data: row } });
}


/**
 * 打开数据创建窗口-事件
 */
const openDataFormClick = () => {
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getCreatType() } });
}

//---------------------------------------------------------
//---------------------------------------------------------
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
</script>
