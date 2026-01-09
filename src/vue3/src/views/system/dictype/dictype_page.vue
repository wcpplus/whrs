<template>
  <el-drawer class="farm2-query-form" v-model="drawerdicTypeWin" title="字典项管理" :append-to-body="true"
    :with-header="false" size="800px">
    <el-container>
      <el-header>
        <!--导航条 -->
        <Data_navigate :title="'字典选项:' + title"></Data_navigate>
      </el-header>
      <el-main>
        <el-container>
          <el-container>
            <el-header>
              <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
              <Data_querys placeholder="名称" :isShowQueryPlus="true">
              </Data_querys>
              <div class="farm2-data-buttons-operators">
                <el-button type="primary" @click="openDataFormClick" plain><el-icon>
                    <Plus />
                  </el-icon>创建</el-button>
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
                  <el-table-column sortable="custom" prop="NAME" label="名称" min-width="150" />
                  <el-table-column sortable="custom" prop="KEYID" label="关键字" min-width="150" />
                  <el-table-column sortable="custom" prop="STATE" label="状态" min-width="80" />
                  <el-table-column sortable="custom" prop="SORTCODE" label="排序" min-width="80" />
                  <el-table-column fixed="right" label="操作" min-width="200">
                    <template #default="scope">
                      <el-button link type="primary" icon="View" size="small" @click="dataViewClick(scope.row)">
                        查看
                      </el-button>
                      <el-button link type="primary" icon="Edit" size="small" @click="dataEditClick(scope.row)">
                        修改
                      </el-button>
                      <el-button link type="danger" icon="Delete" size="small"
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
    <DicType_form_query></DicType_form_query>
    <!--数据实体表单-->
    <DicType_form_data></DicType_form_data>
  </el-drawer>
</template>
<script setup lang="ts">
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
import DicType_form_query from './components/dictype_form_query.vue';
import DicType_form_data from './components/dictype_form_data.vue';
import { getCreatType, getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { ElMessageBox } from 'element-plus';
//---------------------------------------------------------
//路由信息：
//import dictype_page from "@/views/apps/dictype/dictype_page.vue";
// {
//     path: "dictype",
//     name: "dictype_page",
//     component: dictype_page,
//     meta: { title: "字典类型" },
//   },
//---------------------------------------------------------
const drawerdicTypeWin = ref(false);//窗口
const title = ref('');
const eventBus = mitt();//事件总线
const dataResult: DataResult = reactive(QueryUtils.getDefaultResult());//数据查询结果
const dataQuery: DataQuery = reactive({});//查询条件
const loadingFlag = ref(true);//加载中遮罩
const multipleSelection = ref([]);//当前选定行
const handleSelectionChange = (selection: []) => {
  multipleSelection.value = selection;//选定行事件
};
//字典entity的id
let dicEntityId = "";
//---------------------------------------------------------
//---------------------------------------------------------

/**
 * 后台检索查询--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  Object.assign(dataQuery, query);
  QueryUtils.initRule(dataQuery, "A.ENTITYID", dicEntityId, "=");
  farm2Request.search('api/dictype/query', dataQuery, loadingFlag)
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
    'api/dictype/batch',
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
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getCreatType(), id: dicEntityId } });
}
/**
 * 打开字典项窗口
 */
const openDicTypeWin = (row: { ID: string, NAME: string }) => {
  title.value = row.NAME;
  dicEntityId = row.ID;
  doDataQuery();
  drawerdicTypeWin.value = true;
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

defineExpose({
  openDicTypeWin,//打开字典选项窗口
})


</script>
