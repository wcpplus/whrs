<template>
  <el-container>
    <el-header>
      <!--导航条 -->
      <Data_navigate></Data_navigate>
    </el-header>
    <el-main>
      <el-container>
        <el-aside width="284px" style="padding-left: 10px;;">
          <Data_tree :isShowCtrl="true" :treeReloadMittKey="PageEvent.data_do_reload_tree_node"
            :chooseNodeMittKey="PageEvent.data_choose_tree" loadTreeUrl="api/actions/tree"></Data_tree>
        </el-aside>
        <el-container>
          <el-header>
            <!--普通查询条件  isShowQueryPlus是否展示高级查询-->
            <Data_querys placeholder="名称" :isShowQueryPlus="true">
            </Data_querys>
            <div class="farm2-data-buttons-operators">
              <el-button type="primary" @click="openDataFormClick" plain><el-icon>
                  <Plus />
                </el-icon>创建</el-button>
              <el-button type="primary" @click="openMoveFormClick" plain>
                <el-icon>
                  <Rank />
                </el-icon>移动</el-button>
              <el-button type="primary" @click="initTreeCodeClick" plain>
                <el-icon>
                  <Refresh />
                </el-icon>初始化</el-button>
              <el-button type="primary" @click="datasAutoSortClick" plain>
                <el-icon>
                  <Sort />
                </el-icon>自动排序</el-button>
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
                <el-table-column fixed="left" sortable="custom" prop="NAME" label="名称" width="120" />
                <el-table-column sortable="custom" :formatter="dic.formatType" prop="TYPE" label="权限类型" width="120" />
                <el-table-column sortable="custom" prop="MENUKEY" label="菜单KEY" min-width="220" />
                <el-table-column sortable="custom" prop="ACTIONS" label="权限KEY" min-width="220" />
                <el-table-column sortable="custom" :formatter="dic.formatSTATE" prop="STATE" label="状态" width="80" />
                <el-table-column sortable="custom" prop="SORTCODE" label="排序" width="80" />
                <el-table-column sortable="custom" :formatter="dic.formatDomain" prop="DOMAIN" label="作用域"
                  width="120" />
                <el-table-column sortable="custom" prop="NOTE" label="备注" min-width="120" />
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
  <Actions_form_query></Actions_form_query>
  <!--数据实体表单-->
  <Actions_form_data></Actions_form_data>
  <!--移动机构表单-->
  <el-dialog v-model="dialogMoveTreeVisible" title="请选择权限菜单" width="280">
    <Data_tree :isShowCtrl="false" loadTreeUrl="api/actions/tree"
      :treeReloadMittKey="PageEvent.data_do_reload_tree_node" :chooseNodeMittKey="'moveTreeChooseNode'">
    </Data_tree>
  </el-dialog>
</template>
<script setup lang="ts">
import Data_navigate from '@/components/datas/data_navigate.vue';
import Data_tree from '@/components/datas/data_tree.vue';
import Data_page from '@/components/datas/data_page.vue';
import { onBeforeMount, onBeforeUnmount, onMounted, provide, reactive, ref } from 'vue';
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import Data_querys from '@/components/datas/data_querys.vue';
import notice from '@/components/msg/FarmNotice';
import QueryUtils from '@/hook/farmQueryUtils'
import mitt from 'mitt';
import { PageEvent } from '@/types/commons/PageEvent';
import type { SortInfo } from '@/types/elementplus/SortInfo';
import type { DataQuery } from '@/types/commons/DataQuery';
import type { DataResult } from '@/types/commons/DataResult';
import tableUtils from '@/hook/farmTableUtils';
import Actions_form_query from './components/actions_form_query.vue';
import Actions_form_data from './components/actions_form_data.vue';
import { getCreatType, getUpdateType, getViewType, type FormParam } from '@/types/commons/FormType';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import { ElMessageBox } from 'element-plus';
import dic from './utils/dictinarys'

//---------------------------------------------------------
//路由信息：
//import actions_page from "@/views/apps/actions/actions_page.vue";
// {
//     path: "actions",
//     name: "actions_page",
//     component: actions_page,
//     meta: { title: "系统权限" },
//   },
//---------------------------------------------------------
const eventBus = mitt();//事件总线
const dataResult: DataResult = reactive(QueryUtils.getDefaultResult());//数据查询结果
const dataQuery: DataQuery = reactive({});//查询条件
const loadingFlag = ref(true);//加载中遮罩
const multipleSelection = ref([]);//当前选定行
let currentTreeNode = { NAME: '', ID: '' };//缓存当前树节点
const handleSelectionChange = (selection: []) => {
  multipleSelection.value = selection;//选定行事件
};
const dialogMoveTreeVisible = ref(false)//移动树窗口
//---------------------------------------------------------
//---------------------------------------------------------

