<template>

  <el-dialog v-if="state.dialogVisible" :append-to-body="true" style="background-color: rgb(248, 248, 250);" @close="() => {
    if (props.refreshHandle) {
      props.refreshHandle();
    }
  }" v-model="state.dialogVisible" width="900">
    <el-container style="height: 500px;">
      <el-main>
        <el-container>
          <el-container>
            <el-header>
              <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
              <Data_querys placeholder="名称" :isShowQueryPlus="true">
              </Data_querys>
              <div class="farm2-data-buttons-operators">
                <el-button icon="Plus" type="primary" @click="openDataFormClick" plain>创建</el-button>
                <el-button icon="Delete" type="danger" @click="datasDeleteClick" plain>删除</el-button>
              </div>
            </el-header>
            <el-main>
              <!--数据集-->
              <div class="farm2-data-table">
                <el-table v-loading="loadingFlag" :data="dataResult.data" :stripe="true"
                  @selection-change="handleSelectionChange" :highlight-current-row="true" class="table"
                  @sort-change="(data: SortInfo) => { doDataQuery(tableUtils.getSortQuery(data)) }">
                  <el-table-column fixed="left" type="selection" width="55" />
                  <el-table-column fixed="left" sortable="custom" prop="NAME" label="名称" min-width="160" />
                  <el-table-column prop="DEFAULTVAL" label="默认值" min-width="110" />
                  <el-table-column prop="KEYCODE" label="关键字" min-width="130" />
                  <el-table-column sortable="custom" prop="COMPONENTTYPE" label="类型" min-width="80" />
                  <el-table-column sortable="custom" prop="SOURCEMODEL" v-if="false" label="来源类型" min-width="120" />
                  <el-table-column prop="USEROVER" label="用户覆盖" :formatter="dic.formatSTATE" min-width="100" />
                  <el-table-column prop="SORTCODE" label="排序" min-width="60" />
                  <el-table-column sortable="custom" prop="TEMPLATEID" v-if="false" label="TEMPLATEID"
                    min-width="120" />
                  <el-table-column sortable="custom" prop="SHOWMODEL" v-if="false" label="显示类型" min-width="100" />
                  <el-table-column sortable="custom" prop="NOTE" label="备注" min-width="250" />
                  <el-table-column fixed="right" label="操作" min-width="60">
                    <template #default="scope">
                      <el-button link type="primary" v-if="false" size="small" @click="dataViewClick(scope.row)">
                        查看
                      </el-button>
                      <el-button link type="primary" size="small" @click="dataEditClick(scope.row)">
                        修改
                      </el-button>
                      <el-button link type="danger" v-if="false" size="small"
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
    <SalaryTemplateItem_form_query></SalaryTemplateItem_form_query>
    <!--数据实体表单-->
    <SalaryTemplateItem_form_data></SalaryTemplateItem_form_data>
  </el-dialog>
</template>
<script setup lang="ts">
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
import SalaryTemplateItem_form_query from './components/salarytemplateitem_form_query.vue';
import SalaryTemplateItem_form_data from './components/salarytemplateitem_form_data.vue';
import { getCreatType, getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { ElMessageBox } from 'element-plus';
import dic from './utils/dictinarys'
//---------------------------------------------------------
//路由信息：
//import salarytemplateitem_page from "@/views/system/salarytemplateitem/salarytemplateitem_page.vue";
// {
//     path: "salarytemplateitem",
//     name: "salarytemplateitem_page",
//     component: salarytemplateitem_page,
//     meta: { title: "薪酬项" },
//   },
//---------------------------------------------------------
const state = reactive({
  dialogVisible: false
});
const templateIdRef = ref('');
const eventBus = mitt();//事件总线
const dataResult: DataResult = reactive(QueryUtils.getDefaultResult());//数据查询结果
const dataQuery: DataQuery = reactive({});//查询条件
const loadingFlag = ref(true);//加载中遮罩
const multipleSelection = ref([]);//当前选定行
const handleSelectionChange = (selection: []) => {
  multipleSelection.value = selection;//选定行事件
};

const props = defineProps({
  refreshHandle: {
    type: Function,
    required: false,
  }
});
//---------------------------------------------------------
//---------------------------------------------------------

/**
 * 后台检索查询--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  Object.assign(dataQuery, query)
  QueryUtils.initRule(dataQuery, "TEMPLATEID", templateIdRef.value, "=");
  farm2Request.search('api/salarytemplateitem/query', dataQuery, loadingFlag)
    .then((remoteResult: DataResult) => {
      Object.assign(dataResult, remoteResult);
    })
    .catch((msg: string) => {
      notice.byError(msg);
    });
}

/**
 * 后台批量删除--HTTP(DELETE)
 * @param ids
 */
const doDataDelete = (ids: string[]) => {
  Farm2Request.submit(
    'api/salarytemplateitem/batch',
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

  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getUpdateType(), id: row.ID, data: row, templateId: templateIdRef.value } });
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
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getCreatType(), templateId: templateIdRef.value } });
}

//---------------------------------------------------------
const openWin = (isOpen: boolean, templateId: string) => {
  if (!templateId) { return; }
  templateIdRef.value = templateId;
  //页面加载后调用默认查询事件
  doDataQuery();
  state.dialogVisible = isOpen;
}
//---------------------------------------------------------
onMounted(() => {
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
  openWin,
});
</script>
