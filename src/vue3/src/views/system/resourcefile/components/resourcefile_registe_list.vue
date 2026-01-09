<template>
  <el-drawer v-model="isDrawerOpen" class="farm2-data-form" :append-to-body="true" :with-header="false" size="800px">
    <div class="title">附件注册信息</div>
    <!--数据集-->
    <div class="farm2-data-table">
      <el-table v-loading="loadingFlag" :data="dataResult.data" :stripe="true" @selection-change="handleSelectionChange"
        :highlight-current-row="true" class="table"
        @sort-change="(data: SortInfo) => { doDataQuery(tableUtils.getSortQuery(data)) }">
        <el-table-column fixed="left" v-if="false" type="selection" width="55" />
        <el-table-column sortable="custom" prop="CTIME" :formatter="dic.formatTime" label="注册时间" min-width="120" />
        <el-table-column sortable="custom" prop="APPID" label="业务id" min-width="120" />
        <el-table-column sortable="custom" prop="TYPE" label="业务类型" min-width="100" />
        <el-table-column sortable="custom" v-if="false"  prop="FILEID" label="附件id" min-width="120" />
        <el-table-column sortable="custom" v-if="false"  prop="NOTE" label="备注" min-width="250" />
        <el-table-column fixed="right" label="操作" min-width="60">
          <template #default="scope">
            <el-button link type="danger" icon="Delete" size="small" @click="dataDeleteClick(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-drawer>
</template>
<script setup lang="ts">
import dic from '../utils/dictinarys';
import { onMounted, reactive, ref } from 'vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import notice from '@/components/msg/FarmNotice';
import QueryUtils from '@/hook/farmQueryUtils'
import type { SortInfo } from '@/types/elementplus/SortInfo';
import type { DataQuery } from '@/types/commons/DataQuery';
import type { DataResult } from '@/types/commons/DataResult';
import tableUtils from '@/hook/farmTableUtils';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { ElMessageBox } from 'element-plus';
//---------------------------------------------------------
//路由信息：
//import resourcefileregiste_page from "@/views/system/resourcefileregiste/resourcefileregiste_page.vue";
// {
//     path: "resourcefileregiste",
//     name: "resourcefileregiste_page",
//     component: resourcefileregiste_page,
//     meta: { title: "附件注册" },
//   },
//---------------------------------------------------------
const isDrawerOpen = ref(false);
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
  farm2Request.search('api/resourcefileregiste/query', Object.assign(dataQuery, query), loadingFlag)
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
    'api/resourcefileregiste/batch',
    RequestTypeEnum.delete,
    loadingFlag, ids
  ).then(() => {
    doDataQuery();
  }).catch((msg: string) => {
    notice.byError(msg);
  });
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
 * 打开流程设计窗口
 * @param isOpen
 */
const openWin = (fileId: string) => {
  isDrawerOpen.value = true;
  doDataQuery({ "rules": [{ "key": "fileid", "value": fileId, "compara": "=" }] })
};

//---------------------------------------------------------
//---------------------------------------------------------
onMounted(() => {
  //页面加载后调用默认查询事件
});


defineExpose({
  openWin
});
</script>
