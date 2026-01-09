<template><el-drawer v-model="isShowWin" class="farm2-data-form" style="background-color: var(--el-farms-win-bg-color);"
    :append-to-body="true" :with-header="false" size="650px">
    <el-container>
      <el-header class="farm2-data-form">
        <!--导航条 -->
        <div class="title">绑定转换器</div>
      </el-header>
      <el-main>
      <br/>
        <el-container>
          <el-container>
            <el-header>
              <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
              <Data_querys placeholder="名称" :isShowQueryPlus="false"> </Data_querys>
            </el-header>
            <el-main>
              <!--数据集-->
              <div class="farm2-data-table">
                <el-table v-loading="loadingFlag" :data="dataResult.data" :stripe="true"
                  @selection-change="handleSelectionChange" :highlight-current-row="true" class="table"
                  @sort-change="(data: SortInfo) => { doDataQuery(tableUtils.getSortQuery(data)) }">
                  <el-table-column v-if="false" fixed="left" type="selection" width="55" />
                  <el-table-column fixed="left" sortable="custom" prop="TITLE" label="标题" min-width="160" />
                  <el-table-column sortable="custom" prop="SFILEMODEL" label="来源model" min-width="160" />
                  <el-table-column sortable="custom" prop="TFILEMODEL" label="目标model" min-width="160" />
                  <el-table-column sortable="custom" prop="CLASSKEY" label="实现类" min-width="450" />
                  <el-table-column fixed="right" label="操作" min-width="80">
                    <template #default="scope">
                      <el-button link type="primary" icon="Edit" size="small" @click="chooseClick(scope.row)">
                        选中
                      </el-button>
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
  </el-drawer>
</template>
<script setup lang="ts">
import Data_querys from '@/components/datas/data_querys.vue';
import Data_page from '@/components/datas/data_page.vue';
import { onBeforeMount, onMounted, provide, reactive, ref } from 'vue';
import farm2Request from '@/service/remoteRequests/Farm2Request';
import notice from '@/components/msg/FarmNotice';
import QueryUtils from '@/hook/farmQueryUtils'
import mitt from 'mitt';
import { PageEvent } from '@/types/commons/PageEvent';
import type { SortInfo } from '@/types/elementplus/SortInfo';
import type { DataQuery } from '@/types/commons/DataQuery';
import type { DataResult } from '@/types/commons/DataResult';
import tableUtils from '@/hook/farmTableUtils';
const eventBus = mitt();//事件总线
let bindFunction: (convertorId: string) => void;
//---------------------------------------------------------
//路由信息：
//import wdapconvertor_page from "@/views/system/wdapconvertor/wdapconvertor_page.vue";
// {
//     path: "wdapconvertor",
//     name: "wdapconvertor_page",
//     component: wdapconvertor_page,
//     meta: { title: "文件转换器" },
//   },
//---------------------------------------------------------
const isShowWin = ref(false);//窗口展示
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
  farm2Request.search('api/wdapconvertor/query', Object.assign(dataQuery, query), loadingFlag)
    .then((remoteResult: DataResult) => {
      Object.assign(dataResult, remoteResult);
    })
    .catch((msg: string) => {
      notice.byError(msg);
    });
}

const chooseClick = (row: { ID: string }) => {
  bindFunction(row.ID);
}
/**
 * 打开流程设计窗口
 * @param isOpen
 */
const openWin = (func: (convertorId: string) => void) => {
  isShowWin.value = true;
  bindFunction = func;
};

/**
 * 关闭流程设计窗口
 * @param isOpen
 */
const closeWin = () => {
  isShowWin.value = false;
};


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

defineExpose({
  openWin, closeWin
});

</script>