/**
 * 后台检索查询--HTTP(POST)
 * @param query
 */
const doDataQuery = async (query?: DataQuery) => {
  Farm2Request.search('api/actions/query', Object.assign(dataQuery, query), loadingFlag)
    .then((remoteResult: DataResult) => {
      Object.assign(dataResult, remoteResult);
    })
    .catch((msg: Error) => {
      notice.byError(msg.message);
    });
}


/**
 * 初始化treecode
 * @param query
 */
const initTreeCodeClick = () => {
  Farm2Request.submit('api/actions/init', RequestTypeEnum.post, loadingFlag, null)
    .then(() => {
      notice.bySuccess('操作成功');
    })
    .catch((msg: Error) => {
      notice.byError(msg.message);
    });
}


/**
 * 格式化排序--HTTP(DELETE)
 * @param ids
 */
const datasAutoSortClick = () => {
  const idArray = (multipleSelection.value as { ID: string; }[]).map(item => item.ID);
  if (idArray.length <= 0) {
    ElMessageBox.alert("必须至少选定一条记录！");
    return;
  }
  Farm2Request.submit(
    'api/actions/autosort',
    RequestTypeEnum.post,
    loadingFlag, idArray
  ).then(() => {
    doDataQuery();
    eventBus.emit(PageEvent.data_do_reload_tree_node);
  }).catch((msg: Error) => {
    notice.byError(msg.message);
  });
}


/**
 * 移动多条-事件
 * @param row
 */
const openMoveFormClick = () => {
  const idArray = (multipleSelection.value as { ID: string; }[]).map(item => item.ID);
  if (idArray.length <= 0) {
    ElMessageBox.alert("必须至少选定一条记录！");
  } else {
    dialogMoveTreeVisible.value = true;
  }
}

/**
 * 移动树选中节点
 */
const moveTreeChooseNode = (row: { NAME: string, ID: string }) => {
  //询问是否立即移动
  const idArray = (multipleSelection.value as { ID: string; }[]).map(item => item.ID);
  ElMessageBox.confirm('是否立即移动这' + idArray.length + '条数据？', { icon: 'QuestionFilled' })
    .then(() => {
      //成功：关闭窗口，刷新树
      Farm2Request.submit(
        'api/actions/move',
        RequestTypeEnum.post,
        loadingFlag, { ids: idArray, id: row.ID }
      ).then(() => {
        doDataQuery();
        dialogMoveTreeVisible.value = false;
        eventBus.emit(PageEvent.data_do_reload_tree_node);
      }).catch((msg: Error) => {
        notice.byError(msg.message);
      });
    }).catch(() => { });
}

/**
 * 选中树节点
 * @param row
 */
const chooseTreeNode = (row: { NAME: string, ID: string }) => {
  currentTreeNode = row;
  doDataQuery({ rules: [{ key: 'parentid', value: row.ID, compara: '=' }] });
}

/**
 * 后台批量删除--HTTP(DELETE)
 * @param ids
 */
const doDataDelete = (ids: string[]) => {
  Farm2Request.submit(
    'api/actions/batch',
    RequestTypeEnum.delete,
    loadingFlag, ids
  ).then(() => {
    doDataQuery();
    eventBus.emit(PageEvent.data_do_reload_tree_node);//刷新树
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
  eventBus.emit(PageEvent.data_form_open, { isOpen: true, type: <FormParam>{ type: getCreatType(), data: { ID: currentTreeNode.ID, NAME: currentTreeNode.NAME } } });
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
  eventBus.on(PageEvent.data_choose_tree, (row) => {
    chooseTreeNode(row as { NAME: string, ID: string });//用户点击树
  })
  eventBus.on('moveTreeChooseNode', (row) => {
    moveTreeChooseNode(row as { NAME: string, ID: string });//用户点击移动树
  })
});
onBeforeMount(() => {
  provide('eventBus', eventBus);//子组件注入事件总线
});
onBeforeUnmount(() => {
  eventBus.all.clear();//移除事件总线
})
</script>
