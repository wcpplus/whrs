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
                <el-table-column sortable="custom" prop="ID" label="主鍵" min-width="120" />
                <el-table-column sortable="custom" prop="CTIME" label="创建时间" min-width="120" />
                <el-table-column sortable="custom" prop="USERKEY" label="USERKEY" min-width="120" />
                <el-table-column sortable="custom" prop="ATTENDANCETIME" label="考勤日期" min-width="120" />
                <el-table-column sortable="custom" prop="WORKHOURS" label="工作时长" min-width="100" />
                <el-table-column sortable="custom" prop="LATEM" label="迟到分钟" min-width="120" />
                <el-table-column sortable="custom" prop="EARLYM" label="早退分钟" min-width="120" />
                <el-table-column sortable="custom" prop="OVERTIMEM" label="加班时长" min-width="120" />
                <el-table-column sortable="custom" prop="ABSENTIS" label="是否缺勤" min-width="100" />
                <el-table-column sortable="custom" prop="EXCEPTIONTYPE" label="原始状态" min-width="120" />
                <el-table-column sortable="custom" prop="STATE" label="最终状态" min-width="120" />
                <el-table-column sortable="custom" prop="EXEMPTNOTE" label="豁免原因" min-width="250" />
                <el-table-column sortable="custom" prop="WORKING" label="是否工作日" min-width="100" />
                <el-table-column sortable="custom" prop="BACKUP" label="是否归档" min-width="100" />
                <el-table-column sortable="custom" prop="SSTIME" label="上午上班时间" min-width="120" />
                <el-table-column sortable="custom" prop="SXTIME" label="上午下班时间" min-width="120" />
                <el-table-column sortable="custom" prop="XSTIME" label="下午上班时间" min-width="120" />
                <el-table-column sortable="custom" prop="XXTIME" label="下午下班时间" min-width="120" />
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
  <AttendanceSummary_form_query></AttendanceSummary_form_query>
  <!--数据实体表单-->
  <AttendanceSummary_form_data></AttendanceSummary_form_data>
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
import AttendanceSummary_form_query from './components/attendancesummary_form_query.vue';
import AttendanceSummary_form_data from './components/attendancesummary_form_data.vue';
import { getCreatType, getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { ElMessageBox } from 'element-plus';
//---------------------------------------------------------
//路由信息：
//import attendancesummary_page from "@/views/system/attendancesummary/attendancesummary_page.vue";
// {
//     path: "attendancesummary",
//     name: "attendancesummary_page",
//     component: attendancesummary_page,
//     meta: { title: "考勤结果" },
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
 * 后台检索查询--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  farm2Request.search('api/attendancesummary/query', Object.assign(dataQuery, query), loadingFlag)
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
    'api/attendancesummary/batch',
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
