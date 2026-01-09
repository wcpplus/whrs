<template>
  <el-container>
    <el-header>
      <!--导航条 -->
      <Data_navigate></Data_navigate>
    </el-header>
    <el-main>
      <el-container>
        <el-aside width="284px" style="padding-left: 10px;;">
          <div style="margin: 0px 0px 0px 0px;background-color: #ffffff;border-radius: 16px;overflow: hidden;">
            <div style="padding: 16px 20px;background-color: var(--el-skc-theme-color);
      font-size: 14px;color:#ffffff;display: flex;padding-bottom: 12px;">
              <div style="">岗位类别</div>
              <div style="margin-left: auto;display: flex;">
                <el-button @click="reloadTypes()" size="small" icon="Refresh" round>刷新</el-button>
              </div>
            </div>
            <div style=" overflow: auto; max-height:calc(50vh - 120px); " v-loading="loadingFlag">
              <div class="ptype_for_node_out">
                <div class="ptype_for_node" @click="setCurrentId()">
                  <div><el-icon style="top: 2px;margin-right: 4px;">
                      <Refresh />
                    </el-icon>全部</div>
                </div>
              </div>
              <div class="ptype_for_node_out" v-for="value in tracktypes" :key="value.keyid">
                <div class="ptype_for_node" :class="currentIdRef == value.keyid ? 'current' : ''"
                  @click="setCurrentId(value.keyid!)">
                  <div>{{ value.name }}</div>
                </div>
              </div>
              <Web_null_min tip="暂无数据" v-if="dataResult.totalSize <= 0"></Web_null_min>
            </div>
          </div>
        </el-aside>
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
                <el-table-column v-if="false" sortable="custom" prop="ID" label="主鍵" min-width="120" />
                <el-table-column sortable="custom" prop="NAME" label="名称" min-width="250" />
                <el-table-column sortable="custom" prop="KEYID" label="编码" min-width="250" />
                <el-table-column sortable="custom" prop="TRACKTYPE_ALT" label="序列类型" min-width="120" />
                <el-table-column sortable="custom" prop="SORTCODE" label="排序" min-width="120" />
                <el-table-column sortable="custom" prop="MINSALARY" label="最低工资" min-width="120" />
                <el-table-column sortable="custom" prop="MAXSALARY" label="最高工资" min-width="120" />
                <el-table-column fixed="right" label="操作" width="200">
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
  <AuthGrade_form_query></AuthGrade_form_query>
  <!--数据实体表单-->
  <AuthGrade_form_data></AuthGrade_form_data>
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
import AuthGrade_form_query from './components/authgrade_form_query.vue';
import AuthGrade_form_data from './components/authgrade_form_data.vue';
import { getCreatType, getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { ElMessageBox } from 'element-plus';
import type { DicType } from '../dictype/utils/dictype';
import farmDicEntityUtils from '@/hook/farmDicEntityUtils';
const currentIdRef = ref();
const tracktypes: Array<DicType> = reactive([]);
//---------------------------------------------------------
//路由信息：
//import authgrade_page from "@/views/system/authgrade/authgrade_page.vue";
// {
//     path: "authgrade",
//     name: "authgrade_page",
//     component: authgrade_page,
//     meta: { title: "职级" },
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
const typeChooseHandle = (key?: string) => {
  if (key) {
    QueryUtils.initRule(dataQuery, "TRACKTYPE", key, "=");
  } else {
    if (dataQuery.rules) {
      QueryUtils.removeRuleByKey(dataQuery.rules, "TRACKTYPE");
    }
  }
  doDataQuery();
}
/**
 * 后台检索查询--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  farm2Request.search('api/authgrade/query', Object.assign(dataQuery, query), loadingFlag)
    .then((remoteResult: DataResult) => {
      Object.assign(dataResult, remoteResult);
    })
    .catch((msg: string) => {
      notice.byError(msg);
    });
}
const setCurrentId = (keyid?: string) => {
  currentIdRef.value = keyid;
  typeChooseHandle(keyid);
}
/**
 * 后台批量删除--HTTP(DELETE)
 * @param ids
 */
const doDataDelete = (ids: string[]) => {
  Farm2Request.submit(
    'api/authgrade/batch',
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
const reloadTypes = () => {
  tracktypes.length = 0;
  farmDicEntityUtils.getOptions('TRACK_TYPES', (types: Array<DicType>) => {
    Object.assign(tracktypes, types);
  });
}
//---------------------------------------------------------
onMounted(() => {
  //页面加载后调用默认查询事件
  doDataQuery();
  //事件注册
  eventBus.on(PageEvent.data_do_query, (query) => {
    doDataQuery(query as DataQuery);//数据查询方法
  })
  reloadTypes();
});
onBeforeMount(() => {
  provide('eventBus', eventBus);//子组件注入事件总线
});
onBeforeUnmount(() => {
  eventBus.all.clear();//移除事件总线
})
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
