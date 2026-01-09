<template>
  <el-dialog v-if="state.dialogVisible" :append-to-body="true" style="background-color: rgb(248, 248, 250);"
    v-model="state.dialogVisible" width="800">
    <el-container style="height: 600px;">
      <el-main>
        <el-container>
          <el-container>
            <el-header>
              <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
              <Data_querys placeholder="名称" :isShowQueryPlus="true">
              </Data_querys>
              <div class="farm2-data-buttons-operators">
                <el-button icon="Plus" type="primary" v-if="false" @click="openDataFormClick" plain>创建</el-button>
                <el-button type="danger" @click="datasDeleteClick" plain>恢复默认值</el-button>
              </div>
            </el-header>
            <el-main>
              <!--数据集-->
              <div class="farm2-data-table">
                <el-table v-loading="loadingFlag" :data="dataResult.data" :stripe="true"
                  @selection-change="handleSelectionChange" :highlight-current-row="true" class="table"
                  @sort-change="(data: SortInfo) => { doDataQuery(tableUtils.getSortQuery(data)) }">
                  <el-table-column fixed="left" type="selection" width="55" />
                  <el-table-column v-if="false" sortable="custom" prop="ID" label="主鍵" min-width="120" />
                  <el-table-column prop="NAME" label="名称" min-width="200" />
                  <el-table-column prop="KEYCODE" label="关键字" min-width="200" />
                  <el-table-column prop="VAL" label="项目值" min-width="120">
                    <template #default="scope">
                      <el-button link :type="scope.row.USEROVER === '1' ? 'info' : 'primary'" icon="Edit" size="small"
                        @click="dataEditClick(scope.row)">
                        {{ scope.row.VAL }}
                      </el-button>
                    </template>
                  </el-table-column>
                  <el-table-column v-if="false" sortable="custom" prop="SHOWMODEL" label="是否显示" min-width="100" />
                  <el-table-column v-if="false" sortable="custom" prop="USERKEY" label="用户KEY" min-width="120" />
                  <el-table-column v-if="false" sortable="custom" prop="USERNAME" label="USERNAME" min-width="120" />
                  <el-table-column prop="SALARYTIME" label="计薪周期" min-width="120" />
                  <el-table-column v-if="false" fixed="right" label="操作" min-width="200">
                    <template #default="scope">
                      <el-button v-if="false" link type="primary" icon="View" size="small"
                        @click="dataViewClick(scope.row)">
                        查看
                      </el-button>
                      <el-button link type="primary" icon="Edit" size="small" @click="dataEditClick(scope.row)">
                        修改
                      </el-button>
                      <el-button v-if="false" link type="danger" icon="Delete" size="small"
                        @click="dataDeleteClick(scope.row)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-main>
            <el-footer>
              <!--分页框-->
              <el-button size="large" type="success" @click="runSalaryClick()"
                style="width: 100%;margin: 20px 0px;margin-top: 20px;">
                立即生成薪资
              </el-button>
            </el-footer>
          </el-container>
        </el-container>
      </el-main>
    </el-container>
    <!--高级查询表单-->
    <SalaryUserBase_form_query></SalaryUserBase_form_query>
    <!--数据实体表单-->
    <SalaryUserBase_form_data></SalaryUserBase_form_data>
  </el-dialog>
</template>
<script setup lang="ts">
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
import SalaryUserBase_form_query from './components/salaryuserbase_form_query.vue';
import SalaryUserBase_form_data from './components/salaryuserbase_form_data.vue';
import { getCreatType, getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { ElMessageBox } from 'element-plus';
import farmClientUtils from '@/hook/farmClientUtils';
//---------------------------------------------------------
//路由信息：
//import salaryuserbase_page from "@/views/system/salaryuserbase/salaryuserbase_page.vue";
// {
//     path: "salaryuserbase",
//     name: "salaryuserbase_page",
//     component: salaryuserbase_page,
//     meta: { title: "用户薪酬定义" },
//   },
//---------------------------------------------------------
const state = reactive({
  dialogVisible: false
})
const id2Ref = ref('');
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
    required: true,
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
  //QueryUtils.initRule(dataQuery, "parentid", chooseTreeIdRef.value, "=");
  QueryUtils.initRule(dataQuery, "id2", id2Ref.value, "=");
  farm2Request.search('api/salaryuserbase/query', dataQuery, loadingFlag)
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
    'api/salaryuserbase/batch',
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
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getUpdateType(), id: row.ID, data: row, id2: id2Ref.value } });
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
const runSalaryClick = () => {
  ElMessageBox.confirm('立即计算薪资?', { icon: 'QuestionFilled' })
    .then(() => {
      farmClientUtils.postObject('api/salaryuserbase/runSalary', { id: id2Ref.value }, () => {
        state.dialogVisible = false;
        props.refreshHandle();
      });
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
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getCreatType(), id2: id2Ref.value } });
}

//---------------------------------------------------------
const openWin = (isOpen: boolean, id2: string) => {
  id2Ref.value = id2;
  //页面加载后调用默认查询事件
  doDataQuery();
  state.dialogVisible = isOpen;
}
//---------------------------------------------------------
onMounted(() => {
  //页面加载后调用默认查询事件
  //doDataQuery();
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
});
defineExpose({
  openWin,
});
</script>
